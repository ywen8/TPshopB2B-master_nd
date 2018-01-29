/*
 * 性别选择
 * ============================================================================
 * * 版权所有 2015-2027 Ben，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: Ben on 2017/5/3
 * $description:
 */
package com.tpshop.mall.activity.person.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_user_gender)
public class SPUserGenderActivity extends SPBaseActivity {

    int selectIndex = -1;

    @ViewById(R.id.rad_male)
    RadioButton male;

    @ViewById(R.id.rad_female)
    RadioButton female;

    @Override
    protected void onCreate(Bundle arg0) {
        setCustomerTitle(true, true, getString(R.string.user_sex));
        super.onCreate(arg0);
        selectIndex = getIntent().getIntExtra("defaultIndex", -1);
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
        if (selectIndex == 2)
            female.setChecked(true);
        else
            male.setChecked(true);
    }

    public void onResultOkClick(View view) {
        selectIndex = male.isChecked() ? 0 : 1;
        Intent data = new Intent();
        data.putExtra("index", selectIndex);
        setResult(RESULT_OK, data);
        finish();
    }

}
