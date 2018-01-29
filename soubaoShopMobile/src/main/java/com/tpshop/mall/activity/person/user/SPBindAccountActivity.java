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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_bind_account)
public class SPBindAccountActivity extends SPBaseActivity implements View.OnClickListener {

    @ViewById(R.id.head_mimgv)
    SimpleDraweeView headImgv;

    @ViewById(R.id.account_type)
    TextView accountType;

    @ViewById(R.id.account_name)
    TextView accountName;

    @ViewById(R.id.regist_btn)
    Button registBtn;

    @ViewById(R.id.text1)
    TextView text1;

    @ViewById(R.id.text2)
    TextView text2;

    @ViewById(R.id.text3)
    TextView text3;

    @ViewById(R.id.bind_btn)
    Button bindBtn;

    @Override
    protected void onCreate(Bundle arg0) {
        setCustomerTitle(true, true, "关联账户");
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
        registBtn.setOnClickListener(this);
        bindBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        String from = getIntent().getStringExtra("from");
        String nickName = getIntent().getStringExtra("nickName");            //昵称
        String headPic = getIntent().getStringExtra("headPic");              //头像
        String platform = from.equals("wx") ? "微信" : "QQ";
        accountType.setText("亲爱的" + platform + "用户：");
        headImgv.setImageURI(SPUtils.getImageUri(this, headPic));
        accountName.setText(nickName);
        text1.setText("为了给您更好的服务，请关联一个" + getString(R.string.app_name) + "账号");
        text2.setText("还没有" + getString(R.string.app_name) + "账号？");
        text3.setText("已有" + getString(R.string.app_name) + "账号？");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist_btn:      //注册
                Intent intent = new Intent(this, SPRegisterActivity_.class);
                intent.putExtra("bind", true);
                startActivity(intent);
                break;
            case R.id.bind_btn:        //绑定
                startActivity(new Intent(this, SPAssociatedAccountActivity_.class));
                break;
        }
    }

}
