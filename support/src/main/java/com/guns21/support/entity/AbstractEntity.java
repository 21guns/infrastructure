package com.guns21.support.entity;

import com.guns21.common.util.DateUtils;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

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
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     *
     */
    @Override
    @PrePersist
    public void prePersist() {
        super.prePersist();
        setCreateTime(DateUtils.newDate());
        setUpdateTime(DateUtils.newDate());
    }

    /**
     *
     */
    @PreUpdate
    public void preUpdate() {
        setUpdateTime(DateUtils.newDate());
    }
}
