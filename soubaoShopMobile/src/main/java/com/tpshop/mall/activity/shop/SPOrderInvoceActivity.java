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
 * Description: 品牌街
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.OrderInvoceAdapter;
import com.tpshop.mall.adapter.SPSearchkeyListAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPSaveData;
import com.tpshop.mall.model.order.SPInvoce;
import com.tpshop.mall.widget.NoScrollListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * @author 飞龙
 */
@EActivity(R.layout.order_invoce)
public class SPOrderInvoceActivity extends SPBaseActivity implements OrderInvoceAdapter.ItemOnclick, View.OnClickListener {

    @ViewById(R.id.needInvoce_txt)
    Button needInvoceTxt;

    @ViewById(R.id.noInvoce_txt)
    Button noInvoceTxt;

    @ViewById(R.id.invoce_Ll)
    LinearLayout invoceLl;

    @ViewById(R.id.person_ll)
    LinearLayout personLl;

    @ViewById(R.id.company_ll)
    LinearLayout companyLl;

    @ViewById(R.id.person_img)
    ImageView personImg;

    @ViewById(R.id.company_img)
    ImageView companyImg;

    @ViewById(R.id.invoce_user_ll)
    LinearLayout invoceUserLl;

    @ViewById(R.id.company_name_et)
    EditText companyNameEt;

    @ViewById(R.id.name_lv)
    NoScrollListView nameLv;

    @ViewById(R.id.invoce_code_et)
    EditText invoceCodeEt;

    @ViewById(R.id.code_lv)
    NoScrollListView codeLv;

    @ViewById(R.id.invoce_list)
    NoScrollListView invoceList;

    @ViewById(R.id.sure_btn)
    Button sureBtn;

    private boolean check;
    private String invoceContent;
    private boolean hasInvoce = true;
    private OrderInvoceAdapter adapter;
    private ArrayList<SPInvoce> spInvoces;
    private ArrayList<String> nameRecords;
    private ArrayList<String> codeRecords;
    private SPSearchkeyListAdapter mAdapter1, mAdapter2;

    @Override
    protected void onCreate(Bundle bundle) {
        super.setCustomerTitle(true, true, "设置发票信息");
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
        needInvoceTxt.setOnClickListener(this);
        noInvoceTxt.setOnClickListener(this);
        personLl.setOnClickListener(this);
        companyLl.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
        companyNameEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String content = companyNameEt.getText().toString().trim();
                if (hasFocus && content.isEmpty())
                    nameLv.setVisibility(View.VISIBLE);
                else
                    nameLv.setVisibility(View.GONE);
            }
        });
        companyNameEt.addTextChangedListener(watcher1);
        nameLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) mAdapter1.getItem(position);
                companyNameEt.setText(name);
                nameLv.setVisibility(View.GONE);
            }
        });
        invoceCodeEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String content = invoceCodeEt.getText().toString().trim();
                if (hasFocus && content.isEmpty())
                    codeLv.setVisibility(View.VISIBLE);
                else
                    codeLv.setVisibility(View.GONE);
            }
        });
        invoceCodeEt.addTextChangedListener(watcher2);
        codeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) mAdapter2.getItem(position);
                invoceCodeEt.setText(name);
                codeLv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void initData() {
        final String[] items = getResources().getStringArray(R.array.order_invoce);
        invoceContent = items[0];
        spInvoces = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            SPInvoce spInvoce = new SPInvoce();
            spInvoce.setName(items[i]);
            if (i == 0)
                spInvoce.setCheck(true);
            else
                spInvoce.setCheck(false);
            spInvoces.add(spInvoce);
        }
        adapter = new OrderInvoceAdapter(this, spInvoces, this);
        invoceList.setAdapter(adapter);
        nameRecords = new ArrayList<>();
        loadname();              //获取缓存单位名称
        mAdapter1 = new SPSearchkeyListAdapter(this);
        mAdapter1.setData(nameRecords);
        nameLv.setAdapter(mAdapter1);
        codeRecords = new ArrayList<>();
        loadcode();              //获取缓存纳税人识别号
        mAdapter2 = new SPSearchkeyListAdapter(this);
        mAdapter2.setData(codeRecords);
        codeLv.setAdapter(mAdapter2);
    }

    public void loadname() {
        nameRecords.clear();
        String searchKey = SPSaveData.getString(this, SPMobileConstants.KEY_NAME_KEY);
        if (!searchKey.isEmpty()) {
            String[] keys = searchKey.split(",");
            if (keys != null) {
                for (String key : keys) {
                    if (!key.isEmpty())
                        nameRecords.add(key);
                }
            }
        }
    }

    public void loadcode() {
        codeRecords.clear();
        String searchKey = SPSaveData.getString(this, SPMobileConstants.KEY_CODE_KEY);
        if (!searchKey.isEmpty()) {
            String[] keys = searchKey.split(",");
            if (keys != null) {
                for (String key : keys) {
                    if (!key.isEmpty())
                        codeRecords.add(key);
                }
            }
        }
    }

    @Override
    public void click(int position) {
        SPInvoce spInvoce = spInvoces.get(position);
        invoceContent = spInvoce.getName();
        for (SPInvoce invoce : spInvoces) {
            invoce.setCheck(false);
            if (spInvoce == invoce)
                invoce.setCheck(true);
        }
        adapter.notifyDataSetChanged();
        nameLv.setVisibility(View.GONE);
        codeLv.setVisibility(View.GONE);
        companyNameEt.clearFocus();
        invoceCodeEt.clearFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.needInvoce_txt:         //开发票
                hasInvoce = true;
                invoceLl.setVisibility(View.VISIBLE);
                needInvoceTxt.setBackgroundResource(R.drawable.red_btn_shape);
                needInvoceTxt.setTextColor(getResources().getColor(R.color.light_red));
                noInvoceTxt.setBackgroundResource(R.drawable.white_btn_shape);
                noInvoceTxt.setTextColor(getResources().getColor(R.color.person_info_text));
                break;
            case R.id.noInvoce_txt:           //不开发票
                hasInvoce = false;
                invoceLl.setVisibility(View.GONE);
                needInvoceTxt.setBackgroundResource(R.drawable.white_btn_shape);
                needInvoceTxt.setTextColor(getResources().getColor(R.color.person_info_text));
                noInvoceTxt.setBackgroundResource(R.drawable.red_btn_shape);
                noInvoceTxt.setTextColor(getResources().getColor(R.color.light_red));
                break;
            case R.id.person_ll:              //个人
                check = false;
                invoceUserLl.setVisibility(View.GONE);
                personImg.setImageResource(R.drawable.radio_checked);
                companyImg.setImageResource(R.drawable.radio_nocheck);
                break;
            case R.id.company_ll:             //单位
                check = true;
                invoceUserLl.setVisibility(View.VISIBLE);
                personImg.setImageResource(R.drawable.radio_nocheck);
                companyImg.setImageResource(R.drawable.radio_checked);
                break;
            case R.id.sure_btn:               //确定
                Intent intent = new Intent();
                if (hasInvoce) {
                    if (check) {
                        String companyName = companyNameEt.getText().toString();
                        String invoceCode = invoceCodeEt.getText().toString();
                        if (companyName.trim().isEmpty()) {
                            showToast("请填写单位名称");
                            return;
                        } else {
                            if (!invoceCode.trim().isEmpty()) {
                                intent.putExtra("taxpayer", invoceCode);
                                saveCode(invoceCode);
                            }
                            intent.putExtra("invoce_title", companyName);
                            intent.putExtra("invoce_desc", invoceContent);
                            saveName(companyName);
                        }
                    } else {
                        intent.putExtra("invoce_title", "个人");
                        intent.putExtra("invoce_desc", invoceContent);
                    }
                }
                setResult(SPMobileConstants.Result_Code_GetInvoce, intent);
                finish();
                break;
        }
    }

    public void saveName(String key) {
        String searchKey = SPSaveData.getString(this, SPMobileConstants.KEY_NAME_KEY);
        if (!searchKey.isEmpty() && !searchKey.contains(key)) {
            searchKey = key + "," + searchKey;
        } else if (SPStringUtils.isEmpty(searchKey)) {
            searchKey = key;
        }
        SPSaveData.putValue(this, SPMobileConstants.KEY_NAME_KEY, searchKey);
    }

    public void saveCode(String key) {
        String searchKey = SPSaveData.getString(this, SPMobileConstants.KEY_CODE_KEY);
        if (!searchKey.isEmpty() && !searchKey.contains(key)) {
            searchKey = key + "," + searchKey;
        } else if (SPStringUtils.isEmpty(searchKey)) {
            searchKey = key;
        }
        SPSaveData.putValue(this, SPMobileConstants.KEY_CODE_KEY, searchKey);
    }

    TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().isEmpty())
                nameLv.setVisibility(View.GONE);
            else
                nameLv.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher watcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().isEmpty())
                codeLv.setVisibility(View.GONE);
            else
                codeLv.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}
