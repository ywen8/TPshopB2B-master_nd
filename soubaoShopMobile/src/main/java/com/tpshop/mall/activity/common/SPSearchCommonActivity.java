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
 * Description:Activity 支付列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tpshop.mall.activity.shop.SPProductListSearchResultActivity;
import com.tpshop.mall.adapter.SPSearchkeyListAdapter;
import com.tpshop.mall.global.SPSaveData;
import com.tpshop.mall.utils.SPServerUtils;
import com.tpshop.mall.widget.SPSearchView;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.soubao.tpshop.utils.SPStringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/29
 */
@EActivity(R.layout.common_search)
public class SPSearchCommonActivity extends SPBaseActivity implements SPSearchView.SPSearchViewListener {

    SPSearchkeyListAdapter mAdapter;
    List<String> mSearchkeys = new ArrayList<>();

    @ViewById(R.id.search_delete_btn)
    Button deleteBtn;

    @ViewById(R.id.search_hotkey_lyaout)
    LinearLayout buttonGallery;

    @ViewById(R.id.search_key_listv)
    ListView searchkeyListv;

    @ViewById(R.id.search_view)
    SPSearchView searchView;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SPMobileConstants.MSG_CODE_SEARCHKEY:
                    startSearch(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        searchView.getSearchEditText().setFocusable(true);
        searchView.setSearchViewListener(this);
    }

    @Override
    public void initEvent() {
        searchkeyListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = (String) mAdapter.getItem(position);
                startSearch(key);
            }
        });
        if (searchView.getSearchEditText() != null) {
            searchView.getSearchEditText().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER) {
                        String searchKey = searchView.getSearchEditText().getText().toString();
                        startSearch(searchKey);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView.getSearchEditText() != null) {
            searchView.getSearchEditText().setFocusable(true);
            searchView.getSearchEditText().setFocusableInTouchMode(true);
        }
    }

    @Override
    public void initData() {
        buildProductButtonGallery(buttonGallery);
        mAdapter = new SPSearchkeyListAdapter(this);
        searchkeyListv.setAdapter(mAdapter);
        loadKey();
        mAdapter.setData(mSearchkeys);
    }

    private void buildProductButtonGallery(LinearLayout gallery) {
        List<String> hotKeys = SPServerUtils.getHotKeyword();
        if (hotKeys != null && hotKeys.size() > 0) {
            for (int i = 0; i < hotKeys.size() + 1; i++) {
                if (i == 0) {
                    View txtView = LayoutInflater.from(this).inflate(R.layout.text_gallery_item, gallery, false);
                    gallery.addView(txtView);
                } else {
                    View view = LayoutInflater.from(this).inflate(R.layout.order_button_gallery_item, gallery, false);
                    Button button = (Button) view.findViewById(R.id.id_index_gallery_item_button);
                    button.setText(hotKeys.get(i - 1));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v instanceof Button) {
                                Button hotBtn = (Button) v;
                                if (mHandler != null) {
                                    Message msg = mHandler.obtainMessage(SPMobileConstants.MSG_CODE_SEARCHKEY);
                                    msg.obj = hotBtn.getText();
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }
                    });
                    button.setBackgroundResource(R.drawable.tag_button_bg_unchecked);
                    gallery.addView(view);
                }
            }
        }
    }

    @Click({R.id.search_delete_btn})
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.search_delete_btn:      //清空搜索历史
                if (searchView.getSearchEditText() != null)
                    searchView.getSearchEditText().setText("");
                SPSaveData.putValue(this, SPMobileConstants.KEY_SEARCH_KEY, "");
                loadKey();
                mAdapter.setData(mSearchkeys);
                break;
        }
    }

    @Override
    public void onBackClick() {
        this.finish();
    }

    @Override
    public void onSearchBoxClick(String keyword) {
        startSearch(keyword);
    }

    public void loadKey() {
        mSearchkeys.clear();
        String searchKey = SPSaveData.getString(this, SPMobileConstants.KEY_SEARCH_KEY);
        if (!SPStringUtils.isEmpty(searchKey)) {
            String[] keys = searchKey.split(",");
            if (keys != null) {
                for (String key : keys) {
                    if (!key.isEmpty())
                        mSearchkeys.add(key);
                }
            }
        }
    }

    public void saveKey(String key) {
        String searchKey = SPSaveData.getString(this, SPMobileConstants.KEY_SEARCH_KEY);
        if (!searchKey.isEmpty() && !searchKey.contains(key)) {
            searchKey = key + "," + searchKey;
        } else if (SPStringUtils.isEmpty(searchKey)) {
            searchKey = key;
        }
        SPSaveData.putValue(this, SPMobileConstants.KEY_SEARCH_KEY, searchKey);
    }

    public void startSearch(String searchKey) {
        if (!SPStringUtils.isEmpty(searchKey)) saveKey(searchKey);
        Intent intent = new Intent(this, SPProductListSearchResultActivity.class);
        intent.putExtra("searchKey", searchKey);
        startActivity(intent);
    }

}
