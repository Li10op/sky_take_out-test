package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用Controller
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

//    @Autowired
//    private AliOssUtil aliOssUtil;


    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);
//  阿里云对象存储服务失效，重新定义本地存储
//        try {
////            原始文件名
//            String originalFileName = file.getOriginalFilename();
////            截取文件后缀
//            String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
//            String newFileName = UUID.randomUUID().toString() + fileType;
//
//            String filePath = aliOssUtil.upload(file.getBytes(), newFileName);
//            return Result.success(filePath);
//        } catch (IOException e) {
//            log.error("文件上传失败",e);
//        }
        try {
//        file是临时文件对象，需要转存到指定位置，否则本次请求结束后会完成临时文件的删除
            log.info(file.toString());
//        1、获取原始文件名
            String getOriginalFilename = file.getOriginalFilename();
//        2、获取上传文件的后缀名
            String fileType = getOriginalFilename.substring(getOriginalFilename.lastIndexOf("."));
//        3、重新为文件生成文件名，使用uuid生成，防止文件名重复造成文件覆盖问题
            String filerename = UUID.randomUUID().toString() + fileType;
//        4、获取本机桌面地址，创建文件夹存放图片资源
            File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
            String desktopPath = desktopDir.getAbsolutePath();
            File image_temp = new File(desktopPath + "\\image_temp");
//        文件夹不存在则创建
            if (!image_temp.exists()) {
                image_temp.mkdir();
            }

//        5、将临时文件转存到指定位置
            try {
                file.transferTo(new File(image_temp + "\\" + filerename));
            } catch (Exception e) {
                e.printStackTrace();
            }
//        此处文件路径为nginx静态资源访问路径
//        在nginx服务器配置了映射，使得可以通过image/filename可以直接访问到桌面目录文件
            String filePath = "image/" + filerename;
            return Result.success(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }


}
