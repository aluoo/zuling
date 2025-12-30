package com.anyi.miniapp.mobileStat;

import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.common.mobileStat.dto.CompanyStatDTO;
import com.anyi.common.mobileStat.response.RecycleStatVO;
import com.anyi.common.mobileStat.service.CompanyDataDailyBaseService;
import com.anyi.common.mobileStat.service.RecycleDataDailyBaseService;
import com.anyi.miniapp.interceptor.UserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "二手机统计模块")
@Validated
@RequestMapping("mobile/stat")
public class MobileStatController {
    @Autowired
    private CompanyDataDailyBaseService companyDataDailyBaseService;
    @Autowired
    private RecycleDataDailyBaseService recycleDataDailyBaseService;


    @PostMapping("/recycle")
    @WebLog(description = "回收商统计")
    @ApiOperation("回收商统计")
    public Response<RecycleStatVO> recycleStat(@RequestBody CompanyStatDTO req) {
        //没传默认看当前登陆人底下
        req.setEmployeeId(UserManager.getCurrentUser().getEmployeeId());
        RecycleStatVO resultVo = recycleDataDailyBaseService.recycleStat(req);
        return Response.ok(resultVo);
    }


}