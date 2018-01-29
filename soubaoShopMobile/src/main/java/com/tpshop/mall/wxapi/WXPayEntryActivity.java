package com.tpshop.mall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPPayCompletedSecActivity_;
import com.tpshop.mall.activity.common.SPPayListActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.model.order.SPOrder;
import com.tpshop.mall.utils.SPDialogUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, View.OnClickListener {

    private IWXAPI api;
    private int mPayStatus;                //支付状态,1:支付成功,0:取消支付,-1:支付错误
    private Button mOrderBtn;
    private TextView mPayTipTxtv;
    private Button mHomeOrPayBtn;
    public TextView mTradeNoTxtv;
    public TextView mPayMoneyTxtv;
    public ImageView mPayCompletedImgv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_result);
        initSubViews();
        initEvent();
        initData();
        api = WXAPIFactory.createWXAPI(this, SPMobileConstants.pluginWeixinAppid);
        api.handleIntent(getIntent(), this);
    }

    public void initSubViews() {
        mPayCompletedImgv = (ImageView) findViewById(R.id.pay_completed_imgv);
        mTradeNoTxtv = (TextView) findViewById(R.id.pay_trade_no_txtv);
        mPayMoneyTxtv = (TextView) findViewById(R.id.pay_money_txtv);
        mPayTipTxtv = (TextView) findViewById(R.id.pay_tip_txtv);
        mOrderBtn = (Button) findViewById(R.id.order_btn);
        mHomeOrPayBtn = (Button) findViewById(R.id.home_or_repay_btn);
    }

    public void initEvent() {
        mOrderBtn.setOnClickListener(this);
        mHomeOrPayBtn.setOnClickListener(this);
    }

    public void initData() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {  //微信结果返回
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {       //用户取消授权
                mPayStatus = 1;
                onPayFinish();
            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {  //用户取消授权
                SPDialogUtils.showToast(this, "用户取消授权" + String.valueOf(resp.errCode));
                mPayStatus = 0;
                this.finish();
            } else {  //支付错误
                SPDialogUtils.showToast(this, "用户取消授权" + getString(R.string.weixin_pay));
                mPayStatus = -1;
                this.finish();
            }
        }
    }

    /**
     * 支付成功后调用
     */
    public void onPayFinish() {
        SPOrder order = SPMobileApplication.getInstance().getPayOrder();
        if (order == null) {
            this.finish();
            return;
        }
        if (SPPayListActivity.getInstantce() != null) SPPayListActivity.getInstantce().finish();
        Intent completedIntent = new Intent(this, SPPayCompletedSecActivity_.class);
        completedIntent.putExtra("tradeFee", order.getOrderAmount());
        completedIntent.putExtra("tradeNo", order.getOrderSN());
        startActivity(completedIntent);
    }

    /**
     * 刷新页面状态
     */
    public void refreshView() {
        switch (mPayStatus) {
            case 1:        //支付成功
                mPayTipTxtv.setText("感谢购买,我们将尽快安排发货!");
                break;
            case 0:        //取消支付
                mPayTipTxtv.setText("亲,给个表现的机会!");
                break;
            case -1:       //支付错误
                mPayTipTxtv.setText("抱歉,支付出错啦!");
                break;
        }
    }

    @Override
    public void onClick(View v) {
    }

}