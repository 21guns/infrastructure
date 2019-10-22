package com.guns21.mybatis.mapper;

import com.guns21.mybatis.mapper.base.BatchMapper;

/**
 * Created by ljj on 2018/11/27.
 */
public interface BaseAllMapper<DTO, QUERY, DO> extends BaseMapper<DTO, QUERY, DO>, BatchMapper<DO> {


}
