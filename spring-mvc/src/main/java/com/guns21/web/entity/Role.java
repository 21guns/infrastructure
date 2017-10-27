package com.guns21.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;

/**
 * Created by ljj on 17/7/14.
 */
@Data
@AllArgsConstructor
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String nickname;

}
