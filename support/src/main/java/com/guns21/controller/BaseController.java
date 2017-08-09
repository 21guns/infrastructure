package com.guns21.controller;

import com.guns21.domain.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础的控制层
 * @author liufeng
 *
 */
public abstract class BaseController {
    @Autowired
    protected ResultFactory resultFactory;


}
