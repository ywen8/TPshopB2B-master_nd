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
 * Description:Activity 支付列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tpshop.mall.R;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.order.SPOrder;
import com.tpshop.mall.model.shop.WxPayInfo;
import com.tpshop.mall.utils.PayResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 2016/6/29
 */
@EActivity(R.layout.pay_list)
public class SPPayListActivity extends SPBaseActivity {

    @ViewById(R.id.pay_money_txtv)
    TextView payMoneyText;

    WxPayInfo mWxPayInfo;
    private SPOrder order;
    private IWXAPI mWXApi;
    private String account;
    String mAlipayOrderSignInfo;
    private final int SDK_PAY_FLAG = 1;
    public static SPPayListActivity mPayListActivity;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {        //同步返回的结果必须放置到服务端进行验证,建议商户依赖异步通知
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();        //同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {           //判断resultStatus为“9000”则代表支付成功,具体状态码代表含义可参考接口文档
                        showToast("支付成功");
                        onPayFinish();
                    } else {
                        //判断resultStatus为非"9000"则代表可能支付失败,"8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认
                        //最终交易是否成功以服务端异步通知为准(小概率状态)
                        if (TextUtils.equals(resultStatus, "8000"))
                            showToast("支付结果确认中");
                        else           //其他值就可以判断为支付失败,包括用户主动取消支付,或者系统返回的错误
                            showToast("支付失败");
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_pay_list));
        super.onCreate(savedInstanceState);
        mPayListActivity = this;
    }

    public static SPPayListActivity getInstantce() {
        return mPayListActivity;
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
        if (getIntent() != null) {
            order = (SPOrder) getIntent().getSerializableExtra("order");
            account = getIntent().getStringExtra("account");
        }
        String wxpayid = SPMobileConstants.pluginWeixinAppid;
        mWXApi = WXAPIFactory.createWXAPI(this, wxpayid);
        if (order != null)
            payMoneyText.setText("¥" + order.getOrderAmount());
        else
            payMoneyText.setText("¥" + account);
    }

    @Click({R.id.pay_alipay_aview, R.id.pay_wechat_aview})
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.pay_alipay_aview:
                aliPay();
                break;
            case R.id.pay_wechat_aview:
                wxPay();
                break;
        }
    }

    /**
     * 支付宝支付
     */
    public void aliPay() {
        if (!SPStringUtils.isEmpty(mAlipayOrderSignInfo)) {
            startupAlipay();
            return;
        }
        RequestParams params = new RequestParams();
        if (order != null)
            params.put("order_sn", order.getOrderSN());
        if (account != null)
            params.put("account", account);
        showLoadingSmallToast();
        SPShopRequest.getAlipayOrderSignInfo(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                if (response != null) {
                    mAlipayOrderSignInfo = response.toString();
                    startupAlipay();
                } else {
                    showFailedToast("签名结果为空");
                }
            }
        }, new SPFailuredListener(SPPayListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    //完整的符合支付宝参数规范的订单信息
    private void startupAlipay() {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(SPPayListActivity.this);         //构造PayTask对象
                String result = alipay.pay(mAlipayOrderSignInfo, true);       //调用支付接口,获取支付结果
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);       //必须异步调用
        payThread.start();
    }

    /**
     * 微信支付
     */
    public void wxPay() {
        if (mWxPayInfo != null) {
            startupWxPay();
            return;
        }
        RequestParams params = new RequestParams();
        if (order != null) {
            SPMobileApplication.getInstance().setPayOrder(order);
            String orderSN = order.getOrderSN();
            params.put("order_sn", orderSN);
        }
        if (account != null)
            params.put("account", account);
        showLoadingSmallToast();
        SPShopRequest.getWxPayInfo(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                if (response != null) {
                    mWxPayInfo = (WxPayInfo) response;
                    startupWxPay();
                }
            }
        }, new SPFailuredListener(SPPayListActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    /**
     * 调用微信支付
     */
    public void startupWxPay() {
        PayReq req = new PayReq();
        String extData = (mWxPayInfo.getExtData() != null) ? mWxPayInfo.getExtData() : getString(R.string.app_name);
        req.appId = mWxPayInfo.getAppid();
        req.partnerId = mWxPayInfo.getPartnerid();
        req.prepayId = mWxPayInfo.getPrepayid();
        req.nonceStr = mWxPayInfo.getNoncestr();
        req.timeStamp = mWxPayInfo.getTimestamp();
        req.packageValue = mWxPayInfo.getPackageValue();
        req.sign = mWxPayInfo.getSign();
        req.extData = extData;
        mWXApi.sendReq(req);         //在支付之前,如果应用没有注册到微信,应该先调用IWXMsg.registerApp将应用注册到微信
    }

    /**
     * 支付成功后调用
     */
    public void onPayFinish() {
        if (order != null) {
            Intent completedIntent = new Intent(this, SPPayCompletedSecActivity_.class);
            completedIntent.putExtra("tradeFee", order.getOrderAmount());
            completedIntent.putExtra("tradeNo", order.getOrderSN());
            completedIntent.putExtra("isVirtual", order.isVirtual());
            startActivity(completedIntent);
            setResult(RESULT_OK);
            this.finish();
        } else {
            Intent completedIntent = new Intent(this, SPRechargeCompletedSecActivity_.class);
            completedIntent.putExtra("tradeFee", account);
            startActivity(completedIntent);
            setResult(RESULT_OK);
            this.finish();
        }
    }

}
