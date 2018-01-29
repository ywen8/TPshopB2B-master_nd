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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_spchange_nickname)
public class SPChangeNickNameActivity extends SPBaseActivity {

    String resultData;

    @ViewById(R.id.ok_btn)
    Button btnSubmit;

    @ViewById(R.id.ed_nickname)
    EditText edNickname;

    @Override
    protected void onCreate(Bundle arg0) {
        setCustomerTitle(true, true, getString(R.string.change_nickname_title));
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
        edNickname.addTextChangedListener(textWatcher);
    }

    @Override
    public void initData() {
        String value = getIntent().getStringExtra("value");
        edNickname.setText(value);
    }

    public void onResultOkClick(View view) {
        resultData = edNickname.getText().toString();
        Intent data = new Intent();
        data.putExtra("value", resultData);
        setResult(RESULT_OK, data);
        finish();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0)
                btnSubmit.setEnabled(false);
            else
                btnSubmit.setEnabled(true);
        }
    };

}
