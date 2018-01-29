package com.tpshop.mall.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by admin on 2017/6/17
 */
public class ProgressWebView extends WebView {

    private Handler handler;
    private WebView mWebView;
    private WebViewProgressBar progressBar;             //进度条的矩形(进度线)

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressBar = new WebViewProgressBar(context);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.setVisibility(GONE);                //刚开始时候进度条不可见
        addView(progressBar);                           //把进度条添加到webView里面
        handler = new Handler();
        mWebView = this;
        initSettings();
    }

    private void initSettings() {
        WebSettings mSettings = this.getSettings();
        mSettings.setJavaScriptEnabled(true);                               //开启javascript
        mSettings.setDomStorageEnabled(true);                               //开启DOM
        mSettings.setDefaultTextEncodingName("utf-8");                      //设置字符编码
        mSettings.setAllowFileAccess(true);                                 //设置支持文件流
        mSettings.setSupportZoom(true);                                     //支持缩放
        mSettings.setBuiltInZoomControls(true);                             //支持缩放
        mSettings.setUseWideViewPort(true);                                 //调整到适合webview大小
        mSettings.setLoadWithOverviewMode(true);                            //调整到适合webview大小
        mSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);              //屏幕自适应网页
        mSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mSettings.setAppCacheEnabled(true);                                 //开启缓存机制
        setWebViewClient(new MyWebClient());
        setWebChromeClient(new MyWebChromeClient());
    }

    /**
     * 自定义WebChromeClient
     */
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setProgress(100);
                handler.postDelayed(runnable, 500);          //0.5秒后隐藏进度条
            } else if (progressBar.getVisibility() == GONE) {
                progressBar.setVisibility(VISIBLE);
            }
            if (newProgress < 10)              //设置初始进度10
                newProgress = 10;
            progressBar.setProgress(newProgress);          //不断更新进度
            super.onProgressChanged(view, newProgress);
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.getSettings().setBlockNetworkImage(false);           //关闭图片加载阻塞
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            ProgressWebView.this.requestFocus();
            ProgressWebView.this.requestFocusFromTouch();
        }
    }

    /**
     * 刷新界面(此处为加载完成后进度消失)
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
        }
    };

}
