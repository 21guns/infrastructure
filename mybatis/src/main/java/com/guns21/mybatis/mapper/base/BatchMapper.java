package com.guns21.mybatis.mapper.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatchMapper<DO> {

    Integer batchInsert(@Param("list") List<DO> list);

    Integer batchUpdate(@Param("list") List<DO> list);
}
