package com.anyi.sparrow.assist.projects.controller;

import com.anyi.sparrow.assist.projects.service.ProjectsService;
import com.anyi.sparrow.assist.projects.vo.SysProjectsRps;
import com.anyi.common.domain.param.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


@Api(tags = "版本管理--V1")
@RestController
public class ProjectsV1Controller {


    @Autowired
    private ProjectsService projectsService;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation("查询升级信息")
    @GetMapping(value = "eapp/system/v1.0/getUpgradeInfo")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "projectCode", value = "项目code", dataType = "int", paramType = "query", required = true),
                    @ApiImplicitParam(name = "buildCode", value = "build号", dataType = "int", paramType = "query", required = true)
                    , @ApiImplicitParam(name = "device", value = "设备（1.Android/2.iOS）", dataType = "int", paramType = "query", required = true)})
    public Response<SysProjectsRps> getUpgradeInfo(@RequestParam int projectCode, @RequestParam int buildCode, @RequestParam int device) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
        response.setHeader("Access-Control-Allow-Origin", "*");
        //手机端查询比buildCode更高且已经启用的版本
        SysProjectsRps curPrg = projectsService.getCurPrg(projectCode, device);//查询正在启用的版本
        if (curPrg == null) {

            return new Response(200, "暂无在用最新版", null, new HashMap());
        }
        if (buildCode == curPrg.getBuildCode()) {

            return new Response(200, "当前版本是最新版", null, new HashMap());
        }
        if (buildCode > curPrg.getBuildCode()) {

            return new Response(200, "当前版本未开启", null, new HashMap());
        }
        //再次查询buildcode之间是否有强制更新版本
        SysProjectsRps sysProjects = projectsService.getPrgsForceUpdateByBuildCode(projectCode, buildCode, curPrg.getBuildCode());
        if (sysProjects != null) {
            curPrg.setForcedUpdating(1);
            return Response.ok(curPrg);
        } else {
            curPrg.setForcedUpdating(2);
            return Response.ok(curPrg);
        }

    }

}
