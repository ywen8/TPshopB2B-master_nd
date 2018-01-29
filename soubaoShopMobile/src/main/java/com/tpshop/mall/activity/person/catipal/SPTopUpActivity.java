/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2127 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: Ben  wangqh01292@163.com
 * Date: @date 2017-4-25
 * Description: 资金管理 -》 充值
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.person.catipal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.activity.common.SPPayListActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_top_up)
public class SPTopUpActivity extends SPBaseActivity {

    @ViewById(R.id.btn_submit)
    Button mBtnSubmit;

    @ViewById(R.id.txt_top_up_money)
    EditText txtTopUpMoney;

    @ViewById(R.id.top_up_remark)
    EditText txtTopUpRemark;

    @Override
    protected void onCreate(Bundle arg0) {
        setCustomerTitle(true, true, getString(R.string.capital_top_up));
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
        txtTopUpMoney.addTextChangedListener(textWatcher);
    }

    @Override
    public void initData() {
    }

    public void onSubmitClick(View view) {
        String money = txtTopUpMoney.getText().toString();
        if (Double.parseDouble(money) <= 0) {
            showToast("充值金额有误!");
            txtTopUpMoney.setText("");
            return;
        }
        Intent payIntent = new Intent(this, SPPayListActivity_.class);
        payIntent.putExtra("account", money);
        startActivity(payIntent);
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
                mBtnSubmit.setEnabled(false);
            else
                mBtnSubmit.setEnabled(true);
        }
    };

}
