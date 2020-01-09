package com.guns21.web.boot.config;

import com.guns21.common.enums.ValuableEnum;
import com.guns21.common.util.ObjectUtils;
import com.guns21.web.formatter.ValuableEnumFormatter;
import com.guns21.web.method.resolver.RequireUuidMethodArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Set;

/**
 * 1.implements WebMvcRegistrations spring boot 2 support
 * 2.extend WebMvcConfigurerAdapter spring boot < 2
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Value("${com.guns21.spring.mvc.valuable-enum-package:#{null}}")
    private String valuableEnumPackage;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthTokenInterceptor()).addPathPatterns("/app/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new RequireUuidMethodArgumentResolver());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        if (!ObjectUtils.hasText(valuableEnumPackage)) {
            return;
        }
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        // Add include filters which matches all the classes (or use your own)
        provider.addIncludeFilter(new AssignableTypeFilter(ValuableEnum.class));

        // Aet matching classes defined in the package
        final Set<BeanDefinition> enumClasses = provider.findCandidateComponents(valuableEnumPackage);

        enumClasses.forEach(beanDefinition -> {
            try {
                Class<Object> clazz = (Class<Object>) Class.forName(beanDefinition.getBeanClassName());
                if (ValuableEnum.class.isAssignableFrom(clazz) && Enum.class.isAssignableFrom(clazz)) {
                    registry.addFormatterForFieldType(clazz, new ValuableEnumFormatter<>(clazz));
                }
            } catch (ClassNotFoundException e) {
                logger.error("class not found", e);
            }
        });
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }


}