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

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPSaveData;
import com.tpshop.mall.model.shop.SPStore;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zw
 */
@EActivity(R.layout.store_map)
public class SPStoreMapActivity extends SPBaseActivity {

    SPStore store;
    BaiduMap mBaiduMap;
    ImageView goNowImg;
    private String lng;
    private String lat;
    String authinfo = null;
    private Marker mMarkerA;
    private Marker mMarkerB;
    TextureMapView mTexturemap;
    private LatLng startPt, endPt;
    private String mSDCardPath = null;
    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;
    private CoordinateType mCoordinateType = null;
    private final static int authComRequestCode = 2;
    private final static int authBaseRequestCode = 1;
    private static final String APP_FOLDER_NAME = "TPshopDemo";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static List<Activity> activityList = new LinkedList<>();
    private final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};
    BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    BitmapDescriptor bdB = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);
    private final static String authBaseArr[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle bundle) {
        activityList.add(this);
        store = (SPStore) getIntent().getSerializableExtra("store");
        if (store == null) finish();
        String storeName = (SPStringUtils.isEmpty(store.getStoreName())) ? "店铺名称异常" : store.getStoreName();
        setCustomerTitle(true, true, storeName);
        super.onCreate(bundle);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        mTexturemap = (TextureMapView) findViewById(R.id.mTexturemap);
        goNowImg = (ImageView) findViewById(R.id.go_now_img);
        lng = SPSaveData.getString(this, SPMobileConstants.KEY_LONGITUDE);
        lat = SPSaveData.getString(this, SPMobileConstants.KEY_LATITUDE);
        startPt = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        endPt = new LatLng(Double.valueOf(store.getLat()), Double.valueOf(store.getLon()));
//        BNOuterLogUtil.setLogSwitcher(true);
        if (initDirs()) initNavi();
        initMapStatus();
        initOverlay();
    }

    private void initMapStatus() {
        mBaiduMap = mTexturemap.getMap();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(endPt).zoom(15);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    public void initOverlay() {
        MarkerOptions ooA = new MarkerOptions().position(startPt).icon(bdA).zIndex(9).draggable(true);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        mMarkerA.setDraggable(true);
        MarkerOptions ooB = new MarkerOptions().position(endPt).icon(bdB).zIndex(5);
        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
        mMarkerB.setDraggable(true);
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                if (marker == mMarkerA) {
                    startPt = marker.getPosition();
                } else if (marker == mMarkerB) {
                    endPt = marker.getPosition();
                }
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    @Override
    public void initEvent() {
        goNowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaiduNaviManager.isNaviInited())
                    routeplanToNavi(CoordinateType.BD09LL);
            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTexturemap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTexturemap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTexturemap.onDestroy();
        bdA.recycle();
        bdB.recycle();
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null)
            return false;
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    Log.i("Handler", "TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    Log.i("Handler", "TTS play end");
                    break;
                }
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            Log.i("TTSPlayStateListener", "TTS play end");
        }

        @Override
        public void playStart() {
            Log.i("TTSPlayStateListener", "TTS play start");
        }
    };

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    private boolean hasCompletePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    private void initNavi() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                requestPermissions(authBaseArr, authBaseRequestCode);            //申请权限
                return;
            }
        }
        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status)
                    authinfo = "key校验成功!";
                else
                    authinfo = "key校验失败, " + msg;
                Log.e("authinfo", authinfo);
            }

            public void initSuccess() {
                Log.i("NaviInitListener", "导航引擎初始化成功");
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
                Log.i("NaviInitListener", "导航引擎初始化开始");
            }

            public void initFailed() {
                showToast("导航引擎初始化失败");
            }
        }, null, ttsHandler, ttsPlayStateListener);
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return Environment.getExternalStorageDirectory().toString();
        return null;
    }

    private void routeplanToNavi(CoordinateType coType) {
        mCoordinateType = coType;
        if (!hasInitSuccess) {
            showToast("还未初始化!");
        }
        if (android.os.Build.VERSION.SDK_INT >= 23) {        //权限申请
            if (!hasCompletePhoneAuth()) {         //保证导航功能完备
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    this.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    showToast("没有完备的权限!");
                }
            }
        }
        BNRoutePlanNode sNode = new BNRoutePlanNode(Double.valueOf(lng), Double.valueOf(lat), "当前位置", null, coType);
        BNRoutePlanNode eNode = new BNRoutePlanNode(Double.valueOf(store.getLon()), Double.valueOf(store.getLat()),
                store.getStoreName(), null, coType);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode), null);
        }
    }

    private class DemoRoutePlanListener implements RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            for (Activity ac : activityList) {
                if (ac.getClass().getName().endsWith("SPStoreGuideActivity"))
                    return;
            }
            Intent intent = new Intent(SPStoreMapActivity.this, SPStoreGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onRoutePlanFailed() {
            showToast("路线规划失败");
        }
    }

    private void initSetting() {
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "10090433");          //必须设置APPID,否则会静音
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret != 0) {
                    showToast("缺少导航基本的权限!");
                    return;
                }
            }
            initNavi();
        } else if (requestCode == authComRequestCode) {
            routeplanToNavi(mCoordinateType);
        }
    }

}
