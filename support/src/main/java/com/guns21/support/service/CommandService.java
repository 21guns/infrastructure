package com.guns21.support.service;


import com.guns21.data.domain.result.MessageResult;

/**
 * 所有修改需要走该类,禁止添加查询方法
 * Created by jliu on 16/7/6.
 */
public  interface CommandService<E,Id> extends Service<E,Id> {

    MessageResult save(E dto);

    MessageResult update(E dto);

    MessageResult deleteById(Id id);
}
