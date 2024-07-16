package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @RequestMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile multipartFile){
    if(multipartFile == null || multipartFile.isEmpty()){
        return Result.error("文件不能为空");
    }
    log.info("上传文件，文件名：{}",multipartFile.getOriginalFilename());
    try {
        String fileName = multipartFile.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String objectName = UUID.randomUUID().toString() + extension;
        String url = aliOssUtil.upload(multipartFile.getBytes(),objectName);
        return Result.success(url);
    } catch (IOException e) {
        log.error("上传文件失败，{}",e);
    }

    return Result.error("文件上传失败");
}

}
