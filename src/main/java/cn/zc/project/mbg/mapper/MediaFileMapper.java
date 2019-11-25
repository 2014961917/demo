package cn.zc.project.mbg.mapper;

import cn.zc.project.mbg.model.MediaFile;
import cn.zc.project.mbg.model.MediaFileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

public interface MediaFileMapper {
    long countByExample(MediaFileExample example);

    int deleteByExample(MediaFileExample example);

    int deleteByPrimaryKey(String id);

    int insert(MediaFile record);

    int insertSelective(MediaFile record);

    List<MediaFile> selectByExample(MediaFileExample example);

    MediaFile selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MediaFile record, @Param("example") MediaFileExample example);

    int updateByExample(@Param("record") MediaFile record, @Param("example") MediaFileExample example);

    int updateByPrimaryKeySelective(MediaFile record);

    int updateByPrimaryKey(MediaFile record);
}