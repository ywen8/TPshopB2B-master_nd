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
 * Description: 店铺街道
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRouteGuideManager.CustomizedLayerItem;
import com.baidu.navisdk.adapter.BNRouteGuideManager.OnNavigationListener;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviBaseCallbackModel;
import com.baidu.navisdk.adapter.BaiduNaviCommonModule;
import com.baidu.navisdk.adapter.NaviModuleFactory;
import com.baidu.navisdk.adapter.NaviModuleImpl;
import com.tpshop.mall.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zw
 */
public class SPStoreGuideActivity extends AppCompatActivity {

    private Handler hd = null;
    private static final int MSG_SHOW = 1;
    private static final int MSG_HIDE = 2;
    private static final int MSG_RESET_NODE = 3;
    private BNRoutePlanNode mBNRoutePlanNode = null;
    private BaiduNaviCommonModule mBaiduNaviCommonModule = null;
    private final static String RET_COMMON_MODULE = "module.ret";
    private final String TAG = SPStoreGuideActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        SPStoreMapActivity.activityList.add(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
                mBNRoutePlanNode = (BNRoutePlanNode) bundle.getSerializable(SPStoreMapActivity.ROUTE_PLAN_NODE);
        }
        createHandler();
        View view = null;
        mBaiduNaviCommonModule = NaviModuleFactory.getNaviModuleManager().getNaviCommonModule(
                NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE, this,
                BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE, mOnNavigationListener);
        if (mBaiduNaviCommonModule != null) {
            mBaiduNaviCommonModule.onCreate();
            view = mBaiduNaviCommonModule.getView();
        }
        if (view != null)
            setContentView(view);
        if (hd != null)        //显示自定义图标
            hd.sendEmptyMessageAtTime(MSG_SHOW, 5000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBaiduNaviCommonModule != null)
            mBaiduNaviCommonModule.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBaiduNaviCommonModule != null)
            mBaiduNaviCommonModule.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBaiduNaviCommonModule != null)
            mBaiduNaviCommonModule.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBaiduNaviCommonModule != null)
            mBaiduNaviCommonModule.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBaiduNaviCommonModule != null)
            mBaiduNaviCommonModule.onDestroy();
        SPStoreMapActivity.activityList.remove(this);
    }

    @Override
    public void onBackPressed() {
        if (mBaiduNaviCommonModule != null)
            mBaiduNaviCommonModule.onBackPressed(true);
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mBaiduNaviCommonModule != null)
            mBaiduNaviCommonModule.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (mBaiduNaviCommonModule != null) {
            Bundle mBundle = new Bundle();
            mBundle.putInt(RouteGuideModuleConstants.KEY_TYPE_KEYCODE, keyCode);
            mBundle.putParcelable(RouteGuideModuleConstants.KEY_TYPE_EVENT, event);
            mBaiduNaviCommonModule.setModuleParams(RouteGuideModuleConstants.METHOD_TYPE_ON_KEY_DOWN, mBundle);
            try {
                Boolean ret = (Boolean) mBundle.get(RET_COMMON_MODULE);
                if (ret) return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void addCustomizedLayerItems() {
        List<CustomizedLayerItem> items = new ArrayList<>();
        CustomizedLayerItem item1;
        if (mBNRoutePlanNode != null) {
            item1 = new CustomizedLayerItem(mBNRoutePlanNode.getLongitude(), mBNRoutePlanNode.getLatitude(),
                    mBNRoutePlanNode.getCoordinateType(), getResources().getDrawable(R.drawable.ic_launcher),
                    CustomizedLayerItem.ALIGN_CENTER);
            items.add(item1);
            BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
        }
        BNRouteGuideManager.getInstance().showCustomizedLayer(true);
    }

    private void createHandler() {
        if (hd == null) {
            hd = new Handler(getMainLooper()) {
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == MSG_SHOW) {
                        addCustomizedLayerItems();
                    } else if (msg.what == MSG_HIDE) {
                        BNRouteGuideManager.getInstance().showCustomizedLayer(false);
                    } else if (msg.what == MSG_RESET_NODE) {
                        BNRouteGuideManager.getInstance().resetEndNodeInNavi(new BNRoutePlanNode(mBNRoutePlanNode.getLongitude(),
                                mBNRoutePlanNode.getLatitude(), "目的地", null, CoordinateType.BD09LL));
                    }
                }
            };
        }
    }

    private OnNavigationListener mOnNavigationListener = new OnNavigationListener() {

        @Override
        public void onNaviGuideEnd() {
            finish();
        }

        @Override
        public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {
            if (actionType == 0) {
                //导航到达目的地 自动退出
                Log.i(TAG, "notifyOtherAction actionType = " + actionType + ",导航到达目的地！");
            }
            Log.i(TAG, "actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 + "obj:" + obj.toString());
        }
    };

    private interface RouteGuideModuleConstants {
        int METHOD_TYPE_ON_KEY_DOWN = 0x01;
        String KEY_TYPE_KEYCODE = "keyCode";
        String KEY_TYPE_EVENT = "event";
    }

}
