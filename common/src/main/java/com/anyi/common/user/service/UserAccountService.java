package com.anyi.common.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anyi.common.user.domain.UserAccount;
import com.anyi.common.user.mapper.UserAccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Objects;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class UserAccountService extends ServiceImpl<UserAccountMapper, UserAccount> {

    public UserAccount selectByMpOpenId(String mpOpenId) {
        return this.lambdaQuery().eq(UserAccount::getOfficialOpenId, mpOpenId).one();
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveUserMpOpenId(String openId, String unionID) {
        UserAccount userAccount = this.selectByUnionID(unionID);
        if (Objects.isNull(userAccount)) {
            userAccount = new UserAccount();
            userAccount.setUnionId(unionID);
            userAccount.setCreateTime(new Date());
        }
        userAccount.setOfficialOpenId(openId);
        userAccount.setUpdateTime(new Date());
        this.saveOrUpdate(userAccount);
    }

    public UserAccount selectByUnionID(String unionID) {
        return this.lambdaQuery().eq(UserAccount::getUnionId, unionID).one();
    }

    public UserAccount selectByMobile(String mobile) {
        return this.lambdaQuery().eq(UserAccount::getMobileNumber, mobile).one();
    }


}