package com.anyi.sparrow.assist.system.controller;

import com.anyi.common.domain.param.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @ApiOperation("健康检查")
    @GetMapping("/health-check")
    public Response healthCheck() {
        return Response.ok("Hello, Sparrow!");
    }
}
