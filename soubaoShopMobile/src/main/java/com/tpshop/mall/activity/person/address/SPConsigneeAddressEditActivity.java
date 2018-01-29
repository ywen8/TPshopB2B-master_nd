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
 * Date: @date 2015年10月23日 下午9:20:58
 * Description: 新增或编辑收货地址
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.person.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.utils.SPConfirmDialog;
import com.tpshop.mall.widget.SwitchButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author 飞龙
 */
@EActivity(R.layout.consignee_address_edit)
public class SPConsigneeAddressEditActivity extends SPBaseActivity implements SPConfirmDialog.ConfirmDialogListener {

    @ViewById(R.id.consignee_name_edtv)
    EditText consigneeEdtv;                  //收货人姓名

    @ViewById(R.id.consignee_mobile_edtv)
    EditText mobileEdtv;                     //收货人电话

    @ViewById(R.id.consignee_region_txtv)
    TextView regionTxtv;                     //收货地址

    @ViewById(R.id.consignee_address_edtv)
    EditText addressEdtv;                    //收货地址

    @ViewById(R.id.set_default_switch)
    SwitchButton setDefaultSwitch;           //默认地址开关

    @ViewById(R.id.submit_btn)
    Button submitBtn;

    private SPConsigneeAddress consignee;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.title_consignee));
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getSerializableExtra("consignee") != null)
            consignee = (SPConsigneeAddress) getIntent().getSerializableExtra("consignee");
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        if (consignee != null) {
            FrameLayout titlebarLayout = (FrameLayout) findViewById(R.id.titlebar_normal_layout);
            TextView compliteTv = new TextView(this);
            compliteTv.setText("删除");
            compliteTv.setTextColor(this.getResources().getColor(R.color.color_font_666));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.RIGHT;
            compliteTv.setPadding(0, 0, 10, 0);
            params.setMargins(0, 0, 10, 0);
            compliteTv.setGravity(Gravity.CENTER_VERTICAL);
            compliteTv.setTextSize(16);
            titlebarLayout.addView(compliteTv, params);
            compliteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAddress();
                }
            });
        }
    }

    @Override
    public void initEvent() {
        setDefaultSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    consignee.setIsDefault("1");
                else
                    consignee.setIsDefault("0");
            }
        });
    }

    @Override
    public void initData() {
        if (consignee == null) {
            consignee = new SPConsigneeAddress();
            consignee.setIsDefault("0");
        } else {
            consigneeEdtv.setText(consignee.getConsignee());
            mobileEdtv.setText(consignee.getMobile());
            addressEdtv.setText(consignee.getAddress());
            if ("1".equals(consignee.getIsDefault()))
                setDefaultSwitch.setChecked(true);
            else
                setDefaultSwitch.setChecked(false);
            String provinceName = consignee.getProvinceName() == null ? "" : consignee.getProvinceName();
            String cityName = consignee.getCityName() == null ? "" : consignee.getCityName();
            String districtName = consignee.getDistrictName() == null ? "" : consignee.getDistrictName();
            String twonName = consignee.getTwonName() == null ? "" : consignee.getTwonName();
            regionTxtv.setText(provinceName + " " + cityName + " " + districtName + " " + twonName);
        }
    }

    //删除地址
    private void deleteAddress() {
        showConfirmDialog("确定删除该地址吗", "删除提醒", this, 1);
    }

    @Override
    public void clickOk(int actionType) {
        showLoadingSmallToast();
        SPPersonRequest.delConsigneeAddressByID(consignee.getAddressID(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                setResult(RESULT_OK);
                finish();
            }
        }, new SPFailuredListener(SPConsigneeAddressEditActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    @Click({R.id.submit_btn, R.id.consignee_region_txtv})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                if (!checkData()) return;
                RequestParams params = new RequestParams();
                params.put("consignee", consignee.getConsignee());
                params.put("province", consignee.getProvince());
                params.put("city", consignee.getCity());
                params.put("district", consignee.getDistrict());
                params.put("twon", consignee.getTown());
                params.put("address", consignee.getAddress());
                params.put("mobile", consignee.getMobile());
                params.put("is_default", consignee.getIsDefault());
                if (!SPStringUtils.isEmpty(consignee.getAddressID()))
                    params.put("address_id", consignee.getAddressID());
                showLoadingSmallToast();
                SPPersonRequest.saveUserAddressWithParameter(params, new SPSuccessListener() {
                    @Override
                    public void onRespone(String msg, Object response) {
                        hideLoadingSmallToast();
                        showSuccessToast(msg);
                        setResult(RESULT_OK);
                        SPConsigneeAddressEditActivity.this.finish();
                    }
                }, new SPFailuredListener(SPConsigneeAddressEditActivity.this) {
                    @Override
                    public void onRespone(String msg, int errorCode) {
                        hideLoadingSmallToast();
                        showFailedToast(msg);
                    }
                });
                break;
            case R.id.consignee_region_txtv:
                Intent regionIntent = new Intent(this, SPCitySelectActivity_.class);
                regionIntent.putExtra("consignee", consignee);
                regionIntent.putExtra("isShowTown", true);      //false表示不需要显示街道,不带默认是true,显示街道
                startActivityForResult(regionIntent, SPMobileConstants.Result_Code_GetValue);
                break;
        }
    }

    private boolean checkData() {
        if (consigneeEdtv.getText().toString().trim().isEmpty()) {
            showToast("请输入收货人");
            return false;
        }
        consignee.setConsignee(consigneeEdtv.getText().toString());
        if (SPStringUtils.isEmpty(mobileEdtv.getText().toString())) {
            showToast("请输入联系方式");
            return false;
        }
        consignee.setMobile(mobileEdtv.getText().toString());
        if (addressEdtv.getText().toString().trim().isEmpty()) {
            showToast("请输入详细地址");
            return false;
        }
        consignee.setAddress(addressEdtv.getText().toString());
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SPMobileConstants.Result_Code_GetValue) {
            if (data == null || data.getSerializableExtra("consignee") == null) return;
            SPConsigneeAddress selectRegionConsignee = (SPConsigneeAddress) data.getSerializableExtra("consignee");
            String fullRegion = selectRegionConsignee.getProvinceLabel() + " " + selectRegionConsignee.getCityLabel()
                    + " " + selectRegionConsignee.getDistrictLabel() + " " + selectRegionConsignee.getTownLabel();
            regionTxtv.setText(fullRegion);
            consignee.setProvince(selectRegionConsignee.getProvince());
            consignee.setCity(selectRegionConsignee.getCity());
            consignee.setDistrict(selectRegionConsignee.getDistrict());
            consignee.setTown(selectRegionConsignee.getTown());
        }
    }

}
