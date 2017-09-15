package com.guns21.support.service;

import com.guns21.data.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 所有修改需要走该类,禁止添加查询方法
 * Created by jliu on 16/7/6.
 */
public abstract class BaseCommandService extends BaseService {

    @Autowired
    protected ResultFactory resultFactory;
}
