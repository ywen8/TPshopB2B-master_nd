//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.2.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.tpshop.mall.activity.person.order;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.tpshop.mall.R.id;
import com.tpshop.mall.R.layout;
import com.tpshop.mall.widget.NoScrollListView;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SPOrderDetailActivity_
    extends SPOrderDetailActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.order_details);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static SPOrderDetailActivity_.IntentBuilder_ intent(Context context) {
        return new SPOrderDetailActivity_.IntentBuilder_(context);
    }

    public static SPOrderDetailActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new SPOrderDetailActivity_.IntentBuilder_(fragment);
    }

    public static SPOrderDetailActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new SPOrderDetailActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        storeNameTxtv = ((TextView) hasViews.findViewById(id.store_name_txtv));
        mButtonGallery = ((LinearLayout) hasViews.findViewById(id.order_button_gallery_lyaout));
        buyTimeTxtv = ((TextView) hasViews.findViewById(id.fee_addtime_txtv));
        phoneNumberTxtv = ((TextView) hasViews.findViewById(id.phone_number_txtv));
        titleShoppingTxtv = ((TextView) hasViews.findViewById(id.title_shopping_txtv));
        feeGoodsFeeTxtv = ((TextView) hasViews.findViewById(id.fee_goodsfee_txtv));
        codeLl = ((LinearLayout) hasViews.findViewById(id.code_ll));
        payTypeTxtv = ((TextView) hasViews.findViewById(id.fee_paytype_txtv));
        orderStatusTxtv = ((TextView) hasViews.findViewById(id.order_status_txtv));
        feeShoppingTxtv = ((TextView) hasViews.findViewById(id.fee_shopping_txtv));
        addressRl = ((RelativeLayout) hasViews.findViewById(id.address_rl));
        orderDetailRl = ((RelativeLayout) hasViews.findViewById(id.order_detail_rl));
        titleBalanceTxtv = ((TextView) hasViews.findViewById(id.title_balance_txtv));
        feePromTxtv = ((TextView) hasViews.findViewById(id.fee_prom_txtv));
        scrollView = ((ScrollView) hasViews.findViewById(id.confirm_scrollv));
        feePointTxtv = ((TextView) hasViews.findViewById(id.fee_point_txtv));
        orderAddressArrowImgv = ((ImageView) hasViews.findViewById(id.order_address_arrow_imgv));
        feeCouponTxtv = ((TextView) hasViews.findViewById(id.fee_coupon_txtv));
        orderInvoceTv = ((TextView) hasViews.findViewById(id.fee_invoce_txtv));
        mProductListVeiw = ((NoScrollListView) hasViews.findViewById(id.product_list_layout));
        titleGetphoneTxtv = ((TextView) hasViews.findViewById(id.title_getphone_txtv));
        feeBalanceTxtv = ((TextView) hasViews.findViewById(id.fee_balance_txtv));
        invoceCodeTv = ((TextView) hasViews.findViewById(id.fee_code_txtv));
        feeAmountTxtv = ((TextView) hasViews.findViewById(id.fee_amount_txtv));
        consigneeTxtv = ((TextView) hasViews.findViewById(id.order_consignee_txtv));
        titleCodeTv = ((TextView) hasViews.findViewById(id.title_code_txtv));
        giveIntegralTxtv = ((TextView) hasViews.findViewById(id.fee_getpoint_txtv));
        titleCouponTxtv = ((TextView) hasViews.findViewById(id.title_coupon_txtv));
        orderSnTxtv = ((TextView) hasViews.findViewById(id.fee_ordersn_txtv));
        addressTxtv = ((TextView) hasViews.findViewById(id.order_address_txtv));
        titlePointTxtv = ((TextView) hasViews.findViewById(id.title_point_txtv));
        if (storeNameTxtv!= null) {
            storeNameTxtv.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    SPOrderDetailActivity_.this.onViewClick(view);
                }

            }
            );
        }
        {
            View view = hasViews.findViewById(id.contact_customer_btn);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SPOrderDetailActivity_.this.onViewClick(view);
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.call_phone_btn);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SPOrderDetailActivity_.this.onViewClick(view);
                    }

                }
                );
            }
        }
        init();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<SPOrderDetailActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, SPOrderDetailActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), SPOrderDetailActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), SPOrderDetailActivity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent, requestCode, lastOptions);
                } else {
                    if (context instanceof Activity) {
                        Activity activity = ((Activity) context);
                        ActivityCompat.startActivityForResult(activity, intent, requestCode, lastOptions);
                    } else {
                        context.startActivity(intent, lastOptions);
                    }
                }
            }
        }

    }

}
