package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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


    @PostMapping("/upload")
    public Result<String> upload(MultipartFile multipartFile) {
//        String originalFilename = multipartFile.getOriginalFilename();
//        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String filename = UUID.randomUUID() + substring;
//        log.info("filename + {}", filename);
//
//        try {
//            aliOssUtil.upload(multipartFile.getBytes(), filename);
//            return Result.success(filename);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return Result.error(MessageConstant.UPLOAD_FAILED);
//        }
        return Result.success("https://img1.baidu.com/it/u=286074935,1365645234&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1716397200&t=eb0ac09d3dbbe10dedcb5b80bf20764e");
    }


}
