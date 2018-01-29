package com.tpshop.mall.dao;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by admin on 2016/8/19
 */
class SPPushMessageTable {

    //BaseColumn类中已经包含了_id字段
    static final class PushMessageColumn implements BaseColumns {
        static final Uri CONTENT_URI = Uri.parse("content://com.tpshop.mall.dao.SPPushMessageTableProvider");
    }

}
