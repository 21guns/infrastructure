package com.guns21.support.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jliu on 16/7/12.
 */
@Configuration
@ConfigurationProperties(prefix = "com.guns21.web")
public class WebConfig {

    public static String WBE_UPLOAD_PATH;
//    public static String WWW_DOMAIN_URL;
    public static String WWW_RESOURCE_URL;

    public void setWebUploadPath(String webUploadPath) {
        WBE_UPLOAD_PATH = webUploadPath;
    }

    public void setWwwDomainUrl(String wwwDomainUrl) {
//        WWW_DOMAIN_URL = wwwDomainUrl;
    }

    public void setWwwResourceUrl(String wwwResourceUrl) {
        WWW_RESOURCE_URL = wwwResourceUrl;
    }
}
