package com.anyi.common.exchange.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.anyi.common.exchange.domain.MbInstall;
import com.anyi.common.exchange.mapper.MbInstallMapper;
import com.anyi.common.oss.FileUploader;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.util.MoneyUtil;
import com.anyi.common.util.SpringContextUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 拉新安装包 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Service
public class MbInstallService extends ServiceImpl<MbInstallMapper, MbInstall> {
    @Autowired
    SnowflakeIdService snowflakeIdService;
    @Autowired
    private FileUploader fileUploader;
    @Autowired
    private MbExchangeEmployeeService mbExchangeEmployeeService;

    private final static String TMPDIR = System.getProperty("java.io.tmpdir");

    public List<MbInstall> verifyList(Long employeeId){
        //获取员工换机包ID
        List<Long> phoneIds = mbExchangeEmployeeService.employeePhoneId(employeeId);


        return this.list(Wrappers.lambdaQuery(MbInstall.class).eq(MbInstall::getStatus,1)).stream().filter(e->StrUtil.isNotBlank(e.getVerifyUrl())).collect(Collectors.toList());
    }

    public String generateQrCode() {
        String baseUrl = "http://anyichuxing.com/dl/phonehelper";
        String fileName = snowflakeIdService.nextId() + ".jpeg";
        File imgFile = new File(TMPDIR + File.separator + fileName);
        QrCodeUtil.generate(baseUrl, 300, 300, imgFile);
        return fileUploader.upload(imgFile, fileName, "aycx-wechat");
    }



}
