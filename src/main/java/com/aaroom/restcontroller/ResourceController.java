package com.aaroom.restcontroller;

import com.aaroom.service.StorageService;
import com.aaroom.service.impl.RedisCacheService;
import com.aaroom.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sosoda on 2019/1/18.
 */
public class ResourceController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private RedisCacheService redisCacheService;


    @GetMapping(value = "/resource/img/{fileName}")
    public void convert(HttpServletRequest request,
                        HttpServletResponse response,
                        @PathVariable String fileName){
        String cacheKey = Constants.RESOURCE + fileName;
        String url = redisCacheService.get(cacheKey, String.class);
        if (url == null) {
            url = storageService.generatePresignUrl(fileName);
            redisCacheService.put(cacheKey, url, (int) StorageService.DAY);
        }
        response.setStatus(HttpStatus.FOUND.value());
        response.setHeader("Location", url);
    }
}
