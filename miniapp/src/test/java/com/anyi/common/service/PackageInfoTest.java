package com.anyi.common.service;

import com.anyi.common.company.dto.PackageInfoDTO;
import com.anyi.common.company.service.PackageInfoService;
import com.anyi.miniapp.MiniApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
public class PackageInfoTest extends MiniApplicationTest {
    @Autowired
    private PackageInfoService service;

    @Test
    public void test() {
        PackageInfoDTO req = PackageInfoDTO.builder()
                .bizTypeId(3L)
                .price(3920)
                .companyId(1762321196646789122L)
                .build();

        System.out.println(service.getBaseMapper().getPackageInfoByPriceInterval(req).toString());
    }
}