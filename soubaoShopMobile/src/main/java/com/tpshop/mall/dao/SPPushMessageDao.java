/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年10月28日 下午9:10:48
 * Description:	处理推送过来的消息
 *
 * @version V1.0
 */
package com.tpshop.mall.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.common.SPTableConstanct;
import com.tpshop.mall.model.SPPushMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/27
 */
public class SPPushMessageDao {

    private SPMobileDBHelper dbHelper = null;
    private static SPPushMessageDao instance = null;

    private SPPushMessageDao(Context context) {
        this.dbHelper = new SPMobileDBHelper(context);
    }

    public static synchronized SPPushMessageDao getInstance(Context context) {
        if (null == instance)
            instance = new SPPushMessageDao(context);
        return instance;
    }

    /**
     * 缓存到本地数据库
     */
    public void insertMessage(SPPushMessage message) {
        if (message == null) return;
        SQLiteDatabase dbwrite = null;
        try {
            dbwrite = dbHelper.getWritableDatabase();
            //添加之前,删除之前缓存之外的数据
            String dirtySql = "SELECT id FROM " + SPTableConstanct.TABLE_NAME_MESSAGE + " ORDER BY id DESC LIMIT " +
                    SPMobileConstants.CacheMessageCount + " OFFSET 0";
            Cursor dirtyCursor = dbwrite.rawQuery(dirtySql, null);
            if (dirtyCursor.getCount() > SPMobileConstants.CacheMessageCount && dirtyCursor.moveToLast()) {
                //按ID降序排序,查找SPMobileConstants.CacheMessageCount条之内的最小ID
                int minId = dirtyCursor.getInt(dirtyCursor.getColumnIndex("id"));
                //删除ID小于minID的缓存数据
                String deleteSql = "DELETE FROM " + SPTableConstanct.TABLE_NAME_MESSAGE + " WHERE id < " + minId;
                dbwrite.execSQL(deleteSql);
            }
            ContentValues cv = new ContentValues();
            cv.put("message", message.getMessage());
            cv.put("msg_id", message.getMsgId());
            cv.put("title", message.getTitle());
            dbwrite.insert(SPTableConstanct.TABLE_NAME_MESSAGE, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbwrite.close();
        }
    }

    /**
     * 查询消息数据
     */
    public List<SPPushMessage> queryPushMesage() {
        List<SPPushMessage> list = new ArrayList<>();
        SQLiteDatabase dbwrite = dbHelper.getWritableDatabase();
        String[] columns = new String[]{"id", "title", "message", "msg_id", "receiver_time , is_read"};
        String orderBy = " receiver_time desc ";
        try {
            Cursor cursor = dbwrite.query(SPTableConstanct.TABLE_NAME_MESSAGE, columns, null, null, null, null, orderBy);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String message = cursor.getString(cursor.getColumnIndex("message"));
                    String receiverTime = cursor.getString(cursor.getColumnIndex("receiver_time"));
                    String msgId = cursor.getString(cursor.getColumnIndex("msg_id"));
                    int isRead = cursor.getInt(cursor.getColumnIndex("is_read"));
                    SPPushMessage pushMessage = new SPPushMessage(id, title, message, msgId, receiverTime, isRead);
                    list.add(pushMessage);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbwrite.close();
        }
        return list;
    }

}
