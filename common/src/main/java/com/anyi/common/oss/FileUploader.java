package com.anyi.common.oss;


import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.model.GetObjectRequest;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.snowWork.SnowflakeIdService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;


@Component
@Slf4j
public class FileUploader {
    @Value("${ali.oss.accessKeyId:}")
    private String accessKeyId;

    @Value("${ali.oss.accessKeySecret:}")
    private String accessKeySecret;

    //    @Value("${ali.oss.accessKeySecret}")
    private String endpoint = "oss-cn-hangzhou.aliyuncs.com";

    @Value("${ali.oss.contractBucket:}")
    private String contractBucket;

    private static Logger logger = LoggerFactory.getLogger(FileUploader.class);

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    private OSS oss;

    //@PostConstruct
//    public void init() {
//        createBucket(bucket);
//    }

//    private void createBucket(String bucket) {
//        OSSClient ossClient = getOSSClient();
//        if (ossClient.doesBucketExist(bucket)) {
//            logger.info("您已经创建Bucket:{}", bucket);
//        } else {
//            logger.info("您的Bucket不存在，创建Bucket{}", bucket);
//            ossClient.createBucket(bucket);
//        }
//    }

    @PostConstruct
    private OSS initOSSClient() {
//         创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        conf.setMaxConnections(200);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        conf.setSocketTimeout(10000);
        // 设置建立连接的超时时间，默认为50000毫秒。
        conf.setConnectionTimeout(10000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        conf.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        conf.setIdleConnectionTime(10000);
        // 设置失败请求重试次数，默认为3次。
        conf.setMaxErrorRetry(5);
        // 设置连接OSS所使用的协议（HTTP/HTTPS），默认为HTTP。
        conf.setProtocol(Protocol.HTTP);
        // 设置用户代理，指HTTP的User-Agent头，默认为aliyun-sdk-java。
        conf.setUserAgent("aliyun-sdk-java");
        this.oss = new OSSClientBuilder().build("http://" + endpoint, accessKeyId, accessKeySecret, conf);
        return this.oss;
    }

    /**
     * 上传文件
     */
//    public String upload(CommonsMultipartFile multipartFile) {
//        OSSClient ossClient = getOSSClient();
//        String suffix = getSuffix(multipartFile.getOriginalFilename());
//        try {
//            String fileName = snowflakeIdService.nextId() + "." + suffix;
//            ossClient.putObject(bucket, fileName, multipartFile.getInputStream());
//            return "https://" + bucket + "." + endpoint + fileName;
//        } catch (Exception e) {
//            logger.error(e.getLocalizedMessage(), e);
//            throw new BusinessException(BizError.FILE_UPLOAD_ERROR);
//        } finally {
//            ossClient.shutdown();
//        }
//    }

//    public String upload(CommonsMultipartFile multipartFile) {
//        OSSClient ossClient = getOSSClient();
//        String suffix = getSuffix(multipartFile.getOriginalFilename());
//        try {
//            String fileName = snowflakeIdService.nextId() + "." + suffix;
//            ossClient.putObject(bucket, fileName, multipartFile.getInputStream());
//            return "https://" + bucket + "." + endpoint + fileName;
//        } catch (Exception e) {
//            logger.error(e.getLocalizedMessage(), e);
//            throw new BusinessException(BizError.FILE_UPLOAD_ERROR);
//        } finally {
//            ossClient.shutdown();
//        }
//    }
    public String upload(byte[] bytes, String fileName, String bucket) {
        try {
            this.oss.putObject(bucket, fileName, new ByteArrayInputStream(bytes));
            return "https://" + bucket + "." + endpoint + "/" + fileName;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BusinessException(BizError.FILE_UPLOAD_ERROR);
        } finally {
//            ossClient.shutdown();
        }
    }

    public String upload(File file, String fileName, String bucket) {
        try {
            this.oss.putObject(bucket, fileName, file);
            return "https://" + bucket + "." + endpoint + "/" + fileName;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BusinessException(BizError.FILE_UPLOAD_ERROR);
        } finally {
            file.delete();
        }
    }

    /**
     * 从oss上获取图片文件，先保存到本地，记得使用后删除
     *
     * @param fileUrl
     */
    public File getImageFileFromOss(String fileUrl, String bucketName) {
        // 去掉目录提取文件名
        String[] split = fileUrl.split("/");
        // 生成本地的图片文件
        File targetFile = new File(split[split.length - 1]);
        // 创建OSSClient实例
        try {
            this.oss.getObject(new GetObjectRequest(bucketName, fileUrl), targetFile);
            return targetFile;
        } catch (Exception e) {
            targetFile.delete();
            throw new BaseException(99999, "从oss上获取图片文件失败");
        } finally {
        }
    }


    public String getContractBucket(){
        return contractBucket;
    }
    private String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    public boolean exist(String bucket, String fileName) {
        return this.oss.doesObjectExist(bucket, fileName);
    }
}
