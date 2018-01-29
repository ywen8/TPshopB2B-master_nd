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
 * Date: @date 2015年10月20日 下午7:52:58
 * Description:Activity 区域选择列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.common;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.soubao.tpshop.utils.SPJsonUtil;
import com.tpshop.mall.R;
import com.tpshop.mall.adapter.HotCityAdapter;
import com.tpshop.mall.adapter.PinYinSortAdapter;
import com.tpshop.mall.common.PinyinComparator;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.global.SPSaveData;
import com.tpshop.mall.model.SPCity;
import com.tpshop.mall.model.SPServiceConfig;
import com.tpshop.mall.utils.CharacterParser;
import com.tpshop.mall.widget.SPClearEditText;
import com.tpshop.mall.widget.SideBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EActivity(R.layout.activity_pin_yin)
public class PinYinActivity extends SPBaseActivity implements PinYinSortAdapter.OnclickAddressListener {

    @ViewById(R.id.sidrbar)
    public SideBar sideBar;

    @ViewById(R.id.mTextDialog)
    public TextView mTextDialog;

    private String district;
    private TextView locationTv;
    private List<SPCity> cities;
    private GridView hotCityGrid;
    private ListView sortListView;
    private PinYinSortAdapter mAdapter;
    private SPClearEditText mClearEditText;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private List<SPCity> addresses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.choose_address));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        sideBar.setTextView(mTextDialog);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        View headerView = LayoutInflater.from(this).inflate(R.layout.city_list_header, null);
        locationTv = (TextView) headerView.findViewById(R.id.location_tv);
        LinearLayout hotCityLl = (LinearLayout) headerView.findViewById(R.id.hot_city_ll);
        hotCityGrid = (GridView) headerView.findViewById(R.id.hot_city_grid);
        characterParser = CharacterParser.getInstance();        //实例化汉字转拼音类
        pinyinComparator = new PinyinComparator();
        district = SPSaveData.getString(this, SPMobileConstants.KEY_LOCATION);      //获取定位城市
        if (district.isEmpty()) district = "无法获取";
        locationTv.setText(district);
        String[] addtr = null;
        List<SPServiceConfig> serviceConfigs = SPMobileApplication.getInstance().getServiceConfigs();
        for (SPServiceConfig serviceConfig : serviceConfigs) {
            if (serviceConfig.getConfigID().equals("99"))
                addtr = serviceConfig.getValue().split("\\|");
        }
        if (addtr != null) {
            cities = new ArrayList<>();
            for (String anAddtr : addtr) {
                SPCity city = new SPCity();
                city.setName(anAddtr);
                cities.add(city);
            }
            HotCityAdapter hotCityAdapter = new HotCityAdapter(this, cities);      //设置热门城市
            hotCityGrid.setAdapter(hotCityAdapter);
        } else {
            hotCityLl.setVisibility(View.GONE);
        }
        sortListView.addHeaderView(headerView);
        mAdapter = new PinYinSortAdapter(this, this, addresses);
        sortListView.setAdapter(mAdapter);
        mClearEditText = (SPClearEditText) findViewById(R.id.filter_edit);
    }

    @Override
    public void initEvent() {
        locationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (district.equals("无法获取")) return;
                SPCity city = new SPCity();
                city.setName(((TextView) v).getText().toString());
                checkAddress(city);
            }
        });
        hotCityGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkAddress(cities.get(position));
            }
        });
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mAdapter.getPositionForSection(s.charAt(0));        //该字母首次出现的位置
                if (position != -1) sortListView.setSelection(position + 1);
            }
        });
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAddress(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void initData() {
        loadCityData();
    }

    private void loadCityData() {
        StringBuilder stringBuffer = new StringBuilder();
        String line;
        try {
            AssetManager assetManager = getAssets();
            BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open("city.json")));       //从JSon文件读取数据
            while ((line = br.readLine()) != null)
                stringBuffer.append(line);
            JSONArray cityArray = new JSONArray(stringBuffer.toString());          //将Json文件数据形成JSONArray对象
            addresses = SPJsonUtil.fromJsonArrayToList(cityArray, SPCity.class);
            mAdapter.updateListView(addresses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void searchAddress(String s) {
        List<SPCity> cities = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            cities = addresses;
        } else {
            cities.clear();
            for (SPCity city : addresses) {
                String name = city.getName();
                if (name.contains(s) || characterParser.getSelling(name).startsWith(s))
                    cities.add(city);
            }
        }
        Collections.sort(cities, pinyinComparator);          //根据a-z进行排序
        mAdapter.updateListView(cities);
    }

    @Override
    public void checkAddress(SPCity city) {
        SPSaveData.putValue(this, SPMobileConstants.KEY_LOCATION_ADDRESS, city.getName());
        finish();
    }

}
