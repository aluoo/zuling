package com.anyi.miniapp.controller;


import com.anyi.common.result.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * health 前端控制器
 * </p>
 *
 * @author Jianpan
 * @since 2022-12-06
 */
@RestController
@Api(tags = "01健康检查（忽略）")
public class HealthController {


    @ApiOperation("health")
    @GetMapping("/health-check")
    public ResponseDTO<String> ok() {

        return ResponseDTO.success("OK");
    }


}
