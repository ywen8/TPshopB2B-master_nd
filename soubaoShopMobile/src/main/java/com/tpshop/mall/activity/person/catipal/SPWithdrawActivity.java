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
 * Date: @date 2017-5-1
 * Description: 资金管理 -》 提现
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.person.catipal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPCapitalRequest;
import com.tpshop.mall.utils.RandomCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_withdraw)
public class SPWithdrawActivity extends SPBaseActivity {

    @ViewById(R.id.txt_withdraw_card_number)
    EditText editCardNumber;

    @ViewById(R.id.txt_withdraw_card_ower)
    EditText editCardOwer;

    @ViewById(R.id.txt_withdraw_bank_balance)
    EditText editBalance;

    @ViewById(R.id.txt_withdraw_bank)
    EditText editBank;

    @ViewById(R.id.txt_withdraw_code)
    EditText editCode;

    @ViewById(R.id.txt_rand_code)
    ImageView imgCode;

    @ViewById(R.id.btn_submit)
    Button btnSubmit;

    String mCode;

    @Override
    protected void onCreate(Bundle arg0) {
        setCustomerTitle(true, true, getString(R.string.capital_withdraw));
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
        editBank.addTextChangedListener(textWatcher);
    }

    @Override
    public void initData() {
        editBalance.setHint(getString(R.string.withdraw_bank_balance_hint, SPMobileApplication.getInstance().getLoginUser().getUserMoney()));
        getVeridyCode();
    }

    private void getVeridyCode() {
        SPCapitalRequest.getVerifyCodeSuccess(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                mCode = (String) response;
                imgCode.setImageBitmap(RandomCode.getInstance().createBitmap(mCode));
            }
        }, new SPFailuredListener(SPWithdrawActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    public void onRandomCodeClick(View view) {
        getVeridyCode();
    }

    public void onSubmitClick(View view) {
        String cardNumber = editCardNumber.getText().toString();
        String cardOwer = editCardOwer.getText().toString();
        String cardBalance = editBalance.getText().toString();
        String cardBank = editBank.getText().toString();
        String inputCode = editCode.getText().toString();
        if (inputCode.trim().isEmpty()) {
            showToast("请输入图形验证码");
            return;
        }
        if (!inputCode.equalsIgnoreCase(mCode)) {
            showToast("图形验证码错误!");
            getVeridyCode();
            return;
        }
        SPCapitalRequest.postWithdraw(cardNumber, cardOwer, cardBank, cardBalance, mCode, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                showSuccessToast(msg);
                finish();
            }
        }, new SPFailuredListener(SPWithdrawActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
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
