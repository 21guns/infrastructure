package com.guns21.support.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 实体抽象父类
 * @author jliu
 */
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
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
