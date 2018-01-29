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
 * Date: @date 2015年11月12日 下午8:08:13
 * Description: 店铺街道
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tpshop.mall.R;
import com.tpshop.mall.global.SPMobileApplication;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * @author 飞龙
 */
@EActivity(R.layout.image_preview)
public class SPImagePreviewActivity extends Activity {

    private int index;
    private List<String> imageUrls;

    @ViewById(R.id.image_pager)
    ViewPager mPager;

    @ViewById(R.id.pointer_layout)
    ViewGroup mPointerLayout;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        imageUrls = SPMobileApplication.getInstance().getImageUrls();
        index = getIntent().getIntExtra("index", 0);
    }

    @AfterViews
    public void init() {
        initSubViews();
    }

    public void initSubViews() {
        mPager = (ViewPager) findViewById(R.id.image_pager);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                if (imageUrls == null) return 0;
                return imageUrls.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(SPImagePreviewActivity.this);
                view.enable();
                view.setMaxScale(1.5f);
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(SPImagePreviewActivity.this).load(imageUrls.get(position)).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
                container.addView(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                onPageChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (imageUrls != null) buildPointer(mPointerLayout, imageUrls.size());
        mPager.setCurrentItem(index);
    }

    public void onPageChange(int page) {
        int count = mPointerLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = mPointerLayout.getChildAt(i);
            if (v instanceof ImageView) {
                if (i == page)
                    v.setBackgroundResource(R.drawable.ic_home_arrows_focus);
                else
                    v.setBackgroundResource(R.drawable.ic_home_arrows_normal);
            }
        }
    }

    /**
     * 构建轮播广告"圆点指示器"
     */
    public void buildPointer(ViewGroup pLayout, int count) {
        ImageView[] pointerImgv = new ImageView[count];
        pLayout.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);        //圆点指示器宽高
            lp.setMargins(8, 0, 8, 0);
            imageView.setLayoutParams(lp);
            imageView.setPadding(20, 0, 20, 0);
            pointerImgv[i] = imageView;
            if (i == 0)        //默认选中第一张图片
                pointerImgv[i].setBackgroundResource(R.drawable.ic_home_arrows_focus);
            else
                pointerImgv[i].setBackgroundResource(R.drawable.ic_home_arrows_normal);
            pLayout.addView(pointerImgv[i]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPMobileApplication.getInstance().setImageUrl(null);
    }

}
