package com.guns21.mybatis.mapper;

import com.guns21.mybatis.mapper.base.DeleteMapper;
import com.guns21.mybatis.mapper.base.InsertMapper;
import com.guns21.mybatis.mapper.base.SelectMapper;
import com.guns21.mybatis.mapper.base.UpdateMapper;

/**
 * Created by ljj on 2018/11/27.
 */
public interface BaseMapper<DTO, QUERY, DO> extends SelectMapper<QUERY, DTO>, InsertMapper<DO>, DeleteMapper<DO>, UpdateMapper<DO> {


}
