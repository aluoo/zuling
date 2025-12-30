package com.anyi.sparrow.product.controller;

import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.common.product.domain.response.StoreCenterIndexVO;
import com.anyi.sparrow.product.service.StoreCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "门店个人中心管理")
@RestController
@RequestMapping("/mobile/store/center")
public class StoreCenterController {
    @Autowired
    private StoreCenterService service;

    @ApiOperation("个人中心首页")
    @WebLog(description = "门店个人中心首页")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public Response<StoreCenterIndexVO> index() {
        return Response.ok(service.index());
    }
}