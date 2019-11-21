package cn.zc.project.service;

import cn.zc.project.controller.MediaUploadController;
import cn.zc.project.mbg.mapper.MediaFileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @Auther: zhangchao
 * @Date: 2019-11-21 17:37
 * @classDesc: 功能描述:(类的作用)
 * @Version: 1.0
 */
@Service
public class MediaService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MediaUploadController.class);

    @Autowired
    MediaFileMapper mediaFileMapper;
    //上传文件根目录
    @Value("${media.upload-location}")
    String uploadPath;

    /**
     * 根据文件md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * 文件名：md5+文件扩展名
     *
     * @param fileMd5 文件md5值
     * @param fileExt 文件扩展名
     * @return 文件路径
     */
    private String getFilePath(String fileMd5, String fileExt) {
        //文件存放路径
        String filePath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) +
                "/" + fileMd5 + "/" + fileMd5 + "." + fileExt;
        return filePath;
    }

    //得到文件目录相对路径，路径中去掉根目录
    private String getFileFolderRelativePath(String fileMd5,String fileExt){
        String filePath = fileMd5.substring(0,1) + "/" +fileMd5.substring(1,2) + "/" +
                fileMd5 + "/";
        return filePath;
    }

    //得到文件所在目录
    private String getFileFolderPath(String fileMd5){
        String filePath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) +
                "/" + fileMd5 + "/";
        return filePath;
    }

    //创建文件目录
    private boolean createFileFolder(String fileMd5){
        //创建上传文件目录
        String fileFolderPath = getFileFolderPath(fileMd5);
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()){
            //创建文件夹
            boolean mkdirs = fileFolder.mkdirs();
            return mkdirs;
        }
        return true;
    }
}
