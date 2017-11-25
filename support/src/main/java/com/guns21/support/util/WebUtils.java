package com.guns21.support.util;

import com.guns21.support.boot.config.WebConfig;
import org.apache.commons.lang3.StringUtils;

public class WebUtils {

     /* --------------------------------------------------- build url --------------------------------------------------- */

    public static String buildRelativeUrlWithFile(String savePath, String fileName) {
        return savePath + fileName;
    }

    public static String buildWebUrlWithFile(String savePath, String fileName) {
        if (StringUtils.isEmpty(savePath)) {
            return null;
        }
        return WebConfig.WWW_RESOURCE_URL + WebConfig.WBE_UPLOAD_PATH + buildRelativeUrlWithFile(savePath, fileName);
    }

    public static String buildWebUrlWithFile(String url) {
        return buildWebUrlWithFile(url, "");
    }


}
