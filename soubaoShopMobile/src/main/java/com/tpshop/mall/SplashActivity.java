package com.tpshop.mall;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.soubao.tpshop.utils.SPCommonUtils;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.global.SPSaveData;

public class SplashActivity extends SPBaseActivity {

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        view = View.inflate(this, R.layout.activity_splash, null);
    }

    @Override
    public void initSubViews() {
    }

    @Override
    public void initEvent() {
    }

    public void onResume() {
        into();       //每次显示界面执行into方法
        super.onResume();
    }

    @Override
    public void initData() {
    }

    // 进入主程序的方法
    private void into() {
        if (SPCommonUtils.isNetworkAvaiable(this)) {
            if (SPMobileApplication.MAINANIM) {
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_main);
                view.startAnimation(animation);
                animation.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        intoActivity();
                    }
                });
            } else {
                intoActivity();
            }
        } else {
            showToast("无可用网络");
        }
    }

    private void intoActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SPMobileApplication.WELCOME) {
                    if (SPSaveData.getValue(SplashActivity.this, "First", true)) {
                        startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        SplashActivity.this.finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, SPMainActivity.class));
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        SplashActivity.this.finish();
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, SPMainActivity.class));
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    SplashActivity.this.finish();
                }
            }
        }, SPMobileApplication.SPLASHTIME);
    }

}

