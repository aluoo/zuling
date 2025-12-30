package com.anyi.sparrow.applet.user.service;

import com.alibaba.fastjson.JSONObject;
import com.anyi.sparrow.base.security.Constants;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.TokenBuilder;
import com.anyi.sparrow.common.enums.UserType;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anyi.common.user.domain.UserAccount;
import com.anyi.common.employee.domain.UserLogin;
import com.anyi.common.employee.mapper.UserLoginMapper;
import com.anyi.sparrow.applet.user.vo.UserTokenVO;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author peng can
 * @date 2022/12/4 10:33
 */
@Service
public class UserLoginService extends ServiceImpl<UserLoginMapper, UserLogin> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 用户 token 过期时间 秒
     */
    private Long tokenExpireSeconds = 15 * 24L * 60L * 60L;

    /**
     * 用户登陆生成/刷新 token
     *
     * @param userAccount 用户信息
     * @return 用户token
     */
    public UserTokenVO userLogin(UserAccount userAccount) {
        UserLogin userLogin = this.getById(userAccount.getId());
        if (Objects.isNull(userLogin)) {
            userLogin = new UserLogin();
            userLogin.setUserId(userAccount.getId());
        }
        if (StringUtils.isNotBlank(userLogin.getToken())) {
            this.removeCacheToken(userLogin.getToken());
        }
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(tokenExpireSeconds);
        String token = TokenBuilder.build(UserType.USER.getCode());
        userLogin.setToken(token);
        userLogin.setTokenExpire(expireTime);
        userLogin.setLoginTime(LocalDateTime.now());
        this.saveOrUpdate(userLogin);
        this.cacheToken(token, userAccount);

        return new UserTokenVO(token, tokenExpireSeconds);
    }

    /**
     * 删除token缓存
     *
     * @param token
     */
    private void removeCacheToken(String token) {
        redisTemplate.delete(Constants.TOKEN_PREFIX + UserType.USER.getCode() + ":" + token);
    }

    /**
     * 缓存token
     *
     * @param token
     * @param userAccount
     */
    private void cacheToken(String token, UserAccount userAccount) {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(userAccount.getId());
        loginUser.setMobileNumber(userAccount.getMobileNumber());
        loginUser.setName(userAccount.getNickname());
        loginUser.setNickname(userAccount.getNickname());
        loginUser.setOpenId(userAccount.getOpenId());
        String redisKey = this.getRedisKey(token);
        redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(loginUser), tokenExpireSeconds, TimeUnit.SECONDS);
    }

    public void refreshUser(UserAccount userAccount) {
        UserLogin userLogin = this.getById(userAccount.getId());
        if (Objects.isNull(userLogin)) {
            log.error("用户登陆信息不存在");
            return;
        }
        String token = userLogin.getToken();
        this.refreshValue(token, userAccount);
    }

    /**
     * 缓存token
     *
     * @param token
     * @param userAccount
     */
    private void refreshValue(String token, UserAccount userAccount) {
        String redisKey = this.getRedisKey(token);
        Long expire = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
        if (Objects.isNull(expire) || expire < 0) {
            expire = tokenExpireSeconds;
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setId(userAccount.getId());
        loginUser.setMobileNumber(userAccount.getMobileNumber());
        loginUser.setName(userAccount.getNickname());
        loginUser.setNickname(userAccount.getNickname());
        loginUser.setOpenId(userAccount.getOpenId());
        loginUser.setAvatarUrl(userAccount.getAvatarUrl());
        loginUser.setUnionId(userAccount.getUnionId());

        redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(loginUser), expire, TimeUnit.SECONDS);
    }

    @NotNull
    private String getRedisKey(String token) {
        return Constants.TOKEN_PREFIX + UserType.USER.getCode() + ":" + token;
    }

    public UserLogin getByToken(String token) {
        return this.lambdaQuery().eq(UserLogin::getToken, token).one();
    }
}
