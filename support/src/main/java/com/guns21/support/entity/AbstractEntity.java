package com.guns21.support.entity;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * 实体抽象父类
 *
 * @author jliu
 */
@Data
@MappedSuperclass
public abstract class AbstractEntity extends BaseIDEntity {

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModified;

    /**
     *
     */
    @Override
    @PrePersist
    public void prePersist() {
        super.prePersist();
        setGmtCreate(LocalDateTime.now());
        setGmtModified(LocalDateTime.now());
    }

    /**
     *
     */
    @PreUpdate
    public void preUpdate() {
        setGmtModified(LocalDateTime.now());
    }
}
