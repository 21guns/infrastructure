package com.guns21.assembler;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.guns21.assembler")
public class AssemblerConfiguration {

    private String[] doBasePackages;

    private String[] dtoBasePackages;

    public void setDoBasePackages(String[] doBasePackages) {
        this.doBasePackages = doBasePackages;
    }

    public void setDtoBasePackages(String[] dtoBasePackages) {
        this.dtoBasePackages = dtoBasePackages;
    }

    public String[] getDoBasePackages() {
        return doBasePackages;
    }

    public String[] getDtoBasePackages() {
        return dtoBasePackages;
    }

}
