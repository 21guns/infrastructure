package com.guns21.upload.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chhw on 2016/9/28.
 */
@Configuration
@ConfigurationProperties(prefix = "com.guns21.qiniu")
public class QiNiuConfig {
    //密钥AccessKey
    public static String ACCESS_KEY;
    //密钥SecretKey
    public static String SECRET_KEY;
    //存储空间
    public static String BUCKET_NAME;
    //断点上传存放路径
    public static String BREAKPOINT_UPLOAD_PATH;
    //预转七牛720P(通用)
    public static String VIDEO_FORMAT_HD_FOPS;
    //预转七牛480P(通用)
    public static String VIDEO_FORMAT_SD_FOPS;
    //七牛转码的队列
    public static String VIDEO_TRANS_CODE_PIPELINE;

    public static String IMAGE_ZOOM_PIXEL;

    public static void setAccessKey(String accessKey) {
        ACCESS_KEY = accessKey;
    }

    public static void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    public static void setBucketName(String bucketName) {
        BUCKET_NAME = bucketName;
    }

    public static void setBreakpointUploadPath(String breakpointUploadPath) {
        BREAKPOINT_UPLOAD_PATH = breakpointUploadPath;
    }

    public static void setVideoFormatHdFops(String videoFormatHdFops) {
        VIDEO_FORMAT_HD_FOPS = videoFormatHdFops;
    }

    public static void setVideoFormatSdFops(String videoFormatSdFops) {
        VIDEO_FORMAT_SD_FOPS = videoFormatSdFops;
    }

    public static void setVideoTransCodePipeline(String videoTransCodePipeline) {
        VIDEO_TRANS_CODE_PIPELINE = videoTransCodePipeline;
    }

    public static void setImageZoomPixel(String imageZoomPixel) {
        IMAGE_ZOOM_PIXEL = imageZoomPixel;
    }
}

