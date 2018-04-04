package com.dropBucket.album.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by gaurav on 3/4/18.
 */
@Getter
@Setter
@ToString
public class AlbumResponse {

    private String fileName;
    private String thumbnailUrl;
    private Date uploadDate;
}
