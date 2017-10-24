package com.guns21.support.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.*;
import java.util.Date;

/**
 * 实体抽象父类
 * Created by chenbei on 16/1/5.
 */
@Data
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    /**
     * 唯一标识
     */
    @Id
    protected String id;
    /**
     * 创建时间
     */
    protected Date createTime;

}
