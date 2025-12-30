package com.anyi.sparrow.exchange.service;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.oss.FileUploader;
import com.anyi.common.oss.OSSBucketEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/7/15
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class QrCodeService {
    @Autowired
    FileUploader fileUploader;

    public String decode(String fileUrl) {
        if (StrUtil.isBlank(fileUrl)) {
            throw new BaseException(-1, "图片地址不能为空");
        }
        String key = parseUrl(fileUrl);
        String result;
        File img = null;
        try {
            img = fileUploader.getImageFileFromOss(key, OSSBucketEnum.AYCX_PHONE_EXCHANGE.getName());
            result = QrCodeUtil.decode(img);
        } catch (Exception e) {
            log.error("decode error: {}", ExceptionUtil.getMessage(e));
            throw new BaseException(-1, "二维码识别失败");
        } finally {
            if (img != null && img.exists()) {
                img.delete();
            }
        }
        log.info("img: [{}] | decode result is [{}]", fileUrl, result);
        if (StrUtil.isBlank(result)) {
            throw new BaseException(-1, "二维码识别失败");
        }
        return result;
    }

    private String parseUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            return path;
        } catch (MalformedURLException e) {
            log.error("parse url error {}", ExceptionUtil.getMessage(e));
            return null;
        }
    }
}