package com.tpshop.mall.activity.person;

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
import com.tpshop.mall.model.SPCategory;
import com.tpshop.mall.model.person.SPMerchantsType;
import com.tpshop.mall.utils.SPTypeUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.type_select_citiy)
public class SPTypeSelectActivity extends SPBaseActivity {

    @ViewById(R.id.tv_tittle)
    TextView tvTittle;

    @ViewById(R.id.btn_right)
    Button btn_right;

    @ViewById(R.id.btn_back)
    RelativeLayout btn_back;

    @ViewById(R.id.scrollview)
    HorizontalScrollView scrollview;

    @ViewById(R.id.lv_city)
    ListView lv_city;

    int next;
    int current;
    private SPTypeUtil util;
    boolean canCheck = true;
    private CityAdapter adapter;
    private List<SPCategory> models;
    private String thirdTypeString = "";
    private SPMerchantsType merchantsType;
    public static final int LEVEL_FIRST = 1;
    public static final int LEVEL_SECOND = 2;
    public static final int LEVEL_THIRD = 3;
    private TextView[] tvs = new TextView[3];
    List<Integer> idList = new ArrayList<>();
    private int[] ids = {R.id.rb_first, R.id.rb_second, R.id.rb_third};

    Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LEVEL_FIRST:
                case LEVEL_SECOND:
                case LEVEL_THIRD:
                    canCheck = true;
                    models = (List<SPCategory>) msg.obj;
                    adapter.clear();
                    adapter.addAll(models);
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
        tvTittle.setText("选择类别");
    }

    @Override
    public void initEvent() {
        lv_city.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void initData() {
        for (int i = 0; i < tvs.length; i++)
            tvs[i] = (TextView) findViewById(ids[i]);
        if (merchantsType == null) {
            merchantsType = new SPMerchantsType();
            merchantsType.setFirstType("");
            merchantsType.setSecondType("");
            merchantsType.setThirdType("");
        }
        current = 0;
        next = -1;
        setTextViewSelect();
        adapter = new CityAdapter(this);
        lv_city.setAdapter(adapter);
        util = new SPTypeUtil(this, mHandler);
        util.initFirstCategory();
    }

    public void setTextViewSelect() {
        for (int i = 0; i < tvs.length; i++) {
            if (current == i)
                tvs[i].setBackgroundColor(getResources().getColor(R.color.botton_nav_bg));
            else
                tvs[i].setBackgroundColor(Color.WHITE);
        }
    }

    @Click({R.id.rb_first, R.id.rb_second, R.id.rb_third, R.id.btn_back, R.id.btn_right})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right:      //确定
                boolean flag = SPStringUtils.isEmpty(merchantsType.getFirstType()) || SPStringUtils.isEmpty(merchantsType.getSecondType())
                        || SPStringUtils.isEmpty(merchantsType.getThirdType());
                if (flag) {
                    showToast("类别选择不完整");
                    return;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("merchantsType", merchantsType);
                setResult(101, resultIntent);
                finish();
                break;
            case R.id.rb_first:      //一级分类
                current = 0;
                merchantsType.setSecondType("");
                merchantsType.setThirdType("");
                tvs[1].setText("二级分类");
                tvs[2].setText("三级分类");
                util.initFirstCategory();
                setTextViewSelect();
                idList.clear();
                thirdTypeString = "";
                break;
            case R.id.rb_second:      //二级分类
                if (StringUtils.isEmpty(merchantsType.getFirstType())) {
                    showToast("请先选择一级");
                    return;
                }
                merchantsType.setThirdType("");
                tvs[2].setText("三级分类");
                current = 1;
                util.initChildrenCategory(merchantsType.getFirstTypeId(), LEVEL_SECOND);
                setTextViewSelect();
                idList.clear();
                thirdTypeString = "";
                break;
            case R.id.rb_third:      //三级分类
                if (StringUtils.isEmpty(merchantsType.getFirstType()) || StringUtils.isEmpty(merchantsType.getSecondType())) {
                    current = LEVEL_FIRST;
                    showToast("请选择上级类别");
                    return;
                }
                current = 2;
                setTextViewSelect();
                util.initChildrenCategory(merchantsType.getSecondTypeId(), LEVEL_THIRD);
                break;
        }
    }

    OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            switch (current) {
                case 0:
                    if (canCheck) {
                        String newProvince = models.get(position).getName();
                        if (!newProvince.equals(merchantsType.getFirstType())) {
                            merchantsType.setFirstType(newProvince);
                            merchantsType.setFirstTypeId(models.get(position).getId());
                            merchantsType.setSecondType("");
                            merchantsType.setThirdType("");
                            tvs[0].setText(models.get(position).getName());
                            tvs[1].setText("二级分类");
                            tvs[2].setText("三级分类");
                        }
                        current = 1;
                        util.initChildrenCategory(merchantsType.getFirstTypeId(), LEVEL_SECOND);      //点击一级分类中的类别就初始化二级分类
                        canCheck = false;
                    }
                    break;
                case 1:
                    if (canCheck) {
                        String newCity = models.get(position).getName();
                        if (!newCity.equals(merchantsType.getSecondType())) {
                            merchantsType.setSecondType(newCity);
                            merchantsType.setSecondTypeId(models.get(position).getId());
                            merchantsType.setThirdType("");
                            tvs[1].setText(models.get(position).getName());
                            tvs[2].setText("三级分类");
                        }
                        current = 2;
                        util.initChildrenCategory(merchantsType.getSecondTypeId(), LEVEL_THIRD);
                        canCheck = false;
                    }
                    break;
                case 2:
                    String newDistrict = models.get(position).getName();
                    if (!newDistrict.equals(merchantsType.getThirdType()) && !thirdTypeString.contains(newDistrict)) {
                        idList.add(models.get(position).getId());
                        merchantsType.setThirdTypeId(idList);
                        thirdTypeString += newDistrict + " ";
                        merchantsType.setThirdType(thirdTypeString);
                        tvs[2].setText(thirdTypeString);
                    }
                    current = 2;
                    break;
            }
            setTextViewSelect();
        }
    };

    private class CityAdapter extends ArrayAdapter<SPCategory> {
        LayoutInflater inflater;

        CityAdapter(Context con) {
            super(con, 0);
            inflater = LayoutInflater.from(SPTypeSelectActivity.this);
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
