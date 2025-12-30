package com.anyi.common.exchange.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.anyi.common.exchange.domain.MbExchangeDevice;
import com.anyi.common.exchange.dto.DeviceDTO;
import com.anyi.common.exchange.mapper.MbExchangeDeviceMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 晒单客户机基础信息 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Service
public class MbExchangeDeviceService extends ServiceImpl<MbExchangeDeviceMapper, MbExchangeDevice>  {

    public DeviceDTO installDevice(@RequestBody DeviceDTO dto){

        List<MbExchangeDevice> deviceList = this.lambdaQuery()
                .eq(MbExchangeDevice::getOaid,dto.getOaid()).list();

        if(CollUtil.isNotEmpty(deviceList)){
            DeviceDTO deviceDTO = new DeviceDTO();
            deviceDTO.setOaid(deviceList.get(0).getOaid());
            deviceDTO.setModel(deviceList.get(0).getModel());
            deviceDTO.setSysVersion(deviceList.get(0).getSysVersion());
            deviceDTO.setInstallDate(deviceList.get(0).getInstallDate());
            deviceDTO.setBrand(deviceList.get(0).getBrand());
            deviceDTO.setAndroidVersion(deviceList.get(0).getAndroidVersion());
            return deviceDTO;
        }

        MbExchangeDevice device = new MbExchangeDevice();
        BeanUtil.copyProperties(dto,device);
        this.save(device);
        return dto;
    }
}
