/*
 * tpshop
 * ============================================================================
 * * 版权所有 2015-2027 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: Ben  16/07/08
 * $description: 登录
 */
package com.tpshop.mall.activity.person.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.person.SPUser;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

@EActivity(R.layout.activity_splogin)
public class SPLoginActivity extends SPBaseActivity {

    SHARE_MEDIA platform;                        //第三方平台
    String fromActivity = "";                    //跳转路径,从其他activity跳转到登录页面的路径,如果该值存在则进入原来页面,否则跳转到主页面
    boolean mPwdOpen = false;
    private UMShareAPI mShareAPI = null;
    public static String KEY_FROM = "from";

    @ViewById(R.id.edit_phone_num)
    EditText txtPhoneNum;

    @ViewById(R.id.edit_password)
    EditText txtPassword;

    @ViewById(R.id.btn_login)
    Button btnLogin;

    @ViewById(R.id.txt_register)
    TextView txtRegister;

    @ViewById(R.id.txt_forget_pwd)
    TextView txtForgetPwd;

    @ViewById(R.id.test_account_txtv)
    TextView txtTestAccount;

    @ViewById(R.id.test_pwd_txtv)
    TextView txtTestPwd;

    @ViewById(R.id.qq_icon_txt)
    TextView qqIconTxt;

    @ViewById(R.id.wx_icon_txt)
    TextView wxIconTxt;

    @ViewById(R.id.wx_layout)
    View wxView;

    @ViewById(R.id.qq_layout)
    View qqView;

    @ViewById(R.id.img_view_pwd)
    ImageButton imgViewPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, getString(R.string.login_title));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        mBackImgv.setBackgroundResource(R.drawable.title_close_normal);
        setTitleBarLienHide(true);
    }

    @Override
    public void initEvent() {
        txtPhoneNum.addTextChangedListener(textWatcherDone);
        txtPassword.addTextChangedListener(textWatcherDone);
    }

    @Override
    public void initData() {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);      //每次都拉取授权信息
        mShareAPI = UMShareAPI.get(this);
        mShareAPI.setShareConfig(config);
        if (!SPMobileConstants.IsRelease) {
            txtPhoneNum.setText("15889560679");
            txtPassword.setText("123456");
        }
        fromActivity = getIntent().getStringExtra(KEY_FROM);
    }

    public void onLoginClick(View view) {
        if (SPStringUtils.isEditEmpty(txtPhoneNum)) {
            showToast(getString(R.string.login_phone_number_null));
            return;
        }
        if (SPStringUtils.isEditEmpty(txtPassword)) {
            showToast(getString(R.string.login_password_null));
            return;
        }
        showLoadingSmallToast();
        String pushId = JPushInterface.getRegistrationID(this);
        SPUserRequest.doLogin(txtPhoneNum.getText().toString(), txtPassword.getText().toString(), pushId,
                new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingSmallToast();
                        if (response != null) {
                            showSuccessToast("登录成功");
                            SPUser user = (SPUser) response;
                            SPMobileApplication.getInstance().setLoginUser(user);
                            sendBroadcast(new Intent(SPMobileConstants.ACTION_LOGIN_CHNAGE));
                            loginSuccess();
                        }
                    }
                }, new SPFailuredListener(SPLoginActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingSmallToast();
                        showFailedToast(msg);
                    }
                });
    }

    private void loginSuccess() {
        if (SPStringUtils.isEmpty(fromActivity)) {
            Intent intent = new Intent();
            intent.setClass(this, SPMainActivity.class);
            startActivity(intent);
            return;
        }
        finish();
    }

    public void onRegisterClick(View view) {
        startActivity(new Intent(this, SPRegisterActivity_.class));
    }

    public void onForgetPwdClick(View view) {
        startActivity(new Intent(this, SPFindPasswordActivity_.class));
    }

    @Click({R.id.qq_icon_txt, R.id.wx_icon_txt, R.id.img_view_pwd})
    public void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.qq_icon_txt:
                loginWithQQ();
                break;
            case R.id.wx_icon_txt:
                loginWithWeiXin();
                break;
            case R.id.img_view_pwd:
                if (mPwdOpen) {
                    imgViewPwd.setImageResource(R.drawable.icon_secrecy_pwd);
                    txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPwdOpen = false;
                } else {
                    imgViewPwd.setImageResource(R.drawable.icon_open_pwd);
                    txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPwdOpen = true;
                }
                break;
        }
    }

    /**
     * QQ第三方登录
     */
    public void loginWithQQ() {
        platform = SHARE_MEDIA.QQ;
        boolean isInstall = mShareAPI.isInstall(this, platform);
        if (!isInstall) {
            showToast("请先安装QQ!");
            return;
        }
        mShareAPI.getPlatformInfo(this, platform, umAuthListener);
    }

    /**
     * 微信第三方登录
     */
    public void loginWithWeiXin() {
        platform = SHARE_MEDIA.WEIXIN;
        if (!mShareAPI.isInstall(this, platform)) {
            showToast("请先安装微信!");
            return;
        }
        mShareAPI.getPlatformInfo(this, platform, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            thirdLoginWithMap(map);      //授权成功,获取用户信息
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast("授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast("取消授权");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 第三方登录
     */
    public void thirdLoginWithMap(Map<String, String> userMap) {
        final String nickName = userMap.get("screen_name");                      //昵称
        String gender = getGenderByCn(userMap.get("gender"));                    //性别
        final String headPic = userMap.get("profile_image_url");                 //头像
        String openid = userMap.get("openid");
        final String from = (platform == SHARE_MEDIA.WEIXIN ? "wx" : "qq");      //平台
        String unionid = userMap.get("unionid");
        String pushId = JPushInterface.getRegistrationID(this);
        showLoadingSmallToast();
        SPUserRequest.loginWithThirdPart(openid, unionid, from, nickName, headPic, gender, pushId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                if (response != null) {
                    SPUser user = (SPUser) response;
                    SPMobileApplication.getInstance().setLoginUser(user);
                    SPLoginActivity.this.sendBroadcast(new Intent(SPMobileConstants.ACTION_LOGIN_CHNAGE));
                    showSuccessToast("登录成功");
                    loginSuccess();
                }
            }
        }, new SPFailuredListener(SPLoginActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                if (errorCode == 100) {
                    Intent intent = new Intent(SPLoginActivity.this, SPBindAccountActivity_.class);
                    intent.putExtra("from", from);
                    intent.putExtra("headPic", headPic);
                    intent.putExtra("nickName", nickName);
                    startActivity(intent);
                } else {
                    showFailedToast(msg);
                }
            }
        });
    }

    public String getGenderByCn(String gender) {
        switch (gender) {
            case "男":
                return "1";
            case "女":
                return "2";
            default:
                return gender;
        }
    }

    private TextWatcher textWatcherDone = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (txtPhoneNum.getText().length() == 0 || txtPassword.getText().length() == 0)
                btnLogin.setEnabled(false);
            else
                btnLogin.setEnabled(true);
        }
    };

}
