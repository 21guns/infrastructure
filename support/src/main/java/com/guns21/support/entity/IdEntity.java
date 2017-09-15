package com.guns21.support.entity;

import lombok.Data;

import javax.persistence.Id;
import java.io.*;

/**
 * 实体抽象父类
 * Created by chenbei on 16/1/5.
 */
@Data
public abstract class IdEntity implements Serializable {

    /**
     * 唯一标识
     */
    @Id
    private String id;

}
