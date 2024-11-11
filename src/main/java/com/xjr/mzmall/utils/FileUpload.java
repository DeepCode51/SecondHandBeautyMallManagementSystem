package com.xjr.mzmall.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUpload {
    public static void fileUpload(MultipartFile multipartFile, String filePath, String fileName) throws IOException {
        File file = new File(filePath);
        //判断文件夹是否存在
        if (!file.exists()){
            file.mkdir();
        }
        //服务器中的实际文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        multipartFile.transferTo(dest);
    }
}
