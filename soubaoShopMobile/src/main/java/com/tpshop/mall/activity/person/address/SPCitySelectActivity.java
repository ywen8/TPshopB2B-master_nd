package com.tpshop.mall.activity.person.address;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPStringUtils;
import com.ta.utdid2.android.utils.StringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.model.person.SPRegionModel;
import com.tpshop.mall.utils.SPCityUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.consignee_select_citiy)
public class SPCitySelectActivity extends SPBaseActivity {

    @ViewById(R.id.btn_right)
    Button btn_right;

    @ViewById(R.id.btn_back)
    RelativeLayout btn_back;

    @ViewById(R.id.scrollview)
    HorizontalScrollView scrollview;

    @ViewById(R.id.lv_city)
    ListView lv_city;

    int next;                             //-1,0:省,1:市,2:县/区,3:镇/街道
    int current;                          //0:省,1:市,2:县/区,3:镇/街道
    private SPCityUtil util;
    boolean canCheck = true;
    boolean isShowTown = true;
    private CityAdapter adapter;
    private List<SPRegionModel> regions;
    private SPConsigneeAddress consignee;
    public static final int LEVEL_CITY = 2;
    public static final int LEVEL_TOWN = 4;
    private TextView[] tvs = new TextView[4];
    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_DISTRICT = 3;
    private int[] ids = {R.id.rb_province, R.id.rb_city, R.id.rb_district, R.id.rb_town};        //顶栏省市县

    Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LEVEL_PROVINCE:
                case LEVEL_CITY:
                case LEVEL_DISTRICT:
                case LEVEL_TOWN:
                    canCheck = true;
                    regions = (List<SPRegionModel>) msg.obj;
                    adapter.clear();
                    adapter.addAll(regions);
                    adapter.update();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        lv_city.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void initData() {
        consignee = (SPConsigneeAddress) getIntent().getSerializableExtra("consignee");
        isShowTown = getIntent().getBooleanExtra("isShowTown", true);
        for (int i = 0; i < tvs.length; i++)
            tvs[i] = (TextView) findViewById(ids[i]);
        if (!isShowTown)
            tvs[3].setVisibility(View.GONE);
        if (consignee == null) {
            consignee = new SPConsigneeAddress();
            consignee.setProvinceLabel("");
            consignee.setCityLabel("");
            consignee.setDistrictLabel("");
            consignee.setTownLabel("");
        }
        current = 0;
        next = -1;
        setTextViewSelect();
        adapter = new CityAdapter(this);
        lv_city.setAdapter(adapter);
        util = new SPCityUtil(this, mHandler);
        util.initProvince();
    }

    public void setTextViewSelect() {
        for (int i = 0; i < tvs.length; i++) {
            if (current == i)
                tvs[i].setBackgroundColor(getResources().getColor(R.color.botton_nav_bg));
            else
                tvs[i].setBackgroundColor(Color.WHITE);
        }
        if (current == 4)
            tvs[3].setBackgroundColor(getResources().getColor(R.color.botton_nav_bg));
    }

    @Click({R.id.rb_province, R.id.rb_city, R.id.rb_district, R.id.rb_town, R.id.btn_back, R.id.btn_right})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right:
                boolean flag = isShowTown ? (SPStringUtils.isEmpty(consignee.getProvince()) || SPStringUtils.isEmpty(consignee.getCity()) ||
                        SPStringUtils.isEmpty(consignee.getDistrict()) || SPStringUtils.isEmpty(consignee.getTown()))
                        : (SPStringUtils.isEmpty(consignee.getProvince()) || SPStringUtils.isEmpty(consignee.getCity()) ||
                        SPStringUtils.isEmpty(consignee.getDistrict()));
                if (flag) {
                    showToast(getString(R.string.toast_address_nocompletion));
                    return;
                }
                if (!SPStringUtils.isEmpty(consignee.getProvinceLabel())) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("consignee", consignee);
                    setResult(SPMobileConstants.Result_Code_GetValue, resultIntent);
                }
                finish();
                break;
            case R.id.rb_province:      //省份
                current = 0;
                consignee.setCity("");
                consignee.setDistrict("");
                consignee.setTown("");
                tvs[1].setText(R.string.city);
                tvs[2].setText(R.string.district);
                tvs[3].setText(R.string.town);
                util.initProvince();
                setTextViewSelect();
                break;
            case R.id.rb_city:      //城市
                if (StringUtils.isEmpty(consignee.getProvince())) {
                    showToast(getString(R.string.toast_no_select_province));
                    return;
                }
                consignee.setDistrict("");
                consignee.setTown("");
                tvs[2].setText(R.string.district);
                tvs[3].setText(R.string.town);
                current = 1;
                util.initChildrenRegion(consignee.getProvince(), LEVEL_CITY);
                setTextViewSelect();
                break;
            case R.id.rb_district:      //区县
                if (StringUtils.isEmpty(consignee.getProvince()) || StringUtils.isEmpty(consignee.getCity())) {
                    current = LEVEL_PROVINCE;
                    showToast(getString(R.string.toast_no_preview_district));
                    return;
                }
                consignee.setTown("");
                consignee.setTownLabel("");
                tvs[3].setText(R.string.town);
                current = 2;
                setTextViewSelect();
                util.initChildrenRegion(consignee.getCity(), LEVEL_DISTRICT);
                break;
            case R.id.rb_town:      //镇/街道
                if (StringUtils.isEmpty(consignee.getProvince()) || StringUtils.isEmpty(consignee.getCity())
                        || StringUtils.isEmpty(consignee.getDistrict())) {
                    current = 0;
                    showToast(getString(R.string.toast_no_preview_district));
                    return;
                }
                current = 3;
                setTextViewSelect();
                break;
        }
    }

    OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            switch (current) {
                case 0:
                    if (canCheck) {
                        String newProvince = regions.get(position).getName();
                        if (!newProvince.equals(consignee.getProvinceLabel())) {
                            consignee.setProvinceLabel(newProvince);
                            consignee.setProvince(regions.get(position).getRegionID());
                            consignee.setCity("");
                            consignee.setDistrict("");
                            consignee.setTown("");
                            tvs[0].setText(regions.get(position).getName());
                            tvs[1].setText(R.string.city);
                            tvs[2].setText(R.string.district);
                            tvs[3].setText(R.string.town);
                        }
                        current = 1;
                        util.initChildrenRegion(consignee.getProvince(), LEVEL_CITY);      //点击省份列表中的省份就初始化城市列表
                        canCheck = false;
                    }
                    break;
                case 1:
                    if (canCheck) {
                        String newCity = regions.get(position).getName();
                        if (!newCity.equals(consignee.getCity())) {
                            consignee.setCityLabel(newCity);
                            consignee.setCity(regions.get(position).getRegionID());
                            consignee.setDistrict("");
                            consignee.setTown("");
                            tvs[1].setText(regions.get(position).getName());
                            tvs[2].setText(R.string.district);
                            tvs[3].setText(R.string.town);
                        }
                        current = 2;
                        util.initChildrenRegion(consignee.getCity(), LEVEL_DISTRICT);
                        canCheck = false;
                    }
                    break;
                case 2:
                    if (canCheck) {
                        String newDistrict = regions.get(position).getName();
                        if (!newDistrict.equals(consignee.getDistrict())) {
                            consignee.setDistrictLabel(newDistrict);
                            consignee.setDistrict(regions.get(position).getRegionID());
                            consignee.setTown("");
                            consignee.setTownLabel("");
                            tvs[2].setText(regions.get(position).getName());
                            tvs[3].setText(R.string.town);
                        }
                        current = 3;
                        if (isShowTown)
                            util.initChildrenRegion(consignee.getDistrict(), LEVEL_TOWN);
                        canCheck = false;
                    }
                    break;
                case 3:
                case 4:
                    consignee.setTown(regions.get(position).getRegionID());
                    consignee.setTownLabel(regions.get(position).getName());
                    tvs[3].setText(regions.get(position).getName());
                    current = 4;
                    break;
            }
            setTextViewSelect();
        }
    };

    private class CityAdapter extends ArrayAdapter<SPRegionModel> {
        LayoutInflater inflater;

        CityAdapter(Context con) {
            super(con, 0);
            inflater = LayoutInflater.from(SPCitySelectActivity.this);
        }

        @NonNull
        @Override
        public View getView(int position, View v, @NonNull ViewGroup arg2) {
            v = inflater.inflate(R.layout.consignee_select_citiy_item, null);
            TextView tv_city = (TextView) v.findViewById(R.id.tv_city);
            tv_city.setText(getItem(position).getName());
            return v;
        }

        void update() {
            this.notifyDataSetChanged();
        }
    }

}
