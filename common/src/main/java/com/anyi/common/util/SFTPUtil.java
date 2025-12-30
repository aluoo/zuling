package com.anyi.common.util;

import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.ssh.Sftp;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Properties;

@Slf4j
public class SFTPUtil {


    /**
     * 初始化
     *
     * @param ip       远程主机IP地址
     * @param port     远程主机端口
     * @param username 远程主机登陆用户名
     * @param password 远程主机登陆密码
     */
    public Sftp init(String ip, Integer port, String username, String password) {
        Session session = JschUtil.getSession(ip, port, username, password);
        return JschUtil.createSftp(session);
    }

    public void put(Sftp sftp, String srcFilePath, String destPath) {
        sftp.put(srcFilePath, destPath);
    }

    public void download(Sftp sftp, String src, File destFile) {
        sftp.download(src, destFile);
    }


}
