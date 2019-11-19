package cn.zc.project.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * Created by macro on 2019/4/8.
 */
@Configuration
@MapperScan({"cn.zc.project.mbg.mapper","cn.zc.project.demo.dao"})
public class MyBatisConfig {
}
