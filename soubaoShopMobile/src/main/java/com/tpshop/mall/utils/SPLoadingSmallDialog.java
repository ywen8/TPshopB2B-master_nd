package com.tpshop.mall.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.tpshop.mall.R;

/**
 * Created by admin on 2016/6/20
 */
public class SPLoadingSmallDialog extends AlertDialog {

    private AnimationDrawable animation;

    public SPLoadingSmallDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_small_dialog);
        ImageView imageView = (ImageView) findViewById(R.id.loading_imgv);
        animation = (AnimationDrawable) imageView.getBackground();
    }

    @Override
    protected void onStart() {
        super.onStart();
        animation.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        animation.stop();
    }

}
