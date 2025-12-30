package com.anyi.common.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取IP方法
 * 
 * @author Jianpan
 */
@Slf4j
public class IpUtils {

    /**
     * 获取IP地址
     * 
     * @return 本地IP地址
     */
    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
        return "127.0.0.1";
    }

    /**
     * 获取主机名
     * 
     * @return 本地主机名
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
        return "未知";
    }

}