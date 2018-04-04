package com.dropBucket.album.service;

import com.dropBucket.album.response.AlbumResponse;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by gaurav on 28/3/18.
 */
public interface FileService {
    List<AlbumResponse> getListOfObjects();
    byte[] getFileByName(String fileName);
    void uploadFile(MultipartHttpServletRequest multipartHttpServletRequest);
    void deleteFileByName(String fileName);
}
