package com.anyi.sparrow.notice.controller;

import com.anyi.common.advice.BizError;
import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.notice.dto.NoticeUserMsgDTO;
import com.anyi.sparrow.notice.dto.UnReadMsgDTO;
import com.anyi.sparrow.notice.req.NoticeListReq;
import com.anyi.sparrow.notice.req.NoticeMsgIdReq;
import com.anyi.sparrow.notice.service.INoticeEmployeeMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 通知_员工消息 前端控制器
 * </p>
 *
 * @author shenbh
 * @since 2023-03-23
 */
@Slf4j
@Api(tags = "通知_员工消息")
@RestController
@RequestMapping("/eapp/v1.0/message")
public class NoticeEmployeeMsgController {

    @Autowired
    private INoticeEmployeeMsgService noticeEmployeeMsgService;

    @ApiOperation("最近一条未读消息接口")
    @PostMapping("getLastUnReadMsg")
    public Response<UnReadMsgDTO> getLastUnReadMsg() {

        Long employeeId = LoginUserContext.getUser().getId();
        UnReadMsgDTO dto = noticeEmployeeMsgService.findLastUnReadMsg(employeeId);
        return Response.ok(dto);
    }

    @ApiOperation("消息已读接口")
    @PostMapping("readMsg")
    public Response readMsg(@RequestBody NoticeMsgIdReq req) {

        if (req.getMsgId() == null) {
            return Response.failed(BizError.PARAM_ERROR);
        }

        Long employeeId = LoginUserContext.getUser().getId();
        noticeEmployeeMsgService.changeMsgRead(employeeId, req.getMsgId());
        return Response.ok();
    }

    @ApiOperation("首页重要公告弹窗")
    @GetMapping("getIndexSysNocite")
    public Response<List<NoticeUserMsgDTO>> getIndexSysNocite() {
        Long employeeId = LoginUserContext.getUser().getId();
        return Response.ok(noticeEmployeeMsgService.getIndexSysNocite(employeeId));
    }

    @ApiOperation("首页重要公告轮播")
    @GetMapping("getSysNociteSlide")
    public Response<List<NoticeUserMsgDTO>> getSysNociteSlide() {
        Long employeeId = LoginUserContext.getUser().getId();
        return Response.ok(noticeEmployeeMsgService.getSysNociteSlide(employeeId));
    }

    @ApiOperation("公告列表")
    @GetMapping("getList")
    public Response<List<NoticeUserMsgDTO>> getList(NoticeListReq req) {
        req.setEmployeeId(LoginUserContext.getUser().getId());
        return Response.ok(noticeEmployeeMsgService.getList(req));
    }

    @ApiOperation("公告详情")
    @PostMapping("detail")
    public Response<NoticeUserMsgDTO> noticeDetail(@RequestBody NoticeMsgIdReq req) {
        return Response.ok(noticeEmployeeMsgService.noticeDetail(req.getMsgId(), LoginUserContext.getUser().getId()));
    }

    @ApiOperation("最近未读系统公告消息数接口")
    @PostMapping("getLastSysUnReadMsg")
    public Response getLastSysUnReadMsg() {
        Long employeeId = LoginUserContext.getUser().getId();
        return Response.ok(noticeEmployeeMsgService.getLastSysUnReadMsg(employeeId));
    }

}
