package com.anyi.common.exchange.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.exchange.domain.ExchangeDeviceFileInfo;
import com.anyi.common.exchange.dto.ExchangeDeviceFileInfoDTO;
import com.anyi.common.exchange.dto.ExchangeDeviceFileInfoDownloadVO;
import com.anyi.common.exchange.mapper.ExchangeDeviceFileInfoMapper;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/9/5
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ExchangeDeviceFileInfoService extends ServiceImpl<ExchangeDeviceFileInfoMapper, ExchangeDeviceFileInfo> {
    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Transactional(rollbackFor = Exception.class)
    public ExchangeDeviceFileInfoDownloadVO upload(ExchangeDeviceFileInfoDTO req) {
        ExchangeDeviceFileInfoDownloadVO vo = ExchangeDeviceFileInfoDownloadVO.builder().files(req.getFiles()).build();
        /*if (req.getEmployeeId() == null) {
            throw new BusinessException(-1, "登录信息不能为空");
        }*/
        if (CollUtil.isEmpty(req.getFiles())) {
            throw new BusinessException(-1, "文件不能为空");
        }
        String orderNo = snowflakeIdService.nextId().toString();
        List<ExchangeDeviceFileInfo> infos = new ArrayList<>();
        for (int i = 0; i < req.getFiles().size(); i++) {
            infos.add(ExchangeDeviceFileInfo.builder()
                    // .employeeId(req.getEmployeeId())
                    .sort(i)
                    .orderNo(orderNo)
                    .url(req.getFiles().get(i))
                    .build());
        }
        if (CollUtil.isNotEmpty(infos)) {
            this.saveBatch(infos);
        }
        vo.setOrderNo(orderNo);
        return vo;
    }

    public ExchangeDeviceFileInfoDownloadVO download(ExchangeDeviceFileInfoDTO req) {
        ExchangeDeviceFileInfoDownloadVO vo = ExchangeDeviceFileInfoDownloadVO.builder().orderNo(req.getOrderNo()).build();
        /*if (req.getEmployeeId() == null) {
            throw new BusinessException(-1, "登录信息不能为空");
        }*/
        if (StrUtil.isBlank(req.getOrderNo())) {
            throw new BusinessException(-1, "单号不能为空");
        }
        List<ExchangeDeviceFileInfo> list = this.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                // .eq(ExchangeDeviceFileInfo::getEmployeeId, req.getEmployeeId())
                .eq(ExchangeDeviceFileInfo::getOrderNo, req.getOrderNo())
                .orderByAsc(ExchangeDeviceFileInfo::getSort)
                .list();
        if (CollUtil.isEmpty(list)) {
            return vo;
        }
        List<String> files = list.stream().map(ExchangeDeviceFileInfo::getUrl).collect(Collectors.toList());
        vo.setFiles(files);
        return vo;
    }
}