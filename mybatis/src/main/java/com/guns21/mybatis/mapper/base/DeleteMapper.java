package com.guns21.mybatis.mapper.base;

import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface DeleteMapper<DO> {

    Integer deleteById(@Param("id") String id);

    Integer deleteByIds(@Param("ids") Set<String> list);
}
