package cn.zc.project.controller;

import cn.zc.project.api.MediaFileUploadControllerApi;
import cn.zc.project.common.pojo.CommonResult;
import cn.zc.project.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: zhangchao
 * @Date: 2019-11-21 16:25
 * @classDesc: 功能描述:(媒资管理)
 * @Version: 1.0
 */
@RestController
@RequestMapping("/media/upload")
public class MediaUploadUploadController implements MediaFileUploadControllerApi {

    @Autowired
    MediaUploadService mediaUploadService;

    @Override
    @PostMapping("/register")
    public CommonResult register(@RequestParam("fileMd5") String fileMd5,
                                 @RequestParam("fileName") String fileName,
                                 @RequestParam("fileSize")String fileSize,
                                 @RequestParam("mimeType")String mimeType,
                                 @RequestParam("fileExt")String fileExt) {
        return mediaUploadService.register(fileMd5, fileName, fileSize, mimeType, fileExt);
    }

    @Override
    @PostMapping("/check_chunk")
    public CommonResult checkChunk(@RequestParam("fileMd5") String fileMd5,
                                   @RequestParam("chunk") String chunk,
                                   @RequestParam("chunkSize") String chunkSize) {
        return mediaUploadService.checkChunk(fileMd5,chunk,chunkSize);
    }

    @Override
    @PostMapping("/upload_chunk")
    public CommonResult uploadChunk(@RequestParam("file") MultipartFile file,
                                    @RequestParam("chunk") String chunk,
                                    @RequestParam("fileMd5") String fileMd5) {
        return mediaUploadService.uploadChunk(file,chunk,fileMd5);
    }

    @Override
    @PostMapping("/merge_chunk")
    public CommonResult mergeChunks(@RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("fileSize") Long fileSize,
                                    @RequestParam("mimeType") String mimeType,
                                    @RequestParam("fileExt") String fileExt) {
        return mediaUploadService.mergeChunks(fileMd5, fileName, fileSize, mimeType, fileExt);
    }
}
