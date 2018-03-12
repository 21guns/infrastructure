package com.guns21.domain.query;

import lombok.Data;

/**
 * Created by jliu on 2016/12/2.
 */
@Data
public class PageQuery {

    //当前页数
    private int pageNum;

    /**
     * 每页条数
     */
    private int pageSize;

    private Sorter sorter;

}