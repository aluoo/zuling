package com.anyi.sparrow.exchange;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmType;
import com.anyi.common.employee.mapper.EmployeeMapper;
import com.anyi.common.exchange.domain.MbExchangeOrder;
import com.anyi.common.exchange.domain.MbExchangeVerifyEmployee;
import com.anyi.common.exchange.domain.MbVerifyInstall;
import com.anyi.common.exchange.dto.DecodeQrCodeReq;
import com.anyi.common.exchange.dto.ExchangeApplyOrderDTO;
import com.anyi.common.exchange.dto.ExchangePhoneVerifyDTO;
import com.anyi.common.exchange.dto.OnekeyApplyOrderDTO;
import com.anyi.common.exchange.service.*;
import com.anyi.common.product.domain.request.ExchangeOrderListReq;
import com.anyi.common.product.domain.response.ExchangeOrderVO;
import com.anyi.common.result.DictMapVO;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.DateUtils;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import com.anyi.sparrow.common.vo.Dicts;
import com.anyi.sparrow.exchange.req.KuaiShouReq;
import com.anyi.sparrow.exchange.service.ExchangeOrderService;
import com.anyi.sparrow.exchange.service.KsReportService;
import com.anyi.sparrow.exchange.service.QrCodeService;
import com.anyi.sparrow.organize.employee.dao.mapper.EmployeeHistoryMapper;
import com.anyi.sparrow.organize.employee.dao.mapper.EmployeeLoginMapper;
import com.anyi.sparrow.organize.employee.domain.EmployeeHistory;
import com.anyi.sparrow.organize.employee.domain.EmployeeLogin;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "二手机拉新模块卖家端")
@Validated
@RequestMapping("/exchange")
public class ExchangeController {

    @Autowired
    private MbInstallService mbInstallService;
    @Autowired
    private ExchangeOrderService exchangeOrderService;
    @Autowired
    private MbExchangeOrderService mbExchangeOrderService;
    @Autowired
    private MbExchangeEmployeeService mbExchangeEmployeeService;
    @Autowired
    private MbExchangePhoneService mbExchangePhoneService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private MbExchangeVerifyEmployeeService verifyEmployeeService;
    @Autowired
    private MbVerifyInstallService verifyInstallService;
    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private CommonSysDictService commonSysDictService;
    @Autowired
    private EmployeeLoginMapper employeeLoginMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private KsReportService ksReportService;


    @PostMapping("/verify/list")
    @WebLog(description = "验新码列表")
    @ApiOperation("验新码列表")
    public Response<List<MbVerifyInstall>> verifyList() {
        List<MbVerifyInstall> resultVo = new ArrayList<>();
        List<MbExchangeVerifyEmployee> resultList = verifyEmployeeService.companyVerify(LoginUserContext.getUser().getId());
        if(CollUtil.isEmpty(resultList)) return Response.ok(resultVo);

        List<Long> ids = resultList.stream().map(MbExchangeVerifyEmployee::getExchangeVerifyId).collect(Collectors.toList());

        return Response.ok(verifyInstallService.getByIds(ids));
    }

    @GetMapping("/example/list")
    @WebLog(description = "晒单图片样例图")
    @ApiOperation("晒单图片样例图")
    public Response<List<Dicts>> exampleList(String dictType,Boolean reStatus) {
        return Response.ok(sysDictService.getByType(dictType));
    }

    @PostMapping("/down/url")
    @WebLog(description = "换机助手下载地址")
    @ApiOperation("换机助手下载地址")
    public Response downUrl() {
        return Response.ok(mbInstallService.generateQrCode());
    }


    @ApiOperation("换机晒单订单列表")
    @WebLog(description = "换机晒单订单列表")
    @RequestMapping(value = "/order/list", method = RequestMethod.POST)
    public Response<List<ExchangeOrderVO>> listOrder(@RequestBody ExchangeOrderListReq req) {
        //非店长只能看自己
        if(!Arrays.asList(EmType.MANGER_MANGER.getCode(),EmType.CM_MANGER.getCode()).contains(LoginUserContext.getUser().getType()) && req.getStoreEmployeeId()==null){
            req.setStoreEmployeeId(LoginUserContext.getUser().getId());
        }
        return Response.ok(exchangeOrderService.listOrder(req));
    }

    @GetMapping("/exist/exchange")
    @WebLog(description = "是否换机模式")
    @ApiOperation("是否换机模式")
    public Response existExchange() {
        return Response.ok(mbExchangeOrderService.existExchange(LoginUserContext.getUser().getCompanyId()));
    }

    @PostMapping("/oneKey/orderApply")
    @WebLog(description = "一键更新晒单提交")
    @ApiOperation("一键更新晒单提交")
    @RepeatSubmit
    public Response oneKeyOrderApply(@RequestBody OnekeyApplyOrderDTO dto) {
        dto.setEmployeeId(LoginUserContext.getUser().getId());
        mbExchangeOrderService.oneKeyOrderApply(dto);
        return Response.ok();
    }

    @PostMapping("/exchange/orderApply")
    @WebLog(description = "换机晒单提交")
    @RepeatSubmit
    @ApiOperation("换机晒单提交")
    public Response ExchangeOrderApply(@RequestBody ExchangeApplyOrderDTO dto) {
        dto.setEmployeeId(LoginUserContext.getUser().getId());
        mbExchangeOrderService.exchangeOrderApply(dto);
        return Response.ok();
    }

    @PostMapping("/exchange/special/orderApply")
    @WebLog(description = "绿洲晒单提交")
    @ApiOperation("绿洲晒单提交")
    @RepeatSubmit
    public Response ExchangeSpecialOrderApply(@RequestBody ExchangeApplyOrderDTO dto) {
        dto.setEmployeeId(LoginUserContext.getUser().getId());
        mbExchangeOrderService.specialExchangeOrderApply(dto);
        return Response.ok();
    }

    @ApiOperation("识别二维码")
    @WebLog(description = "识别二维码")
    @RequestMapping(value = "/exchange/decode/qrcode", method = RequestMethod.POST)
    public Response<String> decodeQrCodeImg(@RequestBody DecodeQrCodeReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(qrCodeService.decode(req.getUrl()));
    }

    @ApiOperation("快手信息上报")
    @WebLog(description = "快手信息上报")
    @RequestMapping(value = "/exchange/ks/report", method = RequestMethod.POST)
    public Response ksReport(@RequestBody KuaiShouReq req) {

        Employee employee = employeeMapper.selectOne(
                Wrappers.lambdaQuery(Employee.class)
                        .eq(Employee::getMobileNumber,"13795475730")
                        .eq(Employee::getStatus,1)
                        .last("limit 1"));

        if(ObjectUtil.isNull(employee)){
            return Response.ok();
        }

        if(!LoginUserContext.getUser().getAncestors().contains(employee.getAncestors())){
            return Response.ok();
        }

        EmployeeLogin employeeLogin = employeeLoginMapper.selectOne(Wrappers.lambdaQuery(EmployeeLogin.class)
                .eq(EmployeeLogin::getUserId,LoginUserContext.getUser().getId())
                .eq(EmployeeLogin::getOs,"iOS"));

        if(ObjectUtil.isNull(employeeLogin)){
            return Response.ok();
        }

        Long installTime = DateUtils.adjustMinute(employeeLogin.getLoginTime(),-2).getTime();
        req.setTimestamp(installTime.toString());

        ksReportService.report(req);
        return Response.ok();
    }

    @ApiOperation("晒单审核原因")
    @WebLog(description = "晒单审核原因")
    @ResponseBody
    @GetMapping("/order/reason")
    public Response<DictMapVO> getReason(Long id) {
        return Response.ok(commonSysDictService.getNameMap("phone_order_reason"));
    }

    @ApiOperation("晒单审核")
    @WebLog(description = "晒单审核")
    @ResponseBody
    @PostMapping("/order/check")
    public Response check(@RequestBody ExchangePhoneVerifyDTO dto) {
        mbExchangeOrderService.check(dto);
        return Response.ok();
    }

}