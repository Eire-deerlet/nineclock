package com.nineclock.common.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.nineclock.common.enums.ResponseEnum;
import com.nineclock.common.exception.NcException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Slf4j
@Data
public class OssClientUtils {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String host;

    /**
     * 上传文件
     * @param fileName 原始文件名
     * @param inputStream 文件输入流
     * @return
     */
    public String uploadFile(String fileName, InputStream inputStream) {

        OSS ossClient = new OSSClientBuilder().build("http://" + endpoint, accessKeyId, accessKeySecret);

        fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "-" + fileName;

        try {
            PutObjectResult objectResult = ossClient.putObject(bucketName, fileName, inputStream);

            log.info("文件 {} 上传成功, 上传的结果为: {}", fileName, objectResult.getResponse());

            // 拼接url路径
            return "https://" + host + "/" + fileName;

        } catch (Exception e) {
            log.error("文件上传失败，错误信息：", e);
            throw new NcException(ResponseEnum.FILE_UPLOAD_ERROR);

        } finally {
            // 关闭OSSClient
            ossClient.shutdown();
        }
    }
}