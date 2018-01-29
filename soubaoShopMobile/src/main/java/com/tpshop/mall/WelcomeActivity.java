package com.tpshop.mall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.tpshop.mall.adapter.SPWelcomePagerAdapter;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.global.SPSaveData;

import java.util.ArrayList;

public class WelcomeActivity extends Activity implements OnPageChangeListener, OnClickListener {

    private int[] images;
    private int page = 0;
    private int pageState;
    private Animation shakeAnim;
    private ImageButton startButton;
    private ImageView[] indicators = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        images = SPMobileApplication.IMAGES;
        initView();
    }

    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpage);
        startButton = (ImageButton) findViewById(R.id.start_Button);
        shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
        shakeAnim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (page == indicators.length - 1)
                    into();       //动画结束自动进入
            }
        });
        startButton.setOnClickListener(this);
        LinearLayout indicatorLayout = (LinearLayout) findViewById(R.id.indicator);
        ArrayList<View> views = new ArrayList<>();
        indicators = new ImageView[images.length];            //定义指示器数组大小
        for (int i = 0; i < images.length; i++) {             //循环加入图片
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(images[i]);
            views.add(imageView);
            indicators[i] = new ImageView(this);              //循环加入指示器
            if (i == 0) {
                indicators[i].setBackgroundResource(R.drawable.ic_indicators_now);
            } else {
                indicators[i].setBackgroundResource(R.drawable.ic_indicators_default);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(15, 0, 0, 0);
                indicators[i].setLayoutParams(layoutParams);
            }
            indicatorLayout.addView(indicators[i]);
        }
        PagerAdapter pagerAdapter = new SPWelcomePagerAdapter(views);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_Button:
                into();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        pageState = arg0;
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (arg0 == images.length - 1) {
            if (arg2 == 0 && pageState == 1) {            //已经在最后一页还想往右划
                pageState = 0;
                into();
            }
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        page = arg0;
        if (arg0 == indicators.length - 1) {             //显示最后一个图片时显示按钮
            startButton.setVisibility(View.VISIBLE);
            startButton.startAnimation(shakeAnim);
        } else {
            startButton.setVisibility(View.INVISIBLE);
            startButton.clearAnimation();
        }
        for (int i = 0; i < indicators.length; i++) {         //更改指示器图片
            indicators[arg0].setBackgroundResource(R.drawable.ic_indicators_now);
            if (arg0 != i)
                indicators[i].setBackgroundResource(R.drawable.ic_indicators_default);
        }
    }

    public void into() {
        SPSaveData.putValue(this, "First", false);
        startActivity(new Intent(WelcomeActivity.this, SPMainActivity.class));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        this.finish();
    }

}

