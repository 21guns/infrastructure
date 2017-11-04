package com.guns21.support.entity;

import com.guns21.common.util.DateUtils;
import com.guns21.common.uuid.ID;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
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

    /**
     *
     */
    @PrePersist
    public void prePersist() {
        setCreateTime(DateUtils.newDate());
        setUpdateTime(DateUtils.newDate());
    }
}
