package com.anyi.sparrow.exchange;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aliyun.dypnsapi20170525.models.GetMobileResponseBody;
import com.anyi.common.account.req.PartnerAccountLogReq;
import com.anyi.common.account.service.IEmployeeAccountLogService;
import com.anyi.common.account.vo.PartnerAccountLogVO;
import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.exchange.dto.*;
import com.anyi.common.exchange.response.MbExchangeQrcodeVO;
import com.anyi.common.exchange.response.MbInstallVO;
import com.anyi.common.exchange.response.MbPartnerOrderQueryReq;
import com.anyi.common.exchange.response.MbPartnerOrderQueryVO;
import com.anyi.common.exchange.service.*;
import com.anyi.common.oss.GetMobileService;
import com.anyi.common.req.AliYunGetMobileReq;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.organize.employee.service.ReviewStatusService;
import com.anyi.sparrow.organize.employee.vo.EmployeeLoginVO;
import com.anyi.sparrow.proto.RtaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "二手机拉新模块换机助手")
@Validated
@RequestMapping("/mobile/exchange")
public class MobileExchangeController {

    @Autowired
    private MbInstallService mbInstallService;
    @Autowired
    private MbExchangeEmployeeService mbExchangeEmployeeService;
    @Autowired
    private MbExchangePhoneService mbExchangePhoneService;
    @Autowired
    private MbExchangeCustomService mbExchangeCustomService;
    @Autowired
    private MbExchangeOrderService mbExchangeOrderService;
    @Autowired
    private GetMobileService getMobileService;
    @Autowired
    private RtaService rtaService;
    @Autowired
    private WyChannelService wyChannelService;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    private MbExchangeDeviceService deviceService;
    @Autowired
    private ExchangeDeviceFileInfoService exchangeDeviceFileInfoService;
    @Autowired
    private ReviewStatusService reviewStatusService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private IEmployeeAccountLogService employeeAccountLogService;



    @PostMapping("/intall/list")
    @WebLog(description = "安装包列表")
    @ApiOperation("安装包列表")
    public Response<List<MbInstallVO>> installList() {
        //获取员工安装包ID
        List<Long> phoneIds = mbExchangeEmployeeService.employeePhoneId(LoginUserContext.getUser().getId());
        if(CollUtil.isEmpty(phoneIds)){
            return Response.ok(new ArrayList<>());
        }
        return Response.ok(mbExchangePhoneService.getInstallList(phoneIds));
    }

    @PostMapping("/intall/newList")
    @WebLog(description = "安装包列表")
    @ApiOperation("安装包列表RTA版本")
    public Response<List<MbInstallVO>> installNewList(@RequestBody RtaReq req) {

        //获取员工安装包ID
        List<Long> phoneIds = mbExchangeEmployeeService.employeePhoneId(LoginUserContext.getUser().getId());
        if(CollUtil.isEmpty(phoneIds)){
            return Response.ok(new ArrayList<>());
        }

        List<MbInstallVO> resultList = mbExchangePhoneService.getInstallList(phoneIds);

        //安装包有抖音极速做RTA校验
        MbInstallVO rtaVo = resultList.stream().filter(e -> e.getName().equals("抖音极速")).findAny().orElse(null);
        if(ObjectUtil.isNotNull(rtaVo) && dictService.getRtaFuncAble()){
            req.setChannel(rtaVo.getChannelNo());
            req.setToken(rtaVo.getChannelToken());
            rtaService.postRta(req);
        }

        //安装包有网易新闻
        MbInstallVO wyVo = resultList.stream().filter(e -> e.getName().equals("网易新闻")).findAny().orElse(null);
        if(ObjectUtil.isNotNull(wyVo)){
            wyChannelService.invoke(req,wyVo);
        }

        return Response.ok(mbExchangePhoneService.getInstallList(phoneIds));
    }

    @PostMapping("/intall/device")
    @WebLog(description = "上传设备信息")
    @ApiOperation("上传设备信息")
    public Response<DeviceDTO> installDevice(@RequestBody DeviceDTO dto) {
        Boolean exchangeMode = dictService.getExchangeMode(LoginUserContext.getUser().getMobileNumber());
        DeviceDTO vo = deviceService.installDevice(dto);
        vo.setExchangeMode(exchangeMode);
        return Response.ok(vo);
    }

    @GetMapping("/exist/exchange")
    @WebLog(description = "是否换机模式")
    @ApiOperation("是否换机模式")
    public Response existExchange() {
        return Response.ok(mbExchangeOrderService.existExchange(LoginUserContext.getUser().getCompanyId()));
    }

    @GetMapping("/qrcode")
    @WebLog(description = "换机助手绑定订单号二维码")
    @ApiOperation("换机助手绑定订单号二维码")
    public Response<MbExchangeQrcodeVO> qrcode() {
        return Response.ok(mbExchangeOrderService.orderQrcode(LoginUserContext.getUser().getId()));
    }


    @PostMapping("/intall/down")
    @WebLog(description = "安装包下载记录")
    @ApiOperation("安装包下载记录")
    public Response down(@RequestBody ExchangeCustomDTO dto) {
        dto.setEmployeeId(LoginUserContext.getUser().getId());
        mbExchangeCustomService.installDown(dto);
        return Response.ok();
    }

    @ApiOperation("阿里云一键登录取号")
    @WebLog(description = "阿里云一键登录取号")
    @RequestMapping(value = "/auth/getMobile", method = RequestMethod.POST)
    public Response<GetMobileResponseBody> getMobile(@RequestBody AliYunGetMobileReq req) {
        return Response.ok(getMobileService.getMobile(req.getAccessToken()));
    }

    @WebLog(description = "外部-一键更新晒单提交")
    @ApiOperation("外部-一键更新晒单提交")
    @RequestMapping(value = "/oneKey/orderApply", method = RequestMethod.POST)
    public Response<?> oneKeyOrderApply(@RequestBody OnekeyApplyOrderDTO dto) {
        dto.setEmployeeId(LoginUserContext.getUser().getId());
        // 外部晒单
        dto.setSource(0);
        return Response.ok(mbExchangeOrderService.oneKeyOrderApply(dto));
    }

    @WebLog(description = "外部-换机晒单提交")
    @ApiOperation("外部-换机晒单提交")
    @RequestMapping("/exchange/orderApply")
    public Response<Long> exchangeOrderApply(@RequestBody ExchangeApplyOrderDTO dto) {
        dto.setEmployeeId(LoginUserContext.getUser().getId());
        // 外部晒单
        dto.setSource(0);
        return Response.ok(mbExchangeOrderService.exchangeOrderApply(dto));
    }

    @ApiOperation("外部-绿洲晒单提交")
    @WebLog(description = "外部-绿洲晒单提交")
    @RequestMapping(value = "/exchange/special/orderApply", method = RequestMethod.POST)
    public Response<Long> exchangeSpecialOrderApply(@RequestBody ExchangeApplyOrderDTO dto) {
        dto.setEmployeeId(LoginUserContext.getUser().getId());
        dto.setType(5);
        // 外部晒单
        dto.setSource(0);
        return Response.ok(mbExchangeOrderService.specialExchangeOrderApply(dto));
    }

    @ApiOperation("上传旧机设备文件信息")
    @WebLog(description = "上传旧机设备文件信息")
    @RequestMapping(value = "/upload/device/files/info", method = RequestMethod.POST)
    public Response<ExchangeDeviceFileInfoDownloadVO> uploadExchangeDeviceFilesInfo(@RequestBody ExchangeDeviceFileInfoUploadReq req) {
        // return Response.ok(exchangeDeviceFileInfoService.upload(ExchangeDeviceFileInfoDTO.builder().employeeId(LoginUserContext.getUserIdByCatch()).files(req.getFiles()).build()));
        return Response.ok(exchangeDeviceFileInfoService.upload(ExchangeDeviceFileInfoDTO.builder().files(req.getFiles()).build()));
    }

    @ApiOperation("获取旧机设备文件信息")
    @WebLog(description = "获取旧机设备文件信息")
    @RequestMapping(value = "/download/device/files/info", method = RequestMethod.POST)
    public Response<ExchangeDeviceFileInfoDownloadVO> downloadExchangeDeviceFilesInfo(@RequestBody ExchangeDeviceFileInfoDownloadReq req) {
        // return Response.ok(exchangeDeviceFileInfoService.download(ExchangeDeviceFileInfoDTO.builder().employeeId(LoginUserContext.getUserIdByCatch()).orderNo(req.getOrderNo()).build()));
        return Response.ok(exchangeDeviceFileInfoService.download(ExchangeDeviceFileInfoDTO.builder().orderNo(req.getOrderNo()).build()));
    }

    @ApiOperation("根据版本号获取换机助手审核状态")
    @WebLog(description = "根据版本号获取换机助手审核状态")
    @RequestMapping(value = "/review/status", method = RequestMethod.POST)
    public Response<?> getMobileExchangeReviewStatus(@RequestBody EmployeeLoginVO req) {
        // 默认隐藏登录 true保持隐藏，false放开登录
        return Response.ok(reviewStatusService.getMobileExchangeReviewStatus(req));
    }

    @ApiOperation("合作渠道-绿洲晒单提交")
    @WebLog(description = "合作渠道-绿洲晒单提交")
    @RepeatSubmit
    @PostMapping(value = "/partner/orderApply")
    public Response partnerApply(@RequestBody ExchangePartnerApplyDTO dto) {
        dto.setEmployeeId(LoginUserContext.getUser().getId());
        dto.setSource(0);
        mbExchangeOrderService.partnerApply(dto);
        return Response.ok();
    }


    @ApiOperation("合作渠道-资金明细")
    @WebLog(description = "合作渠道-资金明细")
    @RepeatSubmit
    @PostMapping(value = "/account/log")
    public Response<List<PartnerAccountLogVO>> listOrder(@RequestBody PartnerAccountLogReq req) {
        req.setEmployeeIds(employeeService.queryNormalChild(LoginUserContext.getUser().getId())
                .stream().map(Employee::getId).collect(Collectors.toList()));
        return Response.ok(employeeAccountLogService.partnerAccountLog(req));
    }

    @ApiOperation("合作渠道-查订单状态")
    @WebLog(description = "合作渠道-查订单状态")
    @RepeatSubmit
    @PostMapping(value = "/order/status")
    public Response<MbPartnerOrderQueryVO> orderStatus(@RequestBody @Validated MbPartnerOrderQueryReq req) {
        return Response.ok(mbExchangeOrderService.partnerOrderQuery(req));
    }

}