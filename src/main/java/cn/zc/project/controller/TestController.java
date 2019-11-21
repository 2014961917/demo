package cn.zc.project.controller;

import cn.zc.project.api.TestControllerApi;
import cn.zc.project.mbg.mapper.MediaFileMapper;
import cn.zc.project.mbg.model.MediaFile;
import cn.zc.project.mbg.model.MediaFileExample;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: zhangchao
 * @Date: 2019-11-15 11:11
 * @classDesc: 功能描述:(类的作用)
 * @Version: 1.0
 */
@RestController
public class TestController implements TestControllerApi {
    //@RequestMapping("/")
    public String view(){
        return "redirect:/swagger-ui.html";
    }

    @Autowired
    MediaFileMapper mediaFileMapper;

    @Override
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test() {
        return "hello world";
    }

    @Override
    @RequestMapping(value = "/my",method = RequestMethod.GET)
    public List<MediaFile> mysqlMedia() {
        MediaFileExample example = new MediaFileExample();
        example.createCriteria().andFilenameNotLike("11");
        return mediaFileMapper.selectByExample(example);
    }
}

