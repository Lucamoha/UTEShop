package com.uteshop.dao.manager.common;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResult<T> {
    public final List<T> content;
    public final long total;
    public final int page;   // 1-based
    public final int size;

    public PageResult(List<T> content, long total, int page, int size) {
        this.content = content; this.total = total; this.page = page; this.size = size;
    }
}
