package com.anyi.common.exchange.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.exchange.domain.MbExchangeCustom;
import com.anyi.common.exchange.domain.MbExchangeDevice;
import com.anyi.common.exchange.domain.MbInstall;
import com.anyi.common.exchange.dto.ExchangeCustomDTO;
import com.anyi.common.exchange.mapper.MbExchangeCustomMapper;
import com.anyi.common.exchange.mapper.MbExchangeOrderMapper;
import com.anyi.common.service.CommonSysDictService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户安装换机包 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Service
@Slf4j
public class MbExchangeCustomService extends ServiceImpl<MbExchangeCustomMapper, MbExchangeCustom> {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MbInstallService mbInstallService;
    @Autowired
    private MbExchangeOrderMapper mbExchangeOrderMapper;
    @Autowired
    private MbExchangeDeviceService mbExchangeDeviceService;
    @Autowired
    OcpxService ocpxService;
    @Autowired
    private CommonSysDictService dictService;

    @Transactional(rollbackFor = Exception.class)
    public void installDown(ExchangeCustomDTO dto){
        log.info("上传安装包记录{}",dto.getOrderSn());
        Employee employee = employeeService.getById(dto.getEmployeeId());
        if(ObjectUtil.isNull(employee)){
            throw new BusinessException(99999,"员工不存在");
        }
        if(CollUtil.isEmpty(dto.getInstallList())){
            throw new BusinessException(99999,"安装包下载记录上传失败");
        }

        List<MbExchangeCustom> customList = new ArrayList<>();

        //单号对应的安装包
        List<MbExchangeCustom> installList = this.list(Wrappers.lambdaQuery(MbExchangeCustom.class)
                .eq(MbExchangeCustom::getOrderSn,dto.getOrderSn()));

        List<Long> installIds = installList.stream().map(MbExchangeCustom::getInstallId).collect(Collectors.toList());

        for(ExchangeCustomDTO.InstallDTO installDTO:dto.getInstallList()){
            //包已经安装过就跳过
            if(CollUtil.isNotEmpty(installIds) && installIds.contains(installDTO.getInstallId())){
                continue;
            }
            MbInstall mbInstall = mbInstallService.getById(installDTO.getInstallId());
            MbExchangeCustom custom = new MbExchangeCustom();
            custom.setStoreEmployeeId(employee.getId());
            custom.setStoreCompanyId(employee.getCompanyId());
            custom.setEmployeePhone(employee.getMobileNumber());
            custom.setCustomPhone(dto.getCustomPhone());
            custom.setOrderSn(dto.getOrderSn());
            custom.setExchangePhoneId(dto.getExchangePhoneId());
            custom.setMarketFlag(dto.getMarketFlag());
            // custom.setOaid(dto.getOcpxReq().getOaid());
            custom.setOaid(Optional.ofNullable(dto.getOcpxReq()).map(ExchangeCustomDTO.OcpxReq::getOaid).orElse(null));
            custom.setInstallId(mbInstall.getId());
            custom.setInstallName(mbInstall.getName());
            custom.setInstallChannelNo(mbInstall.getChannelNo());
            custom.setOpenTime(installDTO.getOpenTime());
            customList.add(custom);
        }
        this.saveBatch(customList);

        //安装设备信息更新客户手机号
        List<MbExchangeDevice> deviceList = new ArrayList<>();
        if (dto.getOcpxReq() != null && StrUtil.isNotBlank(dto.getOcpxReq().getOaid())) {
            deviceList = mbExchangeDeviceService.list(Wrappers.lambdaQuery(MbExchangeDevice.class)
                    .eq(MbExchangeDevice::getOaid,dto.getOcpxReq().getOaid()));
        }

        if(CollUtil.isNotEmpty(deviceList)){
            MbExchangeDevice mbExchangeDevice = deviceList.get(0);
            BeanUtil.copyProperties(dto,mbExchangeDevice);
            mbExchangeDevice.setOpenTime(dto.getOpenTime()/1000);
            mbExchangeDeviceService.updateById(mbExchangeDevice);
        }

        if (dictService.getRtaFuncAble()) {
            for (MbExchangeCustom exc : customList) {
                ocpxService.invoke(dto, exc);
            }
        }

    }
}