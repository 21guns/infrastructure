package com.guns21.support.entity;

import com.guns21.common.uuid.ID;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.*;

/**
 * 实体抽象父类
 * @author jliu
 */
@Data
@MappedSuperclass
public abstract class BaseIDEntity implements Serializable {

    /**
     * 唯一标识
     */
    @Id
    private String id;

    /**
     *
     */
    @PrePersist
    public void prePersist() {
        setId(ID.get());
    }
}
