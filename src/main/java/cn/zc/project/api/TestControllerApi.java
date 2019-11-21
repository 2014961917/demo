package cn.zc.project.api;

import cn.zc.project.mbg.model.MediaFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Auther: zhangchao
 * @Date: 2019-11-21 11:14
 * @classDesc: 功能描述:(类的作用)
 * @Version: 1.0
 */
@Api(value = "测试swagger",tags = "测试swagger")
public interface TestControllerApi {

    @ApiOperation("测试")
    public String test();

    @ApiOperation("测试数据库的连接")
    List<MediaFile> mysqlMedia();
}
