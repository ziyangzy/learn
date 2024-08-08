package com.xuecheng.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class MediaServiceClientFallback implements MediaServiceClient {
     @Override
     public String upload(MultipartFile fileData, String objectName)  {
            return null;
    }
}