package com.tpshop.mall.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.soubao.tpshop.utils.SPJsonUtil;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.person.catipal.SPCapitalManageActivity_;
import com.tpshop.mall.activity.person.user.SPMessageCenterActivity_;
import com.tpshop.mall.activity.person.user.SPMessageNoticeFragmentActivity_;
import com.tpshop.mall.activity.shop.SPProductShowListActivity_;
import com.tpshop.mall.model.SPMessageListData;
import com.tpshop.mall.utils.SMobileLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * @author liuhao
 */
public class jPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                SMobileLog.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                SMobileLog.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processNotification(context, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                SMobileLog.e(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                SMobileLog.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                SMobileLog.e(TAG, "[MyReceiver] 用户点击打开了通知");
                Intent i = new Intent(context, SPMessageCenterActivity_.class);        //打开自定义的Activity
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                SMobileLog.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                SMobileLog.e(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                SMobileLog.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processNotification(Context context, Bundle bundle) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        String extras = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        SPMessageListData data = null;
        int status = -1;
        if (!ExampleUtil.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                status = extraJson.getInt("category");
                if (status >= 0)
                    data = SPJsonUtil.fromJsonToModel(extraJson.getJSONObject("data"), SPMessageListData.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (data != null) {
            mBuilder.setContentTitle(data.getMessageDataTitle())
                    .setContentText(data.getMessageDataDiscription())
                    .setTicker(data.getMessageDataTitle())            //通知首次出现在通知栏,带上升动画效果的
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setAutoCancel(true)           //设置这个标志当用户单击面板就可以让通知将自动取消
                    //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置,使用defaults属性,可以组合
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.appicon);
            Intent appIntent;
            if (status == 2) {
                if (data.getMessageDataGoodsId() != null) {
                    appIntent = new Intent(context, SPProductShowListActivity_.class);
                    appIntent.putExtra("goodsId", data.getMessageDataGoodsId());
                    appIntent.setAction(Intent.ACTION_MAIN);
                    appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);          //设置启动模式
                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(contentIntent);
                }
            }
            if (status == 4) {
                String changeType = data.getMessageDataChangeType();
                if (changeType != null) {
                    switch (changeType) {
                        case "1": {        //积分
                            appIntent = new Intent(context, SPCapitalManageActivity_.class);
                            appIntent.putExtra("goodsId", data.getMessageDataGoodsId());
                            appIntent.setAction(Intent.ACTION_MAIN);
                            appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);           //设置启动模式
                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(contentIntent);
                            break;
                        }
                        case "2": {        //余额
                            appIntent = new Intent(context, SPCapitalManageActivity_.class);
                            appIntent.putExtra("goodsId", data.getMessageDataGoodsId());
                            appIntent.setAction(Intent.ACTION_MAIN);
                            appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);           //设置启动模式
                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(contentIntent);
                            break;
                        }
                        case "3": {        //优惠券
                            appIntent = new Intent(context, SPProductShowListActivity_.class);
                            appIntent.putExtra("goodsId", data.getMessageDataGoodsId());
                            appIntent.setAction(Intent.ACTION_MAIN);
                            appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);           //设置启动模式
                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(contentIntent);
                            break;
                        }
                    }
                }
            }
            if (status == 0) {
                Intent show = new Intent(context, SPMessageNoticeFragmentActivity_.class);
                show.putExtra("fragmentIndex", status);
                show.addCategory(Intent.CATEGORY_LAUNCHER);
                show.setAction(Intent.ACTION_MAIN);
                show.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);           //设置启动模式
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, show, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);
            }
            if (data.getMessageDataCover() != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(data.getMessageDataCover());
                mBuilder.setLargeIcon(bitmap);
            }
            mNotificationManager.notify(5270, mBuilder.build());
        }
    }

    public static Drawable loadImageFromUrl(String url) {
        URL m;
        InputStream i = null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Drawable.createFromStream(i, "src");
    }

    //打印所有的intent extra数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    SMobileLog.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}
