package com.guns21.captcha.boot.config;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.renderer.DefaultWordRenderer;
import nl.captcha.text.renderer.WordRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 将Captcha存储到redis中
 * key: "captcha_" + Captcha.getAnswer()
 * value: Captcha.getAnswer()
 *
 * @see nl.captcha.servlet.SimpleCaptchaServlet
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

    private static final List<Color> COLORS = new ArrayList<Color>(3);
    private static final List<Font> FONTS = new ArrayList<Font>(3);

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
        WordRenderer wordRenderer = new DefaultWordRenderer(COLORS, FONTS);
        Captcha captcha = (new Captcha.Builder(width, height)).addText(wordRenderer).gimp().addNoise().build();
        template.opsForValue().set(KEY_PREFIX + captcha.getAnswer(),
                captcha.getAnswer(),
                timeout,
                TimeUnit.SECONDS);

        CaptchaServletUtil.writeImage(resp, captcha.getImage());
    }

    static {
        COLORS.add(Color.magenta);
        COLORS.add(Color.DARK_GRAY);
        COLORS.add(Color.PINK);
        FONTS.add(new Font("Geneva", 0, DEFAULT_FONT_SIZE));
        FONTS.add(new Font("Courier", 2, DEFAULT_FONT_SIZE));
        FONTS.add(new Font("Arial", 2, DEFAULT_FONT_SIZE));
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
