package com.guns21.mybatis;

import com.github.pagehelper.Page;

import java.util.List;

public class PageUtils {

    /**
     * 当使用com.github.pagehelper 时会返回Page，要对这个Page进行stream操作时会
     * 抹除掉分页信息，需要使用该方法进行转换，
     * 使用示例
     *         InvitationCollectionDO doo = AssemblerFactory.to(dto, InvitationCollectionDO.class);
     *     	List<InvitationCollectionDO> dos = mapper.pageList(doo, pagerParams.getPage(), pagerParams.getPageSize());
     *         List<InvitationCollectionDTO> collect = dos.stream()
     *                 .map(d -> {
     *                     InvitationCollectionDTO to = AssemblerFactory.to(d, InvitationCollectionDTO.class);
     *                     to.setInvitation(AssemblerFactory.to(d.getInvitation(), InvitationDTO.class));
     *                     return to;
     *                 })
     *                 .collect(Collectors.toList());
     *         return PageUtils.transform(dos, collect);
     * @param source 带Page信息的list
     * @param target stream转换后的list
     * @return
     */
    public static List transform(List source, List target) {

        if (source instanceof Page) {
            Page sourcePage = (Page) source;
            Page page = new Page(sourcePage.getPageNum(),
                    sourcePage.getPageSize(), sourcePage.isCount());
            page.setTotal(sourcePage.getTotal());
            page.setPages(sourcePage.getPages());
            page.countColumn(sourcePage.getCountColumn())
                    .setOrderBy(sourcePage.getOrderBy())
                    .setOrderByOnly(sourcePage.isOrderByOnly());
            page.addAll(target);
            return page;
        }
        return source;
    }
}
