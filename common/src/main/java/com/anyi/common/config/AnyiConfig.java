package com.anyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 读取项目相关配置
 *
 * @author Jianpan
 */
@Data
@Component
@ConfigurationProperties(prefix = "anyi")
public class AnyiConfig {
    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 上传文件路径 */
    private String profile;

    /** 版权年份 */
    private String copyrightYear;

    /** 实例演示开关 */
    private boolean demoEnabled;

    /** 获取地址开关 */
    private boolean addressEnabled;

    /** 验证码类型 */
    private String captchaType;

    /** 是否开启测试模式 */
    private boolean testEnable;

    private static String profilePath;

    public static String getProfile() {
        return profilePath;
    }

    @PostConstruct
    private void initConstants() {
        profilePath = profile;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath()
    {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}
