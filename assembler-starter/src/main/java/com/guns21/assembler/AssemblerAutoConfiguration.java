package com.guns21.assembler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.regex.Pattern;

@EnableConfigurationProperties({AssemblerConfiguration.class})
@Configuration
public class AssemblerAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(AssemblerAutoConfiguration.class);

    private AssemblerConfiguration assemblerConfiguration;

    public AssemblerAutoConfiguration(AssemblerConfiguration assemblerConfiguration) {
        this.assemblerConfiguration = assemblerConfiguration;
    }
    @PostConstruct
    public void init() throws ClassNotFoundException {
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        // add include filters which matches all the classes (or use your own)
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*DTO")));
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*DO")));

        // get matching classes defined in the package
        final Set<BeanDefinition> dtoClasses = provider.findCandidateComponents("com.ktjr.khc.user.api.dto");

        final Set<BeanDefinition> doClasses = provider.findCandidateComponents("com.ktjr.khc.user.service.entity");

        // this is how you can load the class type from BeanDefinition instance
        for (BeanDefinition bean : dtoClasses) {
            Class<?> clazz = Class.forName(bean.getBeanClassName());
            // ... do your magic with the class ...
            System.err.println(clazz.getSimpleName());
        }
        for (BeanDefinition bean : doClasses) {
            Class<?> clazz = Class.forName(bean.getBeanClassName());
            // ... do your magic with the class ...
            System.err.println(clazz.getSimpleName());
        }
    }

}
