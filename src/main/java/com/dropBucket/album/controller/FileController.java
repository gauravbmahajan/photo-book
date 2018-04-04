package com.dropBucket.album.controller;

import com.dropBucket.album.response.AlbumResponse;
import com.dropBucket.album.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by gaurav on 28/3/18.
 */
@Slf4j
@RestController
@RequestMapping("/dropbox/api")
@CrossOrigin("*")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping()
    public List<AlbumResponse> getListOfFiles(){
        log.info("inside File Controller");
        return fileService.getListOfObjects();
    }

    @GetMapping("/")
    public byte[] getTheFileByName(@RequestParam("fileName") String fileName){
        log.info("Inside get file by name {} in controller", fileName);
        return fileService.getFileByName(fileName);
    }

    @PostMapping(value = "/")
    public void uploadFile(MultipartHttpServletRequest multipartHttpServletRequest){
        log.info("inside file controller to upload file");
        fileService.uploadFile(multipartHttpServletRequest);
    }

    @DeleteMapping()
    public void deleteFileByName(@RequestParam("fileName") String fileName){
        log.info("Inside FileController to delete file {} ",fileName);
        fileService.deleteFileByName(fileName);
    }
}
