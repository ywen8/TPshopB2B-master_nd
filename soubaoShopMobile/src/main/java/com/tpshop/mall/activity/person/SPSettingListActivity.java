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
 * Date: @date 2015年11月3日 下午10:04:49
 * Description: 设置列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.person;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.SPSettingListAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.global.SPSaveData;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.SPAppData;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.utils.SPServerUtils;
import com.tpshop.mall.utils.UpdateAppUtil;
import com.tpshop.mall.widget.UpdateDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 飞龙
 */
@EActivity(R.layout.person_setting_list)
public class SPSettingListActivity extends SPBaseActivity {

    @ViewById(R.id.setting_listv)
    ListView settingListv;

    @ViewById(R.id.exit_btn)
    Button exitBtn;

    List<String> mTexts;
    String mAfterSalePhone;
    private String cacheSize;
    private SPAppData appData;
    SPSettingListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.setCustomerTitle(true, true, getString(R.string.settings));
        super.onCreate(bundle);
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
        settingListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:      //联系客服
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        if (SPStringUtils.isEmpty(mAfterSalePhone)) {
                            showToast("请在后台配置客服电话");
                            return;
                        }
                        intent.setData(Uri.parse("tel:" + mAfterSalePhone));
                        if (intent.resolveActivity(getPackageManager()) != null)
                            startActivity(intent);
                        break;
                    case 1:      //触屏版
                        startWebViewActivity(SPMobileConstants.BASE_HOST + "/index.php/mobile", "触屏版");
                        break;
                    case 2:      //清除缓存
                        if (getFolderSize(getCacheDir()) == 0) {
                            showToast("当前无缓存");
                            return;
                        }
                        clearAllCache();
                        cacheSize = getTotalCacheSize();
                        mTexts.remove(position);
                        mTexts.add(position, "清除缓存 （" + cacheSize + "）");
                        mAdapter.notifyDataSetChanged();
                        break;
                    case 3:      //检测版本
                        checkVersion();
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        mAdapter = new SPSettingListAdapter(this);
        settingListv.setAdapter(mAdapter);
        mAfterSalePhone = SPServerUtils.getServicePhone();
        cacheSize = getTotalCacheSize();
        mTexts = new ArrayList<>();
        mTexts.add("客服电话:" + mAfterSalePhone);
        mTexts.add("触屏版");
        mTexts.add("清除缓存 （" + cacheSize + "）");
        mTexts.add("检测版本");
        mAdapter.setData(mTexts);
    }

    @Click({R.id.exit_btn})
    public void onViewClick(View v) {
        if (v.getId() == R.id.exit_btn) {
            showLoadingSmallToast();
            SPUserRequest.logout(new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    sendBroadcast(new Intent(SPMobileConstants.ACTION_LOGIN_CHNAGE));
                    clearInfo();
                    showToast(msg);
                    finish();
                }
            }, new SPFailuredListener(SPSettingListActivity.this) {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    sendBroadcast(new Intent(SPMobileConstants.ACTION_LOGIN_CHNAGE));
                    showToast(msg);
                    SPMobileApplication.getInstance().exitLogin();
                    finish();
                }
            });
        }
    }

    //清除用户有关的信息
    private void clearInfo() {
        SPMobileApplication.getInstance().exitLogin();
        SPSaveData.putValue(this, SPMobileConstants.KEY_SEARCH_KEY, "");      //清除搜索记录
        SPSaveData.putValue(this, SPMobileConstants.KEY_NAME_KEY, "");        //清除发票单位记录
        SPSaveData.putValue(this, SPMobileConstants.KEY_CODE_KEY, "");        //清除发票纳税人识别号记录
        SPConsigneeAddress consigneeAddress = new SPConsigneeAddress();       //清除商品详情选择的区域
        consigneeAddress.setAddress("请选择地址");
        consigneeAddress.setAddressID("");
        consigneeAddress.setDistrict("");
        SPSaveData.saveAddress(this, consigneeAddress);
    }

    //检测版本
    private void checkVersion() {
        try {
            String verName = getPackageManager().getPackageInfo("com.tpshop.mall", 0).versionName;
            showLoadingSmallToast();
            SPUserRequest.getUpdateInfo(verName, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    hideLoadingSmallToast();
                    appData = (SPAppData) response;
                    if (appData.getIsNew() == 1)
                        showUpdataDialog();
                    else
                        showToast("当前已是最新版本");
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    hideLoadingSmallToast();
                    showFailedToast(msg);
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //清除缓存
    public void clearAllCache() {
        deleteDir(getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            deleteDir(getExternalCacheDir());
        showSuccessToast("缓存清除完成");
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) return false;
            }
        }
        return dir.delete();
    }

    //格式化单位
    public String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) return size + "B";
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public String getTotalCacheSize() {
        long cacheSize = 0;
        try {
            cacheSize = getFolderSize(getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                cacheSize += getFolderSize(getExternalCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getFormatSize(cacheSize);
    }

    public long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory())      //如果下面还有文件
                    size = size + getFolderSize(aFileList);
                else
                    size = size + aFileList.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public void showUpdataDialog() {
        UpdateDialog.Builder updateBuilder = new UpdateDialog.Builder(this);
        updateBuilder.setMessage(appData.getLog());
        final UpdateDialog dialog = updateBuilder.create();
        updateBuilder.setCancelOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        updateBuilder.setDownloadOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAppUtil update = new UpdateAppUtil();
                update.downLoadApk(SPSettingListActivity.this, appData.getUrl());
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
