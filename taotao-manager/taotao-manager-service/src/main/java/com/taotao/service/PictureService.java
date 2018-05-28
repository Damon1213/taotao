package com.taotao.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by hu on 2018-05-27.
 */
public interface PictureService {
    Map uploadPicture(MultipartFile uploadFile);
}
