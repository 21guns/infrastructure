package com.guns21.result.domain.query;

/**
 * Created by jliu on 2016/12/2.
 */
public class PageQuery {

    private int current;//当前页数
    private int pageSize;//每页条数

    private Sorter sorter;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Sorter getSorter() {
        return sorter;
    }

    public void setSorter(Sorter sorter) {
        this.sorter = sorter;
    }
}
