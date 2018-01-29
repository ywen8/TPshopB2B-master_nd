/*
 * FYRun
 * ============================================================================
 * * 版权所有 2015-2027 Ben，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: Ben on 2017/5/5
 * $description:
 */
package com.tpshop.mall.activity.person.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.person.SPUser;
import com.tpshop.mall.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_associated_account)
public class SPAssociatedAccountActivity extends SPBaseActivity implements View.OnClickListener {

    @ViewById(R.id.et_phone)
    EditText etPhone;

    @ViewById(R.id.et_pwd)
    EditText etPwd;

    @ViewById(R.id.bind_btn)
    Button bindBtn;

    @Override
    protected void onCreate(Bundle arg0) {
        setCustomerTitle(true, true, "绑定账户");
        super.onCreate(arg0);
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
        bindBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_btn:     //绑定
                bind();
                break;
        }
    }

    /**
     * 绑定账号
     */
    private void bind() {
        String account = etPhone.getText().toString();
        String pwd = etPwd.getText().toString();
        if (account.trim().isEmpty() || pwd.trim().isEmpty()) {
            showToast("账号或密码不能为空");
            return;
        }
        try {
            RequestParams params = new RequestParams();
            params.put("mobile", account);
            params.put("password", SPUtils.md5WithAuthCode(pwd));
            showLoadingSmallToast();
            SPUserRequest.bindAccount(params, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    showSuccessToast(msg);
                    if (response != null) {
                        SPUser user = (SPUser) response;
                        SPMobileApplication.getInstance().setLoginUser(user);
                        sendBroadcast(new Intent(SPMobileConstants.ACTION_LOGIN_CHNAGE));
                        Intent intent = new Intent(SPAssociatedAccountActivity.this, SPMainActivity.class);
                        intent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_PERSON);
                        startActivity(intent);
                    }
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    showFailedToast(msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
