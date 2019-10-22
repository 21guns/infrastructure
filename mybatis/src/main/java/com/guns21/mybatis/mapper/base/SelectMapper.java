package com.guns21.mybatis.mapper.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface SelectMapper<QUERY, DTO>  {

    Optional<DTO> selectById(@Param("id") String id);

    Optional<DTO> select(@Param("query") QUERY queryDTO);

    List<DTO> pageList(@Param("query") QUERY queryDTO, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
}
