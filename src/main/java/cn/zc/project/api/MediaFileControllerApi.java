package cn.zc.project.api;

import cn.zc.project.common.pojo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: zhangchao
 * @Date: 2019-11-21 16:10
 * @classDesc: 功能描述:(媒资管理接口)
 * @Version: 1.0
 */
@Api(value = "媒资管理接口",tags = "媒资管理接口，提供文件上传，文件处理等接口")
public interface MediaFileControllerApi {

    @ApiOperation("文件上传注册")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件的md5值",name = "fileMd5"),
            @ApiImplicitParam(value = "文件的名称",name = "fileName"),
            @ApiImplicitParam(value = "文件的大小",name = "fileSize"),
            @ApiImplicitParam(value = "媒体类型",name = "mimeType"),
            @ApiImplicitParam(value = "文件的后缀名",name = "fileExt")
    })
    public CommonResult register(
            String fileMd5,
            String fileName,
            String fileSize,
            String mimeType,
            String fileExt
    );

    @ApiOperation("分块检查")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件的md5值",name = "fileMd5"),
            @ApiImplicitParam(value = "块名称",name = "chunk"),
            @ApiImplicitParam(value = "块大小",name = "chunkSize")
    })
    public CommonResult checkChunk(
            String fileMd5,
            Integer chunk,
            Integer chunkSize
    );

    @ApiOperation("上传分块")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "块文件",name = "file"),
            @ApiImplicitParam(value = "块名称",name = "chunk"),
            @ApiImplicitParam(value = "文件的MD5",name = "fileMd5")
    })
    public CommonResult uploadChunk(
            MultipartFile file,
            Integer chunk,
            String fileMd5
    );

    @ApiOperation("合并文件")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件的MD5",name = "fileMd5"),
            @ApiImplicitParam(value = "文件的名称",name = "fileName"),
            @ApiImplicitParam(value = "文件的大小",name = "fileSize"),
            @ApiImplicitParam(value = "媒体类型",name = "mimeType"),
            @ApiImplicitParam(value = "文件的后缀名",name = "fileExt")
    })
    public CommonResult mergeChunks(
            String fileMd5,
            String fileName,
            Long fileSize,
            String mimeType,
            String fileExt
    );
}
