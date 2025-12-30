package com.anyi.common.exchange.service;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.anyi.common.exchange.domain.MbExchangeCustom;
import com.anyi.common.exchange.dto.ExchangeCustomDTO;
import com.anyi.common.exchange.dto.RtaReq;
import com.anyi.common.exchange.response.MbInstallVO;
import com.anyi.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author chenjian
 * @Description
 * @Date 2024/8/13
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class WyChannelService {
    public static final String CB_HOST_PROD = "https://mobile.anyichuxing.com/exchange/ocpx/wyCallback";
    public static final String CB_HOST_TEST = "https://mobile-test.anyichuxing.com/exchange/ocpx/wyCallback";
    public static final String OCPX_URL_PROD = "http://channel.m.163.com/cm/roi/na";
    public static final String OCPX_URL_TEST = "http://channel.m.163.com/cm/roi/na";

    private String buildCallbackUrl(RtaReq req) {
        return UrlBuilder.of(SpringContextUtil.isProduction() ? CB_HOST_PROD : CB_HOST_TEST).addPath(req.getOaid()).build();
    }

    private String buildOcpxUrl(RtaReq req, String cbUrl,MbInstallVO vo) {
        UrlBuilder ub = UrlBuilder.of(SpringContextUtil.isProduction() ? OCPX_URL_PROD : OCPX_URL_TEST).addPath(vo.getChannelNo());
        ub.setCharset(null);
        ub.addQuery("ip", req.getIp());
        ub.addQuery("imei", req.getImei());
        ub.addQuery("oaid", req.getOaid());
        ub.addQuery("callback", URLUtil.encodeAll(cbUrl));
        return ub.build();
    }

    public void invoke(RtaReq req,MbInstallVO vo) {
        log.info("[wangyi] start");
        String cbUrl = buildCallbackUrl(req);
        log.info("[wangyi] callback url - {}", cbUrl);
        String ocpxUrl = buildOcpxUrl(req, cbUrl, vo);
        log.info("[wangyi] req url - {}", ocpxUrl);
        HttpRequest request = HttpUtil.createRequest(Method.GET, ocpxUrl);

        try (HttpResponse response = request.execute()) {
            log.info("[wangyi] req is ok - {}", response.isOk());
            if (!response.isOk()) {
                log.error("[wangyi] err - {}", response.body());
            }
        }
    }
}