package com.anyi.common.service;

import com.anyi.common.exchange.domain.MbExchangeCustom;
import com.anyi.common.exchange.dto.ExchangeCustomDTO;
import com.anyi.common.exchange.dto.RtaReq;
import com.anyi.common.exchange.service.OcpxService;
import com.anyi.sparrow.SparrowApplicationTest;
import com.anyi.sparrow.proto.RtaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/7/29
 * @Copyright
 * @Version 1.0
 */
@Slf4j
public class OcpxServiceTest extends SparrowApplicationTest {
    @Autowired
    private OcpxService service;
    @Autowired
    private RtaService rtaService;

    @Ignore
    @Test
    public void test() {
        ExchangeCustomDTO dto = ExchangeCustomDTO.builder().build();
        ExchangeCustomDTO.OcpxReq ocpxReq = ExchangeCustomDTO.OcpxReq.builder()
                .ip("127.0.0.1")
                .os("android")
                // .aid("aid")
                .build();
        dto.setOcpxReq(ocpxReq);
        MbExchangeCustom exc = new MbExchangeCustom();
        exc.setId(1234L);
        exc.setInstallName("测试包");
        exc.setInstallChannelNo("ox21");
        service.invoke(dto, exc);
    }

    @Test
    public void rtaTest() {
        RtaReq req = new RtaReq();
        req.setChannel("test");
        req.setImei("444b1eea57fa0f5b");
        req.setToken("test");
        req.setOaid("504703d4-da50-4853-823f-2c9e64c80501");
        Boolean flag = rtaService.postRta(req);
        String aa = "123123123";

    }
}