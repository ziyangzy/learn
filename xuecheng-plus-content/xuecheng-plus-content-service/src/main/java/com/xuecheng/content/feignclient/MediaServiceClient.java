package com.xuecheng.content.feignclient;

import com.xuecheng.content.config.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description 媒资管理服务远程接口
 * @author Mr.M
 * @date 2022/9/20 20:29
 * @version 1.0
 */
@FeignClient(value = "media-api",configuration = MultipartSupportConfig.class,
        fallbackFactory = MediaServiceClientFallbackFactory.class)
//@FeignClient(value = "media-api",configuration = MultipartSupportConfig.class,
//        fallback = MediaServiceClientFallback.class)
@RequestMapping("/media")
public interface MediaServiceClient {
 @RequestMapping(value = "/upload/coursefile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 String upload(@RequestPart("filedata") MultipartFile upload,
               @RequestParam(value = "objectName",required=false) String objectName);
}