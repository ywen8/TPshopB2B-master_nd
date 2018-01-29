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

import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.person.catipal.SPTopUpActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_sprecharge_completed)
public class SPRechargeCompletedSecActivity extends SPBaseActivity implements View.OnClickListener {

    @ViewById(R.id.check_order_btn)
    Button check_order;

    @ViewById(R.id.pay_money_txtv)
    TextView payMoneyText;

    private String tradeFee;
    private TextView tvComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "确认充值成功");
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
                Intent intent = new Intent(SPRechargeCompletedSecActivity.this, SPTopUpActivity_.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        if (getIntent() != null) tradeFee = getIntent().getStringExtra("tradeFee");
        String totalFeeFmt = "支付金额:¥" + tradeFee;
        int startIndex = 5;
        int endIndex = totalFeeFmt.length();
        SpannableString totalFeeSpanStr = new SpannableString(totalFeeFmt);
        totalFeeSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);        //设置区域内文字颜色为洋红色
        payMoneyText.setText(totalFeeSpanStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_order_btn:      //返回到个人中心
                Intent shopcartIntent = new Intent(this, SPMainActivity.class);
                shopcartIntent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_PERSON);
                startActivity(shopcartIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SPTopUpActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
