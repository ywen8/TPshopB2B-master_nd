/*
 * 找回密码
 * ============================================================================
 * * 版权所有 2015-2027 Ben，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: Ben on 2017/5/3
 * $description: 登录 -》 找回密码
 */
package com.tpshop.mall.activity.person.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPCapitalRequest;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.person.SPUser;
import com.tpshop.mall.utils.RandomCode;
import com.tpshop.mall.utils.SPServerUtils;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.SwitchButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.find_password_activity)
public class SPFindPasswordActivity extends SPBaseActivity {

    private Context mContext;
    private String mPhoneNumber = "";
    private String strRandomCode = "";
    private CheckCodeCountTimer mCountDownTimer;

    @ViewById(R.id.txt_rand_code)
    ImageView randomCodeView;

    @ViewById(R.id.account_name)
    EditText txtAccountName;

    @ViewById(R.id.check_code_edtv)
    EditText txtRandCode;

    @ViewById(R.id.layout_first)
    RelativeLayout mLayoutFirst;

    @ViewById(R.id.layout_second)
    RelativeLayout mLayoutSecond;

    @ViewById(R.id.layout_third)
    RelativeLayout mLayoutThird;

    @ViewById(R.id.btn_first_next)
    Button btnNextOne;

    @ViewById(R.id.btn_second_next)
    Button btnNextTwo;

    @ViewById(R.id.btn_done)
    Button btnDone;

    @ViewById(R.id.txt_phone_number)
    TextView txtPhoneNumber;

    @ViewById(R.id.txt_account_name)
    TextView txtAccountNumber;

    @ViewById(R.id.edit_verification_code)
    EditText txtVerificationCode;

    @ViewById(R.id.btn_verification_code)
    Button btnVerification;

    @ViewById(R.id.btn_show_pwd)
    SwitchButton btnShowPwd;

    @ViewById(R.id.txt_new_pwd)
    EditText txtNewPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.login_forget_pwd));
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        txtAccountName.addTextChangedListener(textWatcher);
        txtRandCode.addTextChangedListener(textWatcher);
        txtVerificationCode.addTextChangedListener(textWatcherSecond);
        txtNewPwd.addTextChangedListener(textWatcherDone);
    }

    @Override
    public void initEvent() {
        btnShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    txtNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                else
                    txtNewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
    }

    @Override
    public void initData() {
        getVeridyCode();
    }

    private void getVeridyCode() {
        SPCapitalRequest.getVerifyCodeSuccess(new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                strRandomCode = (String) response;
                randomCodeView.setImageBitmap(RandomCode.getInstance().createBitmap(strRandomCode));
            }
        }, new SPFailuredListener(SPFindPasswordActivity.this) {
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
            if (txtAccountName.getText().length() == 0 || txtRandCode.getText().length() == 0)
                btnNextOne.setEnabled(false);
            else
                btnNextOne.setEnabled(true);
        }
    };

    private TextWatcher textWatcherSecond = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0)
                btnNextTwo.setEnabled(false);
            else
                btnNextTwo.setEnabled(true);
        }
    };

    private TextWatcher textWatcherDone = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0)
                btnDone.setEnabled(false);
            else
                btnDone.setEnabled(true);
        }
    };

    public void onClearRandomClick(View view) {
        txtRandCode.setText("");
    }

    public void onClearVerificationClick(View view) {
        txtVerificationCode.setText("");
    }

    public void onRandomCodeClick(View view) {
        getVeridyCode();
    }

    public void onFirstNextClick(View view) {
        String strAccount = txtAccountName.getText().toString();
        String inputCode = txtRandCode.getText().toString();
        if (inputCode.trim().isEmpty()) {
            showToast("请输入图形验证码");
            return;
        }
        if (!strRandomCode.equalsIgnoreCase(inputCode)) {
            showToast("图形验证码错误!");
            getVeridyCode();
            return;
        }
        SPUserRequest.forgetPasswordInfo(strAccount, inputCode, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                SPUser user = (SPUser) response;
                mPhoneNumber = user.getMobile();
                initSecondInterface(user.getNickName());
            }
        }, new SPFailuredListener(SPFindPasswordActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    private void initSecondInterface(String accountName) {
        int mSmsTimeOut = SPServerUtils.getSmsTimeOut();
        mCountDownTimer = new CheckCodeCountTimer(mSmsTimeOut * 1000, 1000);
        txtPhoneNumber.setText(getString(R.string.find_pwd_second_title, mPhoneNumber));
        txtAccountNumber.setText(getString(R.string.find_pwd_account_name, accountName));
        mLayoutFirst.setVisibility(View.GONE);
        mLayoutSecond.setVisibility(View.VISIBLE);
    }

    //发送短信验证码
    public void onBtnVerificationClick(View view) {
        setSendSmsButtonStatus(false);
        if (SPMobileConstants.ENABLE_SMS_CODE) {      //启用短信验证码
            String scene = "2";
            SPUserRequest.sendSmsValidateCode(mPhoneNumber, scene, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    showSuccessToast(msg);
                    mCountDownTimer.start();
                    setSendSmsButtonStatus(false);
                }
            }, new SPFailuredListener(SPFindPasswordActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    showFailedToast(msg);
                    setSendSmsButtonStatus(true);
                }
            });
        } else {      //未启用短信验证码
            setSendSmsButtonStatus(false);
            txtVerificationCode.setText("1234");
        }
    }

    private class CheckCodeCountTimer extends CountDownTimer {
        CheckCodeCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnVerification.setText(getString(R.string.register_btn_re_code, millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            btnVerification.setText(getString(R.string.register_btn_re_code_done));
            setSendSmsButtonStatus(true);
        }
    }

    /**
     * 修改发送短信验证码状态
     */
    public void setSendSmsButtonStatus(boolean enable) {
        if (enable)      //启用
            btnVerification.setEnabled(true);
        else      //禁用
            btnVerification.setEnabled(false);
    }

    public void onSecondNextClick(View view) {
        mLayoutFirst.setVisibility(View.GONE);
        mLayoutSecond.setVisibility(View.GONE);
        mLayoutThird.setVisibility(View.VISIBLE);
    }

    public void onBtnDoneClick(View view) {
        String pwd = txtNewPwd.getText().toString();
        String checkCode = txtVerificationCode.getText().toString();
        RequestParams params = new RequestParams();
        try {
            String md5pwd = SPUtils.md5WithAuthCode(pwd);
            params.put("mobile", mPhoneNumber);
            params.put("password", md5pwd);
            params.put("check_code", checkCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SPUserRequest.forgetPassword(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                showToast(msg);
                Intent intent = new Intent(mContext, SPLoginActivity_.class);        //忘记密码,跳到登录页面
                startActivity(intent);
                finish();
            }
        }, new SPFailuredListener(SPFindPasswordActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

}
