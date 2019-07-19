package com.guns21.feign.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guns21.feign.annotation.FeignService;
import com.guns21.feign.codec.ResultDecoder;
import com.guns21.feign.target.SpringSessionHeaderTokenTarget;
import feign.Feign;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Created by Liu Xiang on 2019-07-19.
 */
@Configuration
@Slf4j
public class FeignServiceConfig {

    @Value("${com.guns21.feign.service-package:com.ktjr.ddhc.**.service.feign}")
    private String servicePackage;

    @Autowired
    private Environment env;

    @PostConstruct
    public void registerFeignServiceBeans(GenericApplicationContext context, ObjectMapper objectMapper) {

        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        provider.addIncludeFilter(new AnnotationTypeFilter(FeignService.class));

        // Aet matching classes defined in the package
        final Set<BeanDefinition> serviceInterfaces = provider.findCandidateComponents(servicePackage);

        serviceInterfaces.forEach(beanDefinition -> {
            try {
                Class clazz = Class.forName(beanDefinition.getBeanClassName());
                FeignService annotation = (FeignService) clazz.getAnnotation(FeignService.class);
                String urlPrefix = annotation.value();
                if (urlPrefix.matches("^\\$\\{.+\\}$")) {
                    urlPrefix = env.getProperty(urlPrefix.replaceAll("^\\$\\{|\\}$", ""));
                }
                String finalUrlPrefix = urlPrefix;
                context.registerBean(clazz, () -> Feign.builder()
                        .encoder(new JacksonEncoder(objectMapper))
                        .decoder(new ResultDecoder(objectMapper))
                        .target(SpringSessionHeaderTokenTarget.newTarget(clazz, finalUrlPrefix)));

            } catch (ClassNotFoundException e) {
                log.error("{}", e);
            }
        });

    }
}
