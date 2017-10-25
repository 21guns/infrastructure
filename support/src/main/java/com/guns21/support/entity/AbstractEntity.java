package com.guns21.support.entity;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 实体抽象父类
 * @author jliu
 */
@Data
@MappedSuperclass
public abstract class AbstractEntity extends BaseIDEntity {

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
