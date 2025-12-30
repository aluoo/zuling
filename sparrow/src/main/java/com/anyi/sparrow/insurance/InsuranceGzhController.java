package com.anyi.sparrow.insurance;

import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.common.insurance.domain.DiOption;
import com.anyi.common.insurance.req.*;
import com.anyi.common.insurance.response.DiFixInsuranceDetailVO;
import com.anyi.common.insurance.response.DiInsuranceFixOrderVO;
import com.anyi.common.insurance.response.GzhInsuranceOrderVO;
import com.anyi.common.insurance.service.DiCompanyAccountService;
import com.anyi.common.insurance.service.DiInsuranceOptionService;
import com.anyi.common.insurance.service.DiOptionService;
import com.anyi.common.result.DictMapVO;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import com.anyi.sparrow.insurance.service.InsuranceGzhManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@Api(tags = "碎屏保-公众号模块")
@RequestMapping("/insurance/gzh")
public class InsuranceGzhController {

    @Autowired
    DiCompanyAccountService companyAccountService;
    @Autowired
    InsuranceGzhManageService insuranceGzhManageService;
    @Autowired
    CommonSysDictService commonSysDictService;
    @Autowired
    DiInsuranceOptionService insuranceOptionService;
    @Autowired
    DiOptionService diOptionService;


    @ApiOperation("修改密码")
    @WebLog(description = "修改密码")
    @PostMapping(value = "/passWord")
    public Response passWord(@RequestBody GzhPassWordReq req) {
        ValidatorUtil.validateBean(req);
        insuranceGzhManageService.passWord(req);
        return Response.ok();
    }

    @ApiOperation("产品报修选项")
    @WebLog(description = "产品报修选项")
    @GetMapping(value = "/option/{insuranceId}")
    public Response option(@PathVariable Long insuranceId) {
        return Response.ok(Collections.emptyList());
    }

    @ApiOperation("投保保单查询")
    @WebLog(description = "投保保单查询")
    @PostMapping(value = "/queryOrder")
    public Response<List<GzhInsuranceOrderVO>> orderQuery(@RequestBody GzhOrderQueryReq req) {
        return Response.ok(insuranceGzhManageService.orderList(req));
    }

    @ApiOperation("我的投保订单")
    @WebLog(description = "我的投保订单")
    @PostMapping(value = "/list")
    public Response<List<GzhInsuranceOrderVO>> list() {
        GzhOrderQueryReq req = new GzhOrderQueryReq();
        req.setMobile(LoginUserContext.getUser().getMobileNumber());
        return Response.ok(insuranceGzhManageService.orderList(req));
    }

    @GetMapping(value = "/service/type")
    @ApiOperation("服务类型")
    @WebLog(description = "服务类型")
    public Response<List<DiOption>> serviceType(Long insuranceId)
    {
        return Response.ok(insuranceOptionService.getOptionByProductId(insuranceId,"FIXSERVICE"));
    }

    @ApiOperation("列表串号di_fix_read_imei_item,列表故障di_fix_sjgz_item")
    @WebLog(description = "串号和手机故障列表")
    @GetMapping("/fix/item")
    public Response<DictMapVO> fixItem(String name) {
        return Response.ok(commonSysDictService.getNameMap(name));
    }



    @ApiOperation("投保订单详情")
    @WebLog(description = "投保订单详情")
    @PostMapping(value = "/insurance/order")
    public Response<DiFixInsuranceDetailVO> insuranceOrder(@RequestBody InsuranceOrderDetailReq req) {
        return Response.ok(insuranceGzhManageService.insuranceOrderDetail(req));
    }



    @ApiOperation("报险单提交")
    @WebLog(description = "报险单提交")
    @PostMapping(value = "/saveFix")
    public Response saveFix(@RequestBody InsuranceFixOrderDTO req) {
        ValidatorUtil.validateBean(req);
        insuranceGzhManageService.saveFix(req);
        return Response.ok();
    }

    @ApiOperation("报险订单列表")
    @WebLog(description = "报险订单列表")
    @PostMapping(value = "/fix/list")
    public Response<List<GzhInsuranceOrderVO>> fixList() {
        GzhOrderQueryReq req = new GzhOrderQueryReq();
        req.setMobile(LoginUserContext.getUser().getMobileNumber());
        return Response.ok(insuranceGzhManageService.fixList(req));
    }

    @ApiOperation("报险订单修改详情")
    @WebLog(description = "报险订单修改详情")
    @PostMapping(value = "/fexDetail/{id}")
    public Response<DiInsuranceFixOrderVO> fixDetail(@PathVariable Long id) {
        return Response.ok(insuranceGzhManageService.fixDetail(id));
    }

    @ApiOperation("报险订单修改")
    @WebLog(description = "报险订单修改")
    @PostMapping(value = "/updateFix")
    public Response updateFix(@RequestBody InsuranceFixOrderDTO req) {
        insuranceGzhManageService.updateFix(req);
        return Response.ok();
    }

    @ApiOperation("理赔资料上传")
    @WebLog(description = "理赔资料上传")
    @PostMapping(value = "/saveData")
    public Response saveData(@RequestBody InsuranceFixSettleDTO req) {
        insuranceGzhManageService.saveData(req);
        return Response.ok();
    }







}