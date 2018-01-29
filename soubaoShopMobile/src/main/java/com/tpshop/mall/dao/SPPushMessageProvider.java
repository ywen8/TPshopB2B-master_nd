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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.common.SPTableConstanct;
import com.tpshop.mall.model.SPPushMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/27
 */
public class SPPushMessageProvider extends ContentProvider {

    private static final int CODE_PARAM = 2;
    private SPMobileDBHelper dbHelper = null;
    private static final int CODE_NOPARAM = 1;                                                 // 匹配码
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);             // 若不匹配采用UriMatcher.NO_MATCH(-1)返回

    static {
        //对等待匹配的URI进行匹配操作,必须符合cn.xyCompany.providers.personProvider/person,格式匹配返回CODE_NOPARAM,不匹配返回-1
        MATCHER.addURI("com.tpshop.mall.dao.messageProvider", SPTableConstanct.TABLE_NAME_MESSAGE, CODE_NOPARAM);
        //#表示数字cn.xyCompany.providers.personProvider/person/10,匹配返回CODE_PARAM,不匹配返回-1
        MATCHER.addURI("com.tpshop.mall.dao.messageProvider", SPTableConstanct.TABLE_NAME_MESSAGE + "/#", CODE_PARAM);
    }

    @Override
    public boolean onCreate() {
        this.dbHelper = new SPMobileDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        qb.setTables(SPTableConstanct.TABLE_NAME_MESSAGE);
        Cursor c = qb.query(db, projection, selection, null, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //添加之前,删除之前缓存之外的数据
        String dirtySql = "SELECT " + SPTableConstanct.TABLE_NAME_MESSAGE + " FROM " + SPTableConstanct.TABLE_NAME_MESSAGE +
                " ORDER BY id DESC LIMIT " + SPMobileConstants.CacheMessageCount + " OFFSET 0";
        Cursor dirtyCursor = db.rawQuery(dirtySql, null);
        if (dirtyCursor.getCount() > SPMobileConstants.CacheMessageCount && dirtyCursor.moveToLast()) {
            //按ID降序排序,查找SPMobileConstants.CacheMessageCount条之内的最小ID
            int minId = dirtyCursor.getInt(dirtyCursor.getColumnIndex("id"));
            //删除ID小于minID的缓存数据
            String deleteSql = "DELETE FROM " + SPTableConstanct.TABLE_NAME_MESSAGE + " WHERE id < " + minId;
            db.execSQL(deleteSql);
        }
        long rowId = db.insert(SPTableConstanct.TABLE_NAME_MESSAGE, "", values);
        if (rowId > 0) {
            Uri rowUri = ContentUris.appendId(SPPushMessageTable.PushMessageColumn.CONTENT_URI.buildUpon(), rowId).build();
            getContext().getContentResolver().notifyChange(rowUri, null);
            return rowUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public void insertMessage(SPPushMessage message) {
        if (message == null) return;
        SQLiteDatabase dbwrite = null;
        try {
            dbwrite = dbHelper.getWritableDatabase();
            //添加之前,删除之前缓存之外的数据
            String dirtySql = "SELECT " + SPTableConstanct.TABLE_NAME_MESSAGE + " FROM " + SPTableConstanct.TABLE_NAME_MESSAGE +
                    " ORDER BY id DESC LIMIT " + SPMobileConstants.CacheMessageCount + " OFFSET 0";
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
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String message = cursor.getString(cursor.getColumnIndex("message"));
                    String receiverTime = cursor.getString(cursor.getColumnIndex("receiver_time"));
                    String msgId = cursor.getString(cursor.getColumnIndex("msg_id"));
                    int isRead = cursor.getInt(cursor.getColumnIndex("is_read"));
                    SPPushMessage pushMessage = new SPPushMessage(id, title, message, msgId, receiverTime, isRead);
                    list.add(pushMessage);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbwrite.close();
        }
        return list;
    }

}
