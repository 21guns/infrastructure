package com.guns21.support.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 实体抽象父类
 * @author jliu
 */
@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode
public abstract class BaseIDEntity<ID> implements Serializable {

    /**
     * 唯一标识
     */
    protected ID id;

    /**
     *
     */
    public void preCreate() {
    }
}
