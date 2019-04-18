package com.lysf.service.impl;

import com.lysf.service.FileService;
import com.lysf.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("fileService")
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        //获取扩展名
        String fileExtendName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtendName;
        logger.info("开始上传文件，上传文件的文件名：{}，上传的路径：{}，新文件名：{}",fileName,path,uploadFileName);
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        int size = (int) targetFile.length();
        if (size>10485760){
            return "文件超出10m";
        }
        try {
            file.transferTo(targetFile); //到此为止文件上传成功
            //到此为止，我们暂时没有ftp服务器暂时不会将文件再次上传到ftp服务器上，
            // 并且删除tomcat下的文件图片
        } catch (IOException e) {
            logger.error("上传文件失败",e);
//            return null;
        }
        //通过配置文件进行图片地址拼接
        return PropertiesUtil.getProperty("lysf.server.http.prefix","http://39.105.155.57:8080/comlysf/")+targetFile.getName();
    }
}
