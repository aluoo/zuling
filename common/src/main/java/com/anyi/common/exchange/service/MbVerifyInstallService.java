package com.anyi.common.exchange.service;

import com.anyi.common.exchange.domain.MbVerifyInstall;
import com.anyi.common.exchange.mapper.MbVerifyInstallMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 拉新安装包 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Service
public class MbVerifyInstallService extends ServiceImpl<MbVerifyInstallMapper, MbVerifyInstall> {

    public List<MbVerifyInstall> getByIds(List<Long> ids){
        return this.lambdaQuery().eq(MbVerifyInstall::getStatus,1).in(MbVerifyInstall::getId,ids).list();
    }
}
