package com.guns21.support.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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

}
