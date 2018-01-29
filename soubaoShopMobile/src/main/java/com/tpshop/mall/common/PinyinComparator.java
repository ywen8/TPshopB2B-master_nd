package com.tpshop.mall.common;

import com.tpshop.mall.model.SPCity;

import java.util.Comparator;

/**
 * @author xiaanming
 */
public class PinyinComparator implements Comparator<SPCity> {

    public int compare(SPCity o1, SPCity o2) {
        if (o1.getInitial().equals("@") || o2.getInitial().equals("#")) {
            return -1;
        } else if (o1.getInitial().equals("#") || o2.getInitial().equals("@")) {
            return 1;
        } else {
            return o1.getInitial().compareTo(o2.getInitial());
        }
    }

}
