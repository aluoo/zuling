package com.anyi.sparrow.organize.employee.controller;

import com.anyi.sparrow.organize.employee.vo.EmpCfgVo;
import com.anyi.sparrow.organize.employee.service.EmpCfgService;
import com.anyi.common.domain.param.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("em/cfg")
public class EmpCfgController {
    @Autowired
    private EmpCfgService empCfgService;

    @PostMapping("v127/emp-cfg")
    @ApiOperation("插入员工配置")
    public Response<EmpCfgVo> insert(@RequestBody @Valid EmpCfgVo empCfgVo){
        return Response.ok(empCfgService.inert(empCfgVo));
    }
    @PostMapping("v127/update-cfg")
    @ApiOperation("更新员工编码")
    public Response<EmpCfgVo> update(@RequestBody@Valid EmpCfgVo vo){
        return Response.ok(empCfgService.update(vo));
    }


    @GetMapping("v127/query")
    @ApiOperation("查询员工配置")
    public Response<EmpCfgVo> query(@RequestParam@ApiParam("1 齐鲁工行 2齐鲁交通 3 江苏交行") Integer biz){
        return Response.ok(empCfgService.query(biz));
    }
}
