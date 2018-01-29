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
 * Description: 店铺简介
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPStoreRequest;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author 飞龙
 */
@EActivity(R.layout.store_about)
public class SPStoreAboutActivity extends SPBaseActivity {

    private int mStoreId;
    private SPStore mStore;

    @ViewById(R.id.about_scrollv)
    ScrollView aboutScrollv;

    @ViewById(R.id.store_logo_imgv)
    ImageView logoImgv;            //品牌logo

    @ViewById(R.id.store_name_txtv)
    TextView storeNameTxtv;        //店铺名称

    @ViewById(R.id.seller_name_txtv)
    TextView sellerNameTxtv;       //卖家名称

    @ViewById(R.id.collect1_btn)
    Button collect1Btn;            //关注

    @ViewById(R.id.collect2_btn)
    Button collect2Btn;            //已关注

    @ViewById(R.id.seller_count_txtv)
    TextView sellerCountTxtv;      //销量

    @ViewById(R.id.seller_tel)
    TextView sellerTel;            //商家电话

    @ViewById(R.id.store_count_txtv)
    TextView storeCount;           //全部商品

    @ViewById(R.id.new_txtv)
    TextView newTxtv;              //上新商品

    @ViewById(R.id.hot_txtv)
    TextView hotTxtv;              //热销商品

    @ViewById(R.id.value_location_txtv)
    TextView locationTxtv;         //所在地

    @ViewById(R.id.value_time_txtv)
    TextView timeTxtv;             //开店时间

    @ViewById(R.id.company_name_txt)
    TextView companyName;          //公司名称

    @ViewById(R.id.desccredit_txt)
    TextView desccreditTxt;

    @ViewById(R.id.imageView2)
    ImageView imageView2;

    @ViewById(R.id.servicecredit_txt)
    TextView servicecreditTxt;

    @ViewById(R.id.imageView3)
    ImageView imageView3;

    @ViewById(R.id.deliverycredit_txt)
    TextView deliverycreditTxt;

    @ViewById(R.id.imageView4)
    ImageView imageView4;

    @Override
    protected void onCreate(Bundle bundle) {
        setCustomerTitle(true, true, getString(R.string.title_store_about));
        super.onCreate(bundle);
        if (getIntent() != null && getIntent().hasExtra("storeId"))
            mStoreId = getIntent().getIntExtra("storeId", 0);
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
        showLoadingSmallToast();
        SPStoreRequest.getStoreAbout(String.valueOf(mStoreId), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                if (response != null) {
                    mStore = (SPStore) response;
                    refreshView();
                    aboutScrollv.setVisibility(View.VISIBLE);
                }
            }
        }, new SPFailuredListener(SPStoreAboutActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    public void refreshView() {
        if (mStore == null) return;
        if (!SPStringUtils.isEmpty(mStore.getStoreLogo()))
            Glide.with(this).load(SPUtils.getImageUrl(mStore.getStoreLogo())).asBitmap().fitCenter().placeholder(R.drawable.category_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(logoImgv);
        desccreditTxt.setText(mStore.getDesccredit() + "分");
        servicecreditTxt.setText(mStore.getServicecredit() + "分");
        deliverycreditTxt.setText(mStore.getDeliverycredit() + "分");
        if (mStore.getDesccredit() >= 5)
            imageView2.setVisibility(View.VISIBLE);
        else
            imageView2.setVisibility(View.GONE);
        if (mStore.getServicecredit() >= 5)
            imageView3.setVisibility(View.VISIBLE);
        else
            imageView3.setVisibility(View.GONE);
        if (mStore.getDeliverycredit() >= 5)
            imageView4.setVisibility(View.VISIBLE);
        else
            imageView4.setVisibility(View.GONE);
        if (!SPStringUtils.isEmpty(mStore.getStoreName()))                         //店铺名称
            storeNameTxtv.setText(mStore.getStoreName());
        if (!SPStringUtils.isEmpty(mStore.getStoreZy()))                           //店铺简介
            sellerNameTxtv.setText(mStore.getStoreZy());
        sellerCountTxtv.setText("已有" + mStore.getStoreCollect() + "人关注");      //关注量
        if (!SPStringUtils.isEmpty(mStore.getStoreAddress()))                      //所在地
            locationTxtv.setText(mStore.getStoreAddress());
        if (!SPStringUtils.isEmpty(mStore.getSellerTel()))                         //商家电话
            sellerTel.setText(mStore.getSellerTel());
        storeCount.setText(mStore.getTotalGoods() + "\n全部商品");
        newTxtv.setText(mStore.getNewGoods() + "\n上新");
        hotTxtv.setText(mStore.getHotGoods() + "\n热销");
        if (!SPStringUtils.isEmpty(mStore.getStoreTime()))                         //开店时间
            timeTxtv.setText(SPUtils.convertFullTimeFromPhpTime(Long.valueOf(mStore.getStoreTime())));
        if (!SPStringUtils.isEmpty(mStore.getCompanyName()))                       //公司名称
            companyName.setText(mStore.getCompanyName());
        if (mStore.getIsCollect() == 1) {      //关注
            collect1Btn.setVisibility(View.GONE);
            collect2Btn.setVisibility(View.VISIBLE);
        } else {      //未关注
            collect1Btn.setVisibility(View.VISIBLE);
            collect2Btn.setVisibility(View.GONE);
        }
    }

    @Click({R.id.store_count_txtv, R.id.new_txtv, R.id.hot_txtv, R.id.collect1_btn, R.id.collect2_btn, R.id.service_online_rl, R.id.seller_tel_rl})
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.collect1_btn:
            case R.id.collect2_btn:
                collectOrCancel();
                break;
            case R.id.store_count_txtv:
                if (mStore.getTotalGoods() > 0) {
                    Intent listIntent = new Intent(this, SPStoreProductListActivity.class);
                    listIntent.putExtra("storeId", mStoreId);
                    startActivity(listIntent);
                }
                break;
            case R.id.new_txtv:
                if (mStore.getNewGoods() > 0) {
                    Intent listIntent = new Intent(this, SPStoreProductListActivity.class);
                    listIntent.putExtra("storeId", mStoreId);
                    listIntent.putExtra("type", "is_new");
                    startActivity(listIntent);
                }
                break;
            case R.id.hot_txtv:
                if (mStore.getHotGoods() > 0) {
                    Intent listIntent = new Intent(this, SPStoreProductListActivity.class);
                    listIntent.putExtra("storeId", mStoreId);
                    listIntent.putExtra("type", "is_hot");
                    startActivity(listIntent);
                }
                break;
            case R.id.service_online_rl:
                if (!SPStringUtils.isEmpty(mStore.getStoreQQ()))
                    connectCustomer(mStore.getStoreQQ());
                else
                    showToast(getString(R.string.no_contact));
                break;
            case R.id.seller_tel_rl:
                if (!SPStringUtils.isEmpty(mStore.getSellerTel())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mStore.getSellerTel()));
                    if (intent.resolveActivity(getPackageManager()) != null)
                        startActivity(intent);
                } else {
                    showToast("无联系电话");
                }
                break;
        }
    }

    private void collectOrCancel() {
        showLoadingSmallToast();
        SPStoreRequest.collectOrCancelStoreWithID(mStoreId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                if (msg.equals("关注成功")) {
                    collect1Btn.setVisibility(View.GONE);
                    collect2Btn.setVisibility(View.VISIBLE);
                } else {
                    collect1Btn.setVisibility(View.VISIBLE);
                    collect2Btn.setVisibility(View.GONE);
                }
                Intent intent = new Intent(SPMobileConstants.ACTION_STORE_CHANGE);
                sendBroadcast(intent);
            }
        }, new SPFailuredListener(SPStoreAboutActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

}
