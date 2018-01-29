//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.2.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.tpshop.mall.activity.shop;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.tpshop.mall.R.id;
import com.tpshop.mall.R.layout;
import com.tpshop.mall.widget.NoScrollGridView;
import com.tpshop.mall.widget.SPStarView;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SPCommodityEvaluationActivity_
    extends SPCommodityEvaluationActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.commodity_evaluation);
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

    public static SPCommodityEvaluationActivity_.IntentBuilder_ intent(Context context) {
        return new SPCommodityEvaluationActivity_.IntentBuilder_(context);
    }

    public static SPCommodityEvaluationActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new SPCommodityEvaluationActivity_.IntentBuilder_(fragment);
    }

    public static SPCommodityEvaluationActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new SPCommodityEvaluationActivity_.IntentBuilder_(supportFragment);
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
        commentDeliverStarv = ((SPStarView) hasViews.findViewById(id.comment_deliver_starv));
        commentGoodStarv = ((SPStarView) hasViews.findViewById(id.comment_good_starv));
        anonymousEvaluationRb = ((ToggleButton) hasViews.findViewById(id.anonymous_evaluation_rb));
        productTxt = ((TextView) hasViews.findViewById(id.product_name_txtv));
        EditLl = ((LinearLayout) hasViews.findViewById(id.Edit_ll));
        comment_ll2 = ((LinearLayout) hasViews.findViewById(id.comment_ll2));
        commentDescriptStarv2 = ((SPStarView) hasViews.findViewById(id.comment_descript_starv2));
        addImg = ((ImageView) hasViews.findViewById(id.addImg));
        commentDeliverStarv2 = ((SPStarView) hasViews.findViewById(id.comment_deliver_starv2));
        productImg = ((ImageView) hasViews.findViewById(id.product_pic_imgv));
        commentDescriptStarv = ((SPStarView) hasViews.findViewById(id.comment_descript_starv));
        picGrid = ((NoScrollGridView) hasViews.findViewById(id.picGrid));
        commentServiceStarv = ((SPStarView) hasViews.findViewById(id.comment_service_starv));
        commentEdit = ((EditText) hasViews.findViewById(id.comment_content_edtv));
        comment_ll1 = ((LinearLayout) hasViews.findViewById(id.comment_ll1));
        commentTxtNum = ((TextView) hasViews.findViewById(id.limit_txtv));
        commentServiceStarv2 = ((SPStarView) hasViews.findViewById(id.comment_service_starv2));
        init();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<SPCommodityEvaluationActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, SPCommodityEvaluationActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), SPCommodityEvaluationActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), SPCommodityEvaluationActivity_.class);
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
