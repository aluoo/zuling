package com.anyi.sparrow.assist.video.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anyi.sparrow.assist.video.domain.Video;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/8/3
 */
@Mapper
@Repository
public interface VideoMapper extends BaseMapper<Video> {
}