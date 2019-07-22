package com.guns21.feign.boot.config;

import com.guns21.feign.annotation.FeignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * Created by Liu Xiang on 2019-07-19.
 */
@Slf4j
@Import(FeignServiceConfig.FeignServiceRegistrar.class)
@Configuration
public class FeignServiceConfig {

    private static final String FEIGN_SERVICE_PACKAGE_NAMES_CONFIG_KEY = "com.guns21.open-feign.feign-service-packages";
    private static final String DEFAULT_FEIGN_SERVICE_PACKAGE_NAMES = "com.ktjr.ddhc.**.service.feign";

    public static class FeignServiceRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

        private Environment environment;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            FeignServiceScanner scanner = new FeignServiceScanner(registry);
            scanner.addIncludeFilter(new AnnotationTypeFilter(FeignService.class));
            scanner.doScan(getBasePackages());
        }

        private String[] getBasePackages() {
            if (environment == null) {
                return DEFAULT_FEIGN_SERVICE_PACKAGE_NAMES.split(",");
            }

            String pkgName = environment.getProperty(FEIGN_SERVICE_PACKAGE_NAMES_CONFIG_KEY);

            if (StringUtils.isBlank(pkgName)) {
                return DEFAULT_FEIGN_SERVICE_PACKAGE_NAMES.split(",");
            }

            return pkgName.split(",");
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }
    }
}
