package com.anyi.common.exchange.service;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.anyi.common.exchange.domain.MbExchangeCustom;
import com.anyi.common.exchange.dto.ExchangeCustomDTO;
import com.anyi.common.util.SpringContextUtil;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/7/29
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class OcpxService {
    public static final String CB_HOST_PROD = "https://mobile.anyichuxing.com/exchange/ocpx/callback";
    public static final String CB_HOST_TEST = "https://mobile-test.anyichuxing.com/exchange/ocpx/callback";
    public static final String OCPX_URL_PROD = "http://ocpx.totiot.com/server/v1/evt";
    public static final String OCPX_URL_TEST = "http://ocpx.totiot.com/server/v1/evt";

    private String buildCallbackUrl(Long id) {
        return UrlBuilder.of(SpringContextUtil.isProduction() ? CB_HOST_PROD : CB_HOST_TEST).addPath(id.toString()).build();
    }

    private String buildOcpxUrl(ExchangeCustomDTO dto, String cbUrl) {
        UrlBuilder ub = UrlBuilder.of(SpringContextUtil.isProduction() ? OCPX_URL_PROD : OCPX_URL_TEST);
        ub.setCharset(null);
        ub.addQuery("os", "android");
        ub.addQuery("aid", dto.getOcpxReq().getAid());
        ub.addQuery("oaid", dto.getOcpxReq().getOaid());
        ub.addQuery("imei", dto.getOcpxReq().getImei());
        ub.addQuery("imei1", dto.getOcpxReq().getImei1());
        ub.addQuery("idfa", dto.getOcpxReq().getIdfa());
        ub.addQuery("ip", dto.getOcpxReq().getIp());
        ub.addQuery("ua", dto.getOcpxReq().getUa());
        ub.addQuery("cb", URLUtil.encodeAll(cbUrl));
        ub.addQuery("ts", System.currentTimeMillis());
        ub.addQuery("taskId", 0);
        return ub.build();
    }

    public void invoke(ExchangeCustomDTO dto, MbExchangeCustom exc) {
        log.info("[ocpx] start: excId-{}-{}-{}", exc.getId(), exc.getInstallName(), exc.getInstallChannelNo());
        if (dto.getOcpxReq() == null) {
            log.info("[ocpx] ocpxReq is null");
            return;
        }
        String cbUrl = buildCallbackUrl(exc.getId());
        log.info("[ocpx] callback url - {}", cbUrl);
        String ocpxUrl = buildOcpxUrl(dto, cbUrl);
        log.info("[ocpx] req url - {}", ocpxUrl);
        HttpRequest request = HttpUtil.createRequest(Method.GET, ocpxUrl);

        try (HttpResponse response = request.execute()) {
            log.info("[ocpx] req is ok - {}", response.isOk());
            if (!response.isOk()) {
                log.error("[ocpx] err - {}", response.body());
            }
        }
    }
}