package com.guns21.mybatis;

import com.github.pagehelper.Page;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PageUtils {

    /**
     * 当使用com.github.pagehelper 时会返回Page，要对这个Page进行stream操作时会
     * 抹除掉分页信息，需要使用该方法进行转换，
     * 示例:
     *  List<ShopDO> dos = shopMapper.pageByIds(doo, shopIds, pagerParams.getPage(), pagerParams.getSize());
     *         return dos.stream().map(ddo -> AssemblerFactory.to(ddo, ShopDTO.class))
     *                 .collect(PageUtils.toPages(dos));
     *
     * @param pageInfoList 包含page信息的list
     * @param <T>
     * @param <?>
     * @return
     */
    public static <T> Collector<T, ?, List<T>> toPages(List<?> pageInfoList){
        if (Objects.isNull(pageInfoList)) {
            return Collectors.toList();
        }
        if (pageInfoList instanceof Page) {
            Page sourcePage = (Page) pageInfoList;
            Page<T> page = new Page<T>(sourcePage.getPageNum(),
                    sourcePage.getPageSize(), sourcePage.isCount());
            page.setTotal(sourcePage.getTotal());
            page.setPages(sourcePage.getPages());
            page.countColumn(sourcePage.getCountColumn())
                    .setOrderBy(sourcePage.getOrderBy())
                    .setOrderByOnly(sourcePage.isOrderByOnly());
            return Collector.of(() -> page, List::add,
                    (left, right) -> {
                        left.addAll(right);
                        return left;
                    });
        } else if (pageInfoList instanceof List) {
            return Collectors.toList();
        }
        return Collectors.toCollection(Page::new);
    }

    public static <T> Collector<T, ?, List<T> > emptyPages(){
        return Collectors.toCollection(Page::new);
    }
}
