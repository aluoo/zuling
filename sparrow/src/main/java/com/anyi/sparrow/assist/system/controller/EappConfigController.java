package com.anyi.sparrow.assist.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.common.company.domain.Company;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.organize.employee.service.CompanyProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统配置-新")
@RestController
@RequestMapping(path = {"eapp/v1.0/config"})
@Slf4j
public class EappConfigController {
    @Autowired
    private SysDictService dictService;
    @Autowired
    private CompanyProcessService companyProcessService;

    @ApiOperation("获取系统配置参数")
    @ResponseBody
    @PostMapping("getAppConf")
    public Response<JSONObject> getAppConf() {

        JSONObject jsonObject = new JSONObject();

        String ossEndpoint = dictService.getByNameWithCache("ossEndpoint");
        String ossAccessKey = dictService.getByNameWithCache("ossAccessKey");
        String osSscrectKey = dictService.getByNameWithCache("osSscrectKey");
        String ocrAppCode = dictService.getByNameWithCache("ocrAppCode");
        String ocrUrlIdCard = dictService.getByNameWithCache("ocrUrlIdCard");
        String ocrUrlVehicle = dictService.getByNameWithCache("ocrUrlVehicle");
        // 物料单台押金
//        String materialDeposit = dictService.getByNameWithCache("materialDeposit");

        jsonObject.put("kefuPhone", getKefuPhone());
        jsonObject.put("materialDeposit", getMaterialDeposit());
        jsonObject.put("ossEndpoint", ossEndpoint);
        jsonObject.put("ossAccessKey", ossAccessKey);
        jsonObject.put("osSscrectKey", osSscrectKey);
        jsonObject.put("ocrAppCode", ocrAppCode);
        jsonObject.put("ocrUrlIdCard", ocrUrlIdCard);
        jsonObject.put("ocrUrlVehicle", ocrUrlVehicle);
        return Response.ok(jsonObject);
    }

    /**
     * 物料单台押金
     *
     * @return
     */
    private Integer getMaterialDeposit() {

        if (LoginUserContext.getUser().getCompanyId() != null) {
            Company company = companyProcessService.getById(LoginUserContext.getUser().getCompanyId());
        }
        String materialDeposit = dictService.getByNameWithCache("materialDeposit");
        Integer materialDepositInt = 100;
        try {
            return StringUtils.isEmpty(materialDeposit) ? materialDepositInt : Integer.parseInt(materialDeposit);
        } catch (NumberFormatException e) {
            log.error("获取materialDeposit配置解析失败{}", materialDeposit);
        }
        return materialDepositInt;
    }

    /**
     * 客服电话
     *
     * @return
     */
    private String getKefuPhone() {
        String dictPhone = dictService.getByName("KEFU_PHONE");
        String defaultPhone = "4006686333";
        return StringUtils.isBlank(dictPhone) ? defaultPhone : dictPhone;
    }
}
