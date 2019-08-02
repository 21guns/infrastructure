package com.guns21.support.service;

import com.guns21.data.domain.result.PageData;

import java.util.List;
import java.util.Optional;

/**
 * 所有查询需要走该类,禁止添加修改方法
 * Created by jliu on 16/7/6.
 */
public interface QueryService<R,Q,Id> extends Service<R,Q> {

    List<R> page(Q dto, PageData pagerParams);

    Optional<R> getById(Id id);
}
