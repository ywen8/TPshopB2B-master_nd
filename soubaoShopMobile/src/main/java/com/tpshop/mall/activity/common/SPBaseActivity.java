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
 * Date: @date 2015年10月20日 下午7:52:58
 * Description:Activity 基类
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.common;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.person.user.SPLoginActivity;
import com.tpshop.mall.activity.person.user.SPLoginActivity_;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.order.SPOrder;
import com.tpshop.mall.utils.SPConfirmDialog;
import com.tpshop.mall.utils.SPDialogUtils;
import com.tpshop.mall.utils.SPLoadingSmallDialog;
import com.tpshop.mall.utils.SPServerUtils;
import com.tpshop.mall.widget.CustomToast;

import org.json.JSONObject;

import java.util.List;

/**
 * @author 飞龙
 */
public abstract class SPBaseActivity extends AppCompatActivity implements SPIViewController {

    ImageButton mMoreBtn;
    private String mTtitle;                              //标题栏
    protected Button mBackBtn;
    public boolean isBackShow;                           //是否显示返回箭头
    public JSONObject mDataJson;                         //包含网络请求所有结果
    public boolean isMoreTtitle;                         //是否自定义标题栏:包含更多按钮
    private TextView mTitleTxtv;
    private ImageView mCloseImgv;
    protected ImageView mBackImgv;
    public boolean isCustomerTtitle;                     //是否自定义标题栏
    public boolean isWebViewTitleBar = false;            //是否显示返回箭头
    public SPLoadingSmallDialog mLoadingSmallDialog;

    /**
     * 是否自定义标题,该方法必须在子Activity的super.onCreate()之前调用,否则无效
     */
    public void setCustomerTitle(boolean backShow, boolean customerTtitle, String title) {
        isCustomerTtitle = customerTtitle;
        isBackShow = backShow;
        mTtitle = title;
    }

    /**
     * 是否自定义标题,该方法必须在子Activity的super.onCreate()之前调用,否则无效
     */
    public void setCustomerTitleMore(boolean backShow, String title) {
        isCustomerTtitle = true;
        isMoreTtitle = true;
        isBackShow = backShow;
        mTtitle = title;
    }

    public void setTitle(String title) {
        mTtitle = title;
        if (mTitleTxtv != null) mTitleTxtv.setText(mTtitle);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);       //强制竖屏
        if (isMoreTtitle || isCustomerTtitle)
            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);      //自定义标题
    }

    public void init() {
        init(-1);
    }

    public void initWithWebTitle() {
        isWebViewTitleBar = true;
        init(R.layout.webview_titlebar);
    }

    public void init(int layoutId) {
        if (layoutId != -1) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, layoutId);
            mCloseImgv = (ImageView) findViewById(R.id.close_imgv);      //默认隐藏
            mCloseImgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SPBaseActivity.this.finish();
                }
            });
            mCloseImgv.setVisibility(View.INVISIBLE);
        } else {
            if (isMoreTtitle)
                getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_more);
            else if (isCustomerTtitle)
                getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);          //设置标题为某个layout
        }
        mBackBtn = (Button) findViewById(R.id.titlebar_back_btn);
        mBackImgv = (ImageView) findViewById(R.id.titlebar_back_imgv);
        if (isBackShow) {
            mBackBtn.setVisibility(View.VISIBLE);
            mBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isWebViewTitleBar && getWebView() != null && getWebView().canGoBack()) {
                        getWebView().goBack();        //goBack()表示返回WebView
                        mCloseImgv.setVisibility(View.VISIBLE);
                    } else if (isDealBack()) {
                        dealBack();
                    } else {
                        SPBaseActivity.this.finish();
                    }
                }
            });
        } else {
            if (mBackBtn != null) mBackBtn.setVisibility(View.GONE);
        }
        mMoreBtn = (ImageButton) findViewById(R.id.titlebar_more_btn);
        if (mMoreBtn != null)
            mMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopuMenu(v);
                }
            });
        mTitleTxtv = (TextView) findViewById(R.id.titlebar_title_txtv);
        if (mTitleTxtv != null) mTitleTxtv.setText(mTtitle);
        initSubViews();
        initEvent();
        initData();
    }

    public void showCloseImageView() {
        if (mCloseImgv != null) mCloseImgv.setVisibility(View.VISIBLE);
    }

    public void showToast(String msg) {
        SPDialogUtils.showToast(this, msg);
    }

    public void showSuccessToast(String txt) {
        CustomToast.getToast().ToastShow(this, txt, R.drawable.success);
    }

    public void showFailedToast(String txt) {
        CustomToast.getToast().ToastShow(this, txt, R.drawable.fail);
    }

    public void showLoadingSmallToast() {
        if (mLoadingSmallDialog != null) {
            mLoadingSmallDialog.dismiss();
            mLoadingSmallDialog = null;
        }
        mLoadingSmallDialog = new SPLoadingSmallDialog(this);
        mLoadingSmallDialog.setCanceledOnTouchOutside(false);
        mLoadingSmallDialog.show();
    }

    public void hideLoadingSmallToast() {
        if (mLoadingSmallDialog != null) mLoadingSmallDialog.dismiss();
    }

    public void showConfirmDialog(String message, String title, final SPConfirmDialog.ConfirmDialogListener confirmDialogListener,
                                  final int actionType) {
        SPConfirmDialog.Builder builder = new SPConfirmDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (confirmDialogListener != null)
                    confirmDialogListener.clickOk(actionType);         //设置你的操作事项
            }
        });
        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void showToastUnLogin() {
        showToast(getString(R.string.toast_person_unlogin));
    }

    public void toLoginPage() {
        toLoginPage(null);
    }

    public void toLoginPage(String from) {
        Intent loginIntent = new Intent(this, SPLoginActivity_.class);
        if (!SPStringUtils.isEmpty(from)) loginIntent.putExtra(SPLoginActivity.KEY_FROM, from);
        startActivity(loginIntent);
        this.finish();
    }

    /**
     * 再次购买
     */
    public void gotoProductDetail(SPOrder mOrder) {
        List<SPProduct> products = mOrder.getProducts();
        for (SPProduct product : products) {
            SPShopRequest.shopCartGoodsOperation(product.getGoodsID(), product.getItemId(), product.getGoodsNum(), new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    showToast(msg);
                    gotoShopcart();
                }
            }, new SPFailuredListener(SPBaseActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    showToast(msg);
                }
            });
        }
    }

    private void gotoShopcart() {
        Intent intent = new Intent(SPMobileConstants.ACTION_SHOPCART_CHNAGE);
        sendBroadcast(intent);
        Intent shopcartIntent = new Intent(this, SPMainActivity.class);
        shopcartIntent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_SHOPCART);
        startActivity(shopcartIntent);
    }

    /**
     * 联系客服
     */
    public void connectCustomer(String qq) {
        try {
            String customerUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(customerUrl)));
        } catch (Exception e) {
            showToast(getString(R.string.no_install_qq));
            e.printStackTrace();
        }
    }

    /**
     * 联系客服
     */
    public void connectCustomer() {
        String qq = SPServerUtils.getCustomerQQ();
        if (!SPStringUtils.isEmpty(qq))
            connectCustomer(qq);
        else
            showToast("暂无联系方式");
    }

    /**
     * 启动webActivity
     */
    public void startWebViewActivity(String url, String title) {
        Intent shippingIntent = new Intent(this, SPCommonWebActivity.class);
        shippingIntent.putExtra(SPMobileConstants.KEY_WEB_URL, url);
        shippingIntent.putExtra(SPMobileConstants.KEY_WEB_TITLE, title);
        startActivity(shippingIntent);
    }

    /**
     * 初始化界面
     */
    abstract public void initSubViews();

    /**
     * 基类函数,初始化数据
     */
    abstract public void initData();

    /**
     * 基类函数,绑定事件
     */
    abstract public void initEvent();

    public void showPopuMenu(View v) {
    }

    /**
     * 处理网络加载过的数据
     */
    public void dealModel() {
    }

    /**
     * 子类实现方法
     */
    public WebView getWebView() {
        return null;
    }

    /**
     * 是否需要自己处理"返回按钮"
     */
    public boolean isDealBack() {
        return false;
    }

    public void dealBack() {
    }

    @Override
    public void gotoLoginPage() {
        toLoginPage();
    }

    @Override
    public void gotoLoginPage(String from) {
        toLoginPage(from);
    }

    /**
     * 重写activity切换方法,消除系统自带动画
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void setTitleBarLienHide(boolean hide) {
        if (hide) findViewById(R.id.titlebar_line).setVisibility(View.GONE);
    }

}
