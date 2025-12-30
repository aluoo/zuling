package com.anyi.sparrow.exchange;

import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.common.exchange.service.OcpxNotifyService;
import com.anyi.common.exchange.service.WyNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/7/29
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/exchange")
public class OcpxController {
    @Autowired
    private OcpxNotifyService notifyService;
    @Autowired
    private WyNotifyService wyNotifyService;

    @WebLog(description = "ocpx回调")
    @RequestMapping(value = "/ocpx/callback/{excId}", method = RequestMethod.GET)
    public Response<?> callback(@PathVariable("excId") String excId, @RequestParam("action") String action) {
        log.info("[ocpx-callback] excId-{}, action-{}", excId, action);
        notifyService.addLog(excId, action);
        return Response.ok();
    }

    @WebLog(description = "网易新闻回调")
    @RequestMapping(value = "/ocpx/wyCallback/{excId}", method = RequestMethod.GET)
    public Response<?> wyCallback(@PathVariable("excId") String excId, @RequestParam("actionType") String actionType) {
        log.info("[ocpx-callback] excId-{}, action-{}", excId, actionType);
        wyNotifyService.addLog(excId, actionType);
        return Response.ok();
    }
}