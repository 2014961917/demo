package cn.zc.project.service;

import cn.hutool.crypto.digest.DigestUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @Auther: zhangchao
 * @Date: 2019-11-21 17:37
 * @classDesc: 功能描述:(类的作用)
 * @Version: 1.0
 */
@Service
public class MediaUploadService {
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
    private String getFileFolderRelativePath(String fileMd5, String fileExt) {
        String filePath = fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" +
                fileMd5 + "/";
        return filePath;
    }

    //得到文件所在目录
    private String getFileFolderPath(String fileMd5) {
        String filePath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) +
                "/" + fileMd5 + "/";
        return filePath;
    }

    //创建文件目录
    private boolean createFileFolder(String fileMd5) {
        //创建上传文件目录
        String fileFolderPath = getFileFolderPath(fileMd5);
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            //创建文件夹
            boolean mkdirs = fileFolder.mkdirs();
            return mkdirs;
        }
        return true;
    }

    //文件上传注册
    public CommonResult register(String fileMd5, String fileName, String fileSize, String mimeType, String fileExt) {
        //检查文件是否上传
        //1.得到文件的路径
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        if (file.exists()) {
            CommonResult.failed("上传文件已注册！");
        }

        //2.查询数据库文件是否存在
        MediaFile mediaFile = mediaFileMapper.selectByPrimaryKey(fileMd5);
        if (mediaFile != null) {
            CommonResult.failed("上传文件已注册！");
        }
        boolean fileFolder = createFileFolder(fileMd5);
        if (!fileFolder) {
            //上传文件目录创建失败！
            CommonResult.failed("上传文件目录创建失败!");
        }
        return CommonResult.success();
    }

    //得到块文件所在的目录
    private String getChunkFileFolderPath(String fileMd5) {
        String fileChunkFolderPath = getFileFolderPath(fileMd5) + "/" + "chunks" + "/";
        return fileChunkFolderPath;
    }

    //检查块文件
    public CommonResult<Boolean> checkChunk(String fileMd5, String chunk, String chunkSize) {
        //得到块文件所在路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //块文件的文件名称以1，2，3..序号命名，没有扩展名
        File chunkFile = new File(chunkFileFolderPath + chunk);
        if (chunkFile.exists()) {
            return CommonResult.success(true);
        } else {
            return CommonResult.success(false);
        }
    }

    //创建块文件目录
    private boolean createChunkFileFolder(String fileMd5) {
        //创建上传文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()) {
            //创建文件夹
            return chunkFileFolder.mkdirs();
        }
        return true;
    }

    //块文件上传
    public CommonResult<Boolean> uploadChunk(MultipartFile file, String fileMd5, String chunk) {
        if (file == null) {
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
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("upload chunk file fail :{}", e.getMessage());
            return CommonResult.failed("上传文件失败！");
        } finally {
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


    public CommonResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimeType, String fileExt) {
        //获取块文件的路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()) {
            boolean mkdirs = chunkFileFolder.mkdirs();
            if (!mkdirs) {
                return CommonResult.failed("文件夹创建失败！");
            }
        }

        //合并文件路径
        File mergeFile = new File(getFilePath(fileMd5, fileExt));
        //创建合并文件
        //合并文件存在先删除再创建
        if (!mergeFile.exists()) {
            boolean delete = mergeFile.delete();
            if (!delete) {
                return CommonResult.failed("原有的合并文件删除失败！" + mergeFile.getName());
            }
        }
        boolean newFile = false;
        try {
            newFile = mergeFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("mergeChunks.create mergeFile fail :{}", e.getMessage());
        }

        if (!newFile) {
            return CommonResult.failed("块文件创建失败！");
        }
        //获取块文件，此列表是已经排好序的列表
        List<File> chunkFiles = this.getChunkFiles(chunkFileFolder);
        //合并文件
        mergeFile = this.mergeFile(mergeFile, chunkFiles);
        if (mergeFile == null) {
            return CommonResult.failed("合并块文件失败！");
        }
        //校验文件
        boolean checkResult = this.checkFileMd5(mergeFile, fileMd5);
        if (!checkResult) {
            return CommonResult.failed("校验文件失败！");
        }
        //将文件信息保持数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileid(fileMd5);
        mediaFile.setFilename(fileMd5 + "." + fileExt);
        mediaFile.setFileoriginalname(fileName);
        //文件路径保存相对路径
        mediaFile.setFilepath(getFileFolderRelativePath(fileMd5, fileExt));
        mediaFile.setFilesize(fileSize);
        mediaFile.setUploadtime(new Date());
        mediaFile.setFiletype(fileExt);
        //状态为上传成功
        mediaFile.setFilestatus("301002");
        int insert = mediaFileMapper.insert(mediaFile);
        if (insert == 1) {
            return CommonResult.success();
        }
        return CommonResult.failed("上传 异常");
    }

    private boolean checkFileMd5(File mergeFile, String md5) {
        if (mergeFile == null || StringUtils.isEmpty(md5)) {
            return false;
        }
        //进行md5校验
        FileInputStream mergeFileInputStream = null;
        try {
            mergeFileInputStream = new FileInputStream(mergeFile);
            //得到文件的MD5
            String mergeFileMd5 = DigestUtil.md5Hex(mergeFileInputStream);
            //比较md5
            if (md5.equalsIgnoreCase(mergeFileMd5)) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("checkFileMd5 error,file is:{},md5 is: {}", mergeFile.getAbsoluteFile(), md5);
        } finally {
            try {
                if (mergeFileInputStream != null) {
                    mergeFileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private File mergeFile(File mergeFile, List<File> chunkFiles) {
        try {
            //创建写文件对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile,"rw");
            //遍历分块文件开始合并
            //读取文件缓冲区
            byte[] b = new byte[1024];
            for (File chunkFile : chunkFiles) {
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile,"r");
                int len = -1;
                //读取分块文件
                while ((len = raf_read.read(b)) != -1){
                    //向合并文件中写数据
                    raf_write.write(b,0,len);
                }
                raf_read.close();
            }
            raf_write.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("merge file error:{}",e.getMessage());
            return null;
        }
        return mergeFile;
    }

    private List<File> getChunkFiles(File chunkFileFolder) {
        //获取路径下的所有的块文件
        File[] chunkFiles = chunkFileFolder.listFiles();
        //将文件数组转成list，并排序
        List<File> chunkFileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(chunkFiles)));
        //排序
        chunkFileList.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName()))
                    return 1;
                return -1;
            }
        });
        return chunkFileList;
    }
}
