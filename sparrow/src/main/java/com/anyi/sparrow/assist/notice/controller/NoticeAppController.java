package com.anyi.sparrow.assist.notice.controller;

import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.assist.notice.service.NoticeService;
import com.anyi.sparrow.assist.notice.vo.NoticeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/app")
@Api(description = "公告信息")
public class NoticeAppController {



    @Autowired
    private NoticeService noticeService;


    @ApiOperation("获取首页公告信息")
    @ResponseBody
    @RequestMapping(value = "v1.0/notice/home-notice/list", method = RequestMethod.GET)
    public Response<List<NoticeVo>> homeNotice() {

        List<NoticeVo> lsNoticeNo = noticeService.homDataList();
        return Response.ok(lsNoticeNo);
    }


}
