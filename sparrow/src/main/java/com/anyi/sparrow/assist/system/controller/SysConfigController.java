package com.anyi.sparrow.assist.system.controller;

import com.anyi.sparrow.common.Constants;
import com.anyi.sparrow.common.vo.Dicts;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.common.domain.param.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(description = "系统配置")
//@RestController
//@RequestMapping(path = {"em/v1.0/sys-config", "cm/v1.0/sys-config"})
@Deprecated
public class SysConfigController {
    @Autowired
    private SysDictService dictService;

    @ApiOperation("获取系统配置参数")
    @ResponseBody
    @GetMapping("get-config")
    public Response<List<Dicts>> getSysConfig(){
        return Response.ok(dictService.getByTypeWithCache(Constants.dict_sys_config_type));
    }
}
