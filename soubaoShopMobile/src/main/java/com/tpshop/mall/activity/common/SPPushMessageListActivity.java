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
 * Date: @date 2015年10月20日 下午7:52:58
 * Description:Activity 消息推送列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.common;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.tpshop.mall.R;
import com.tpshop.mall.adapter.SPPushMessageListAdapter;
import com.tpshop.mall.dao.SPPushMessageDao;
import com.tpshop.mall.model.SPPushMessage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.net.URL;
import java.util.List;

/**
 * Created by admin on 2016/6/29
 */
@EActivity(R.layout.push_message)
public class SPPushMessageListActivity extends SPBaseActivity {

    @ViewById(R.id.push_message_lstv)
    ListView messageLstv;

    SPPushMessageListAdapter mAdapter;
    List<SPPushMessage> mPushMessageLst;
    private final int MSG_CODE_REFRESH_DATA = 100;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE_REFRESH_DATA:
                    if (msg.obj != null) {
                        mPushMessageLst = (List<SPPushMessage>) msg.obj;
                        if (mAdapter != null) mAdapter.setData(mPushMessageLst);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_push_message));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        mAdapter = new SPPushMessageListAdapter(this);
        messageLstv.setAdapter(mAdapter);
        MessageQueryTask task = new MessageQueryTask();
        task.execute();
    }

    private class MessageQueryTask extends AsyncTask<URL, Integer, List<SPPushMessage>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<SPPushMessage> messages) {
            super.onPostExecute(messages);
            Message msg = mHandler.obtainMessage(MSG_CODE_REFRESH_DATA);
            msg.obj = messages;
            mHandler.sendMessage(msg);
        }

        @Override
        protected List<SPPushMessage> doInBackground(URL... params) {
            SPPushMessageDao messageDao = SPPushMessageDao.getInstance(SPPushMessageListActivity.this);
            return messageDao.queryPushMesage();
        }
    }

}


