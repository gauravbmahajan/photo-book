package com.dropBucket.album.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.dropBucket.album.CustomUtils;
import com.dropBucket.album.response.AlbumResponse;
import com.dropBucket.album.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by gaurav on 28/3/18.
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private AmazonS3 amazonS3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Value(("#{'${upload.allowed.extension}'.split(',')}"))
    private List<String> allowedExtensions;

    @Value("${s3.public.thumbnail.location}")
    private String publicThumbnailUrl;

    private static final String THUMBNAIL_FOLDER = "Thumbnails";
    private static final String SUFFIX = "/";
    private static final String FILES_FOLDER = "Files";


    @PostConstruct
    public void initialize(){
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        amazonS3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTH_1).build();
    }

    @Override
    public List<AlbumResponse> getListOfObjects() {
        ObjectListing objectListing = amazonS3Client.listObjects(bucketName,FILES_FOLDER + SUFFIX);
        Assert.notNull(objectListing,"Something went wrong while getting object summary ");
        List<AlbumResponse> albumResponses = new ArrayList<>();
        AlbumResponse albumResponse = null;
        for (S3ObjectSummary s3ObjectSummary : objectListing.getObjectSummaries()){
            String key = s3ObjectSummary.getKey();
            if (Objects.nonNull(key) && key.lastIndexOf(SUFFIX) != key.length()-1) {
                key = key.substring(key.lastIndexOf('/')+1);
                URL url = amazonS3Client.getUrl(bucketName,THUMBNAIL_FOLDER + SUFFIX + key);
                albumResponse = new AlbumResponse();
                albumResponse.setFileName(key);
                albumResponse.setThumbnailUrl(url.toString());
                albumResponse.setUploadDate(s3ObjectSummary.getLastModified());
                albumResponses.add(albumResponse);
            }
        }
        return albumResponses;
    }

    @Override
    public byte[] getFileByName(String fileName) {
        byte[] bytes  = null;
        S3Object s3Object = amazonS3Client.getObject(bucketName,fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;

    }

    @Override
    public void uploadFile(MultipartHttpServletRequest multipartHttpServletRequest) {
        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
        File file = null;
        File thumbnail = null;
        while (iterator.hasNext()){
            String uploadFile = iterator.next();
            String extension = uploadFile.substring(uploadFile.indexOf('.')+1);
            if (allowedExtensions.contains(extension)) {
                MultipartFile multipartFile = multipartHttpServletRequest.getFile(uploadFile);
                try {
                    file = CustomUtils.ConvertMultipartFileTOFile(multipartFile);
                    Assert.notNull(file,"uploaded file should not be null.");
                } catch (IOException e) {
                    log.error("IOException while uploading file", e.getMessage());
                }
                thumbnail = CustomUtils.createThumbnailFromImage(file, uploadFile);
                Assert.notNull(thumbnail,"Thumbnail should not be null.");
                String thumbnailName = THUMBNAIL_FOLDER + SUFFIX + uploadFile;
                uploadFile = FILES_FOLDER + SUFFIX + uploadFile;
                amazonS3Client.putObject(new PutObjectRequest(bucketName, uploadFile, file));
                amazonS3Client.putObject(new PutObjectRequest(bucketName, thumbnailName, thumbnail));
            }else {
                log.info("uploaded {} format is not supported.",extension);
            }
        }
        if (Objects.nonNull(file) && Objects.nonNull(thumbnail))
        file.deleteOnExit();
        thumbnail.deleteOnExit();
    }

    @Override
    public void deleteFileByName(String fileName) {
        log.info("Inside fileService to delete Object whose key {}",fileName);
        amazonS3Client.deleteObject(bucketName,FILES_FOLDER + SUFFIX + fileName);
        amazonS3Client.deleteObject(bucketName,THUMBNAIL_FOLDER + SUFFIX + fileName);
    }
}
