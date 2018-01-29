/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年11月3日 下午10:04:49
 * Description: 商品收藏列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.person;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author zw
 */
@EActivity(R.layout.activity_spoffline_alliance)
public class SPOfflineAllianceActivity extends SPBaseActivity {

    @ViewById(R.id.national_mall_image)
    ImageView nationalMallImage;

    @ViewById(R.id.local_mall_image)
    ImageView localMallImage;

    @ViewById(R.id.job_hunting_image)
    ImageView jobHuntingImage;

    @ViewById(R.id.coupon_center_image)
    ImageView couponCenterImage;

    @ViewById(R.id.local_news_image)
    ImageView localNewsImage;

    @ViewById(R.id.fight_groups_image)
    ImageView fightGroupsImage;

    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6;

    @Override
    protected void onCreate(Bundle bundle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(bundle);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        bitmap1 = ((BitmapDrawable) (nationalMallImage.getDrawable())).getBitmap();
        bitmap2 = ((BitmapDrawable) (localMallImage.getDrawable())).getBitmap();
        bitmap3 = ((BitmapDrawable) (jobHuntingImage.getDrawable())).getBitmap();
        bitmap4 = ((BitmapDrawable) (couponCenterImage.getDrawable())).getBitmap();
        bitmap5 = ((BitmapDrawable) (localNewsImage.getDrawable())).getBitmap();
        bitmap6 = ((BitmapDrawable) (fightGroupsImage.getDrawable())).getBitmap();
    }

    @Override
    public void initEvent() {
        fightGroupsImage.setOnTouchListener(new View.OnTouchListener() {
            float x;
            float y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        if (bitmap1.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                            nationalMallImage.setImageResource(R.drawable.national_mall_press);
                        } else if (bitmap2.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                            localMallImage.setImageResource(R.drawable.local_mall_press);
                        } else if (bitmap3.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                            jobHuntingImage.setImageResource(R.drawable.job_hunting_press);
                        } else if (bitmap4.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                            couponCenterImage.setImageResource(R.drawable.coupon_center_press);
                        } else if (bitmap5.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                            localNewsImage.setImageResource(R.drawable.local_news_press);
                        } else if (bitmap6.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                            fightGroupsImage.setImageResource(R.drawable.fight_groups_press);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        nationalMallImage.setImageResource(R.drawable.national_mall);
                        localMallImage.setImageResource(R.drawable.local_mall);
                        jobHuntingImage.setImageResource(R.drawable.job_hunting);
                        couponCenterImage.setImageResource(R.drawable.coupon_center);
                        localNewsImage.setImageResource(R.drawable.local_news);
                        fightGroupsImage.setImageResource(R.drawable.fight_groups);
                        if (Math.abs(event.getX() - x) > 10 || Math.abs(event.getY() - y) > 10)
                            break;
                        if (event.getX() > 0 && event.getY() > 0) {
                            if (event.getX() < bitmap1.getWidth() && event.getY() < bitmap1.getHeight()
                                    && bitmap1.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                                showToast("全国商城");
                            } else if (event.getX() < bitmap2.getWidth() && event.getY() < bitmap2.getHeight()
                                    && bitmap2.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                                showToast("本地商城");
                            } else if (event.getX() < bitmap3.getWidth() && event.getY() < bitmap3.getHeight()
                                    && bitmap3.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                                showToast("招聘求职");
                            } else if (event.getX() < bitmap4.getWidth() && event.getY() < bitmap4.getHeight()
                                    && bitmap4.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                                showToast("领券中心");
                            } else if (event.getX() < bitmap5.getWidth() && event.getY() < bitmap5.getHeight()
                                    && bitmap5.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                                showToast("本地新闻");
                            } else if (event.getX() < bitmap6.getWidth() && event.getY() < bitmap6.getHeight()
                                    && bitmap6.getPixel((int) (event.getX()), ((int) event.getY())) != 0) {
                                showToast("拼团商城");
                            }
                        }
                        break;
                }
                return false;
            }
        });
        fightGroupsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //不做任何处理,该方法必须
            }
        });
    }

    @Override
    public void initData() {
    }

    public void onBackBtnClick(View view) {
        finish();
    }

}
