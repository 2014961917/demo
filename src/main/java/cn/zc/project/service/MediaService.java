package cn.zc.project.service;

import cn.zc.project.common.pojo.CommonResult;
import cn.zc.project.controller.MediaUploadUploadController;
import cn.zc.project.mbg.mapper.MediaFileMapper;
import cn.zc.project.mbg.model.MediaFile;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Auther: zhangchao
 * @Date: 2019-11-21 17:37
 * @classDesc: 功能描述:(类的作用)
 * @Version: 1.0
 */
@Service
public class MediaService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MediaUploadUploadController.class);

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

    //文件上传注册
    public CommonResult register(String fileMd5,String fileName,String fileSize,String mimeType,String fileExt){
        //检查文件是否上传
        //1.得到文件的路径
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        if (file.exists()){
            CommonResult.failed("上传文件已注册！");
        }

        //2.查询数据库文件是否存在
        MediaFile mediaFile = mediaFileMapper.selectByPrimaryKey(fileMd5);
        if (mediaFile != null){
            CommonResult.failed("上传文件已注册！");
        }
        boolean fileFolder = createFileFolder(fileMd5);
        if (!fileFolder){
            //上传文件目录创建失败！
            CommonResult.failed("上传文件目录创建失败!");
        }
        return CommonResult.success();
    }

    //得到块文件所在的目录
    private String getChunkFileFolderPath(String fileMd5){
        String fileChunkFolderPath = getFileFolderPath(fileMd5) + "/" + "chunks" + "/";
        return fileChunkFolderPath;
    }

    //检查块文件
    public CommonResult<Boolean> checkChunk(String fileMd5,String chunk,String chunkSize){
        //得到块文件所在路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //块文件的文件名称以1，2，3..序号命名，没有扩展名
        File chunkFile = new File(chunkFileFolderPath + chunk);
        if (chunkFile.exists()){
            return CommonResult.success(true);
        }else {
            return CommonResult.success(false);
        }
    }

    //创建块文件目录
    private boolean createChunkFileFolder(String fileMd5){
        //创建上传文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()){
            //创建文件夹
            return chunkFileFolder.mkdirs();
        }
        return true;
    }

    //块文件上传
    public CommonResult<Boolean> uploadChunk(MultipartFile file,String fileMd5,String chunk){
        if (file  == null){
            return CommonResult.failed("上传文件为空");
        }
        //创建块文件目录
        boolean fileFolder = createChunkFileFolder(fileMd5);
        //块文件
        File chunkFile = new File(getChunkFileFolderPath(fileMd5) + chunk);
        //上传的块文件
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkFile);
            IOUtils.copy(inputStream,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("upload chunk file fail :{}",e.getMessage());
            return CommonResult.failed("上传文件失败！");
        }finally {
            try {
                if (inputStream != null)
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null)
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return CommonResult.success(true);
    }


    public CommonResult mergeChunks(String fileMd5,String fileName,Long fileSize,String mimeType,String fileExt){
        //获取块文件的路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()){
            boolean mkdirs = chunkFileFolder.mkdirs();
            if (!mkdirs){
                return CommonResult.failed("文件夹创建失败！");
            }
        }

        //合并文件路径
        File mergeFile = new File(getFilePath(fileMd5,fileExt));
        //创建合并文件
        //合并文件存在先删除再创建
        if (!mergeFile.exists()){
            boolean delete = mergeFile.delete();
            if (!delete){
                return CommonResult.failed("原有的合并文件删除失败！"+mergeFile.getName());
            }
        }


        return CommonResult.success();
    }
}
