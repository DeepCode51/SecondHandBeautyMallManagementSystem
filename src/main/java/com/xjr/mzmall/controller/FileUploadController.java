package com.xjr.mzmall.controller;

import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.ResultEnum;
import com.xjr.mzmall.service.UserService;
import com.xjr.mzmall.utils.FileUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/upload")
@Slf4j
public class FileUploadController {

    @Autowired
    private UserService userService;

    /**
     * 单图片上传
     * @param multipartFile
     * @return
     */
    @PostMapping("/goodscover")
    @ResponseBody
    public Result uploadGoodsCover(@RequestParam("file") MultipartFile multipartFile) {
        String filePath = System.getProperty("user.dir") +
                System.getProperty("file.separator") +
                "img"+System.getProperty("file.separator")+"goodscover";
        System.out.println(multipartFile.getOriginalFilename());
        String fileName = System.currentTimeMillis()+multipartFile
                .getOriginalFilename().substring(multipartFile.getOriginalFilename().length()-4);
        try {
            FileUpload.fileUpload(multipartFile,filePath,fileName);
            return Result.success(ResultEnum.SUCCESS.getMessage(),fileName,null);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail();
        }
    }

    /**
     * 多图片上传
     * @param multipartFiles
     * @return
     */
    @PostMapping("/detailimg")
    @ResponseBody
    public Result uploadGoodsImg(@RequestParam("files") MultipartFile[] multipartFiles){
        String filePath = System.getProperty("user.dir") +
                System.getProperty("file.separator") +
                "img"+System.getProperty("file.separator")+"goodsdetailimg";
        StringBuffer sb = new StringBuffer();
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = System.currentTimeMillis()+multipartFile
                    .getOriginalFilename().substring(multipartFile.getOriginalFilename().length()-4);
            try {
                FileUpload.fileUpload(multipartFile,filePath,fileName);
                sb.append(fileName).append("#");
            } catch (IOException e) {
                e.printStackTrace();
                return Result.fail();
            }
        }
        return Result.success(ResultEnum.SUCCESS.getMessage(),sb.toString(),null);
    }

    @PostMapping("/avatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile multipartFile,
                               HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String filePath = System.getProperty("user.dir") +
                System.getProperty("file.separator") +
                "img"+System.getProperty("file.separator")+"avatar";
        System.out.println(multipartFile.getOriginalFilename());
        log.info("上传的图片名称为==>" + multipartFile.getOriginalFilename());
        String fileName = System.currentTimeMillis()+multipartFile
                .getOriginalFilename().substring(multipartFile.getOriginalFilename().length()-4);
        try {
            FileUpload.fileUpload(multipartFile,filePath,fileName);
            // 这里直接修改用户头像数据
            User user1 = new User();
            user1.setUserId(user.getUserId());
            user1.setAvatar(fileName);
            userService.updateById(user1);
            user.setAvatar(fileName);
            return "redirect:/mzmall/touserinfo";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/mzmall/touserinfo";
        }
    }
}
