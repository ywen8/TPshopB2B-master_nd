/*
 * Copyright (C) 2015 ShenZhen Moze Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳市么子信息技术有限公司开发研制，未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 */
package com.tpshop.mall.common;

/**
 * @author wangqh
 */
public class SPTableConstanct {

    private final static String TABLE_NAME_CATEGORY = "tp_goods_category";
    public final static String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CATEGORY + "(" +
            "id integer, " +
            "name STRING NOT NULL, " +
            "parent_id INTEGER NOT NULL , " +
            "level INTEGER NOT NULL ," +
            "image STRING ," +
            "is_hot INTEGER , " +
            "sort_order INTEGER) ";
    public final static String TABLE_NAME_MESSAGE = "tp_message";
    public final static String CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MESSAGE + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title STRING NOT NULL, " +
            "message STRING NOT NULL , " +
            "msg_id INTEGER ," +
            "receiver_time TIMESTAMP default (datetime('now', 'localtime')) ," +         //默认时间为当前时间
            "is_read INTEGER DEFAULT 0 ) ";      //0未读,1:已读

}
 
