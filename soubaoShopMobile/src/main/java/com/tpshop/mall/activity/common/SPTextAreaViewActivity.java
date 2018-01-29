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
 * Description:Activity 公共类, 获取文本
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 2016/6/28
 */
@EActivity(R.layout.text_area)
public class SPTextAreaViewActivity extends SPBaseActivity {

    int maxLimit = 50;

    @ViewById(R.id.value_edtv)
    EditText valueEtv;

    @ViewById(R.id.limit_txtv)
    TextView limitTxtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "填写内容");
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
        String value = getIntent().getStringExtra("content");
        valueEtv.setText(value);
        valueEtv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int c = s.length();
                if (null != valueEtv && c > maxLimit) {
                    String txt = s.toString();
                    int len = txt.length();
                    String cutTxt = txt.substring(0, len - 1);
                    valueEtv.setText(cutTxt);
                    valueEtv.setSelection(valueEtv.getText().length() - 1);      //设置光标位置
                } else {
                    limitTxtv.setText(c + "/" + maxLimit);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Click({R.id.ok_btn})
    public void onButtonClick(View v) {
        if (v.getId() == R.id.ok_btn) {
            String value = valueEtv.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("content", value);
            setResult(SPMobileConstants.Result_Code_GetSelerMessage, intent);
            finish();
        }
    }

}
