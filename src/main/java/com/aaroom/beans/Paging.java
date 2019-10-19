package com.aaroom.beans;

import lombok.Data;

/**
 * 翻页使用的对象
 *
 * @author
 */
@Data
public class Paging {

    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页数量
     */
    private int size;

    /**
     * 总数
     */
    private int count;

    /**
     * 显示的 第一个页码
     */
    private int first;

    /**
     * 显示的最后一个页码
     */
    private int last;

    /**
     * 总页数
     */
    private int total;

    public Paging() {
    }

    public Paging(int p, int s, int c) {
        this(p, s, c, 5);
    }

    /**
     * @param p 当前页
     * @param s 每页数量
     * @param c 总数量
     */
    public Paging(int p, int s, int c, int totalPages) {
        this(p, s, c, totalPages, 100);
    }

    public Paging(int p, int s, int c, int totalPages, int maxPage) {
        this.size = s;
        this.count = c;

        total = count / size + (count % size == 0 ? 0 : 1);

        if (total > maxPage)
            total = maxPage;

        if (p < 1)
            page = 1;
        else if (p > total && total > 0)
            page = total;
        else
            page = p;

        first = Math.max(1, page - totalPages + 1);
        last = Math.min(total, page + totalPages - 1);
        if (last < first)
            last = first;

        // 如果页码超过了5页
        while (last - first >= totalPages) {
            if (page - first < last - page)
                last--;
            else
                first++;
        }
    }

}
