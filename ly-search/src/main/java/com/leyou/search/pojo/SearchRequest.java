package com.leyou.search.pojo;

public class SearchRequest {

    //搜索字段
    private String key;
    private Integer page;
    private Integer size;

    private static final int DEFAULT_ROWS = 20;
    private static final int DEFAULT_PAGE = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPage() {
        if(page == null) {
            return 1;
        }
        return Math.max(DEFAULT_PAGE, page);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Integer getSize() {
        if(size == null) {
            size = DEFAULT_ROWS;
        }
        return Math.max(DEFAULT_ROWS, size);
    }

    public void setSize(Integer size) {

        this.size = size;
    }
}
