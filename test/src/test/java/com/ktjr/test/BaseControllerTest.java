package com.ktjr.test;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.intercept.MockHttpServletRequestBuilderInterceptor;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

/**
 * Created by jliu on 16/8/17.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BaseControllerTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;


    @Before
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        given().contentType("application/json");
    }

    protected static MockMvcRequestSpecification givenWithAuth(final Object userInfo) {
        try {
            //获得session id
            Object nextId = FieldUtils.readDeclaredStaticField(MockHttpSession.class, "nextId", true);
            return given().
                    interceptor(new MockHttpServletRequestBuilderInterceptor(){
                        @Override
                        public void intercept(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) {
                            // 设置登录用户
                            mockHttpServletRequestBuilder.sessionAttr("loginUser",userInfo);
                        }
                    }).
                    header("x-auth-token",nextId);
        } catch (IllegalAccessException e) {
        }
        throw new IllegalArgumentException();
    }

    protected static Map toMap(Object object) {

        BeanMap beanMap = BeanMap.create(object);
        //移除空值
        Map map = new HashMap(beanMap);
        map.values().removeIf(Objects::isNull);

        return map;
    }
}
