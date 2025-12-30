package com.anyi.sparrow.assist.video.controller;

import com.anyi.sparrow.assist.video.domain.request.VideoReq;
import com.anyi.sparrow.assist.video.domain.response.VideoVO;
import com.anyi.sparrow.assist.video.service.VideoService;
import com.anyi.common.domain.param.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/8/3
 */
@Slf4j
@Api(tags = "视频")
@RestController
@RequestMapping("/eapp/v1.0/video")
public class VideoController {
    @Autowired
    private VideoService service;

    @ApiOperation("视频教程列表")
    @RequestMapping(value = "/list/guide", method = RequestMethod.GET)
    public Response<List<VideoVO>> listGuide(VideoReq req) {
        return Response.ok(service.listGuide(req));
    }
}