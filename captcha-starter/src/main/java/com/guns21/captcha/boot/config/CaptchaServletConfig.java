package com.guns21.captcha.boot.config;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 将Captcha存储到redis中
 * key: "captcha_" + Captcha.getAnswer()
 * value: Captcha.getAnswer()
 *
 */
@Configuration
@ConfigurationProperties(prefix = "com.guns21.captcha")
public class CaptchaServletConfig extends HttpServlet {
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 50;
    public static final int DEFAULT_FONT_SIZE = 48;
    public static final long DEFAULT_KEY_TIMEOUT = 120;

    public static final String KEY_PREFIX = "captcha-";

    /* ------------------ 配置参数 -----------------*/
    //产生captcha的url
    private String url = "/api/v1/captcha";
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private long timeout = DEFAULT_KEY_TIMEOUT; //秒

    @Autowired
    private RedisTemplate<String, String> template;

    /**
     * 请求处理
     *
     * @param req  请求
     * @param resp 响应
     * @throws ServletException 处理异常
     * @throws IOException      io异常
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SpecCaptcha captcha = new SpecCaptcha(width, height, 5);
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        String verCode = captcha.text().toLowerCase();
        template.opsForValue().set(KEY_PREFIX + verCode,
                verCode,
                timeout,
                TimeUnit.SECONDS);

        setHeader(resp);//CaptchaUtil.setHeader(resp);
        captcha.out(resp.getOutputStream());
    }
    public static void setHeader(HttpServletResponse response) {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
    }


    @Bean
    public ServletRegistrationBean captchaServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(this);

        registration.setUrlMappings(Arrays.asList(url));
        return registration;
    }

    public CaptchaServletConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public CaptchaServletConfig setWidth(int width) {
        this.width = width;
        return this;
    }

    public CaptchaServletConfig setHeight(int height) {
        this.height = height;
        return this;
    }
}
