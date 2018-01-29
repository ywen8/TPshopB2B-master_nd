package com.tpshop.mall.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.person.order.SPOrderListActivity_;
import com.tpshop.mall.activity.person.order.SPVirtualOrderActivity_;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.utils.SPOrderUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_spreceiving_completed)
public class SPPayCompletedSecActivity extends SPBaseActivity implements View.OnClickListener {

    @ViewById(R.id.check_order_btn)
    Button check_order;

    @ViewById(R.id.pay_money_txtv)
    TextView payMoneyText;

    @ViewById(R.id.pay_trade_no_txtv)
    TextView payTradeNoText;

    private String tradeNo;
    private String tradeFee;
    private boolean isVirtual;
    private TextView tvComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "订单支付成功");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        FrameLayout titlebarLayout = (FrameLayout) findViewById(R.id.titlebar_normal_layout);
        tvComplete = new TextView(this);
        tvComplete.setText("完成");
        tvComplete.setTextColor(this.getResources().getColor(R.color.gray));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        tvComplete.setPadding(0, 0, 10, 0);
        params.setMargins(0, 0, 10, 0);
        tvComplete.setGravity(Gravity.CENTER_VERTICAL);
        tvComplete.setTextSize(16);
        titlebarLayout.addView(tvComplete, params);
    }

    @Override
    public void initEvent() {
        check_order.setOnClickListener(this);
        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVirtual) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    if (SPMobileApplication.getInstance().fellBack == 1 || SPMobileApplication.getInstance().fellBack == 3) {     //返回启动页面
                        setResult(RESULT_OK);
                        finish();
                    } else {      //返回到购物车
                        Intent intent = new Intent(SPPayCompletedSecActivity.this, SPMainActivity.class);
                        intent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_SHOPCART);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            tradeFee = getIntent().getStringExtra("tradeFee");
            tradeNo = getIntent().getStringExtra("tradeNo");
            isVirtual = getIntent().getBooleanExtra("isVirtual", false);
        }
        if (SPStringUtils.isEmpty(tradeFee) || SPStringUtils.isEmpty(tradeNo)) {
            showToast("数据不完整!");
            return;
        }
        String totalFeeFmt = "支付金额:¥" + tradeFee;
        String totalNoFmt = "订单编号:" + tradeNo;
        int startIndex = 5;
        int endIndex = totalFeeFmt.length();
        SpannableString totalFeeSpanStr = new SpannableString(totalFeeFmt);
        totalFeeSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);       //设置区域内文字颜色为洋红色
        payMoneyText.setText(totalFeeSpanStr);
        endIndex = totalNoFmt.length();
        SpannableString tradeNoSpanStr = new SpannableString(totalNoFmt);
        tradeNoSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);       //设置区域内文字颜色为洋红色
        payTradeNoText.setText(tradeNoSpanStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_order_btn:
                if (isVirtual) {
                    Intent intent = new Intent(this, SPVirtualOrderActivity_.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Intent allOrderList = new Intent(this, SPOrderListActivity_.class);
                    allOrderList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    allOrderList.putExtra("orderStatus", SPOrderUtils.OrderStatus.all.value());
                    startActivity(allOrderList);
                }
                break;
        }
    }

}
