package com.guns21.support.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 实体抽象父类
 *
 * @author jliu
 */
@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractEntity<ID> extends BaseIDEntity<ID> {

    /**
     * 创建时间
     */
    protected LocalDateTime gmtCreated;

    /**
     * 更新时间
     */
    protected LocalDateTime gmtModified;

    /**
     *
     */
    @Override
    public void preCreate() {
        super.preCreate();
        setGmtCreated(LocalDateTime.now());
        setGmtModified(LocalDateTime.now());
    }

    /**
     *
     */
    public void preUpdate() {
        setGmtModified(LocalDateTime.now());
    }
}
