package com.anyi.sparrow.assist.video.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.anyi.sparrow.assist.video.dao.mapper.VideoMapper;
import com.anyi.sparrow.assist.video.domain.Video;
import com.anyi.sparrow.assist.video.domain.request.VideoReq;
import com.anyi.sparrow.assist.video.domain.response.VideoVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/8/3
 */
@Slf4j
@Service
public class VideoService extends ServiceImpl<VideoMapper, Video> {

    public List<VideoVO> listGuide(VideoReq req) {
        List<VideoVO> resp = new ArrayList<>();
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<Video> list = this.lambdaQuery()
                .eq(Video::getDeleted, false)
                .eq(Video::getActivated, true)
                .eq(Video::getType, 1)
                .orderByDesc(Video::getSort)
                .orderByDesc(Video::getPublishTime)
                .orderByDesc(Video::getUpdateTime)
                .orderByDesc(Video::getCreateTime)
                .list();
        if (CollUtil.isNotEmpty(list)) {
            resp = BeanUtil.copyToList(list, VideoVO.class);
        }

        return resp;
    }
}