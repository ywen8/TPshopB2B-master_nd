package com.tpshop.mall.entity;

/**
 * Created by admin on 2017/5/15
 */
public class FilterSortMode {

    public String text;
    public int showIcon;
    public String sortType;

    public FilterSortMode(int showIcon, String text, String sortType) {
        this.showIcon = showIcon;
        this.text = text;
        this.sortType = sortType;
    }

}
