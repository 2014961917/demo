package cn.zc.project.controller;

import cn.zc.project.api.MediaFileUploadControllerApi;
import cn.zc.project.common.pojo.CommonResult;
import cn.zc.project.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    MediaService mediaService;

    @Override
    @PostMapping("/register")
    public CommonResult register(String fileMd5, String fileName, String fileSize, String mimeType, String fileExt) {
        return CommonResult.success(null);
    }

    @Override
    @PostMapping("/check_chunk")
    public CommonResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/upload_chunk")
    public CommonResult uploadChunk(MultipartFile file, Integer chunk, String fileMd5) {
        return null;
    }

    @Override
    @PostMapping("/merge_chunk")
    public CommonResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimeType, String fileExt) {
        return null;
    }
}
