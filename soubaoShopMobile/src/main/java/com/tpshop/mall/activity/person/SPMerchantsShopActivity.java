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
 * Description: 商品收藏列表
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.person;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.activity.person.address.SPCitySelectActivity_;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.model.person.SPMerchantsType;
import com.tpshop.mall.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zw
 */
@EActivity(R.layout.activity_spmerchants_shop)
public class SPMerchantsShopActivity extends SPBaseActivity implements View.OnClickListener {

    @ViewById(R.id.merchants_img2)
    ImageView merchantsImg2;

    @ViewById(R.id.merchants_img3)
    ImageView merchantsImg3;

    @ViewById(R.id.merchants_img4)
    ImageView merchantsImg4;

    @ViewById(R.id.merchants_txt2)
    TextView merchantsTxt2;

    @ViewById(R.id.merchants_txt3)
    TextView merchantsTxt3;

    @ViewById(R.id.merchants_txt4)
    TextView merchantsTxt4;

    @ViewById(R.id.linearLayout1)
    LinearLayout linearLayout1;

    @ViewById(R.id.merchants_notice_ll)
    LinearLayout merchantsNoticeLl;

    @ViewById(R.id.close_imgv)
    ImageView closeImg;

    @ViewById(R.id.saller_name_et)
    EditText sallerNameEt;

    @ViewById(R.id.store_phone_et)
    EditText storePhoneEt;

    @ViewById(R.id.store_address_rl)
    RelativeLayout storeAddressRl;

    @ViewById(R.id.store_district_tv)
    TextView storeDistrictTv;

    @ViewById(R.id.address_detail_et)
    EditText addressDetailEt;

    @ViewById(R.id.agree_img)
    ImageView agreeImg;

    @ViewById(R.id.btn_next)
    Button btnNext;

    @ViewById(R.id.linearLayout2)
    LinearLayout linearLayout2;

    @ViewById(R.id.store_name_et)
    EditText storeNameEt;

    @ViewById(R.id.store_name_img)
    ImageView storeNameImg;

    @ViewById(R.id.store_login_et)
    EditText storeLoginEt;

    @ViewById(R.id.store_type_rl)
    RelativeLayout storeTypeRl;

    @ViewById(R.id.store_type_tv)
    TextView storeTypeTv;

    @ViewById(R.id.saller_type_rl)
    RelativeLayout sallerTypeRl;

    @ViewById(R.id.saller_type_tv)
    TextView sallerTypeTv;

    @ViewById(R.id.btn_next2)
    Button btnNext2;

    @ViewById(R.id.scrollView3)
    ScrollView scrollView3;

    @ViewById(R.id.photo_img)
    ImageView photoImg;

    @ViewById(R.id.merchants_btn_img)
    ImageView merchantsBtnImg;

    @ViewById(R.id.time_validity_start)
    RelativeLayout timeValidityStart;

    @ViewById(R.id.time_start_txt)
    TextView timeStartTxt;

    @ViewById(R.id.time_validity_end)
    RelativeLayout timeValidityEnd;

    @ViewById(R.id.time_end_txt)
    TextView timeEndTxt;

    @ViewById(R.id.regist_et1)
    EditText registEt1;

    @ViewById(R.id.regist_et2)
    EditText registEt2;

    @ViewById(R.id.btn_submit)
    Button btnSubmit;

    @ViewById(R.id.relativeLayout4)
    RelativeLayout relativeLayout4;

    @ViewById(R.id.back_btn)
    Button backBtn;

    private int ef;
    private int type;
    private File imgFile;
    private Uri imageUri;
    public Bitmap mBitmap;
    private boolean ischeck;
    public String merchants;
    private Calendar calendar;
    private boolean isEffective = true;
    private SPMerchantsType merchantsType;
    private String imageDataPath = "/sdcard/photo";
    private SPConsigneeAddress selectRegionConsignee;
    private static final int REQUEST_CODE_PHOTO = 0x1;
    private static final int REQUEST_CODE_CAMERA = 0x2;
    final static String imageSavePath = "merchants.jpg";

    @Override
    protected void onCreate(Bundle bundle) {
        setCustomerTitle(true, true, "商家入驻");
        super.onCreate(bundle);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        merchants = getIntent().getStringExtra("merchants");
        switch (merchants) {
            case "1":
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.GONE);
                scrollView3.setVisibility(View.GONE);
                relativeLayout4.setVisibility(View.GONE);
                break;
            case "2":
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);
                scrollView3.setVisibility(View.GONE);
                relativeLayout4.setVisibility(View.GONE);
                merchantsImg2.setImageResource(R.drawable.merchants_shop2_red);
                merchantsTxt2.setTextColor(getResources().getColor(R.color.light_red));
                break;
            case "3":
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                scrollView3.setVisibility(View.VISIBLE);
                relativeLayout4.setVisibility(View.GONE);
                merchantsImg2.setImageResource(R.drawable.merchants_shop2_red);
                merchantsTxt2.setTextColor(getResources().getColor(R.color.light_red));
                merchantsImg3.setImageResource(R.drawable.merchants_shop3_red);
                merchantsTxt3.setTextColor(getResources().getColor(R.color.light_red));
                break;
            case "4":
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                scrollView3.setVisibility(View.GONE);
                relativeLayout4.setVisibility(View.VISIBLE);
                merchantsImg2.setImageResource(R.drawable.merchants_shop2_red);
                merchantsTxt2.setTextColor(getResources().getColor(R.color.light_red));
                merchantsImg3.setImageResource(R.drawable.merchants_shop3_red);
                merchantsTxt3.setTextColor(getResources().getColor(R.color.light_red));
                merchantsImg4.setImageResource(R.drawable.merchants_shop4_red);
                merchantsTxt4.setTextColor(getResources().getColor(R.color.light_red));
                break;
        }
    }

    @Override
    public void initEvent() {
        btnNext.setOnClickListener(this);
        btnNext2.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        closeImg.setOnClickListener(this);
        agreeImg.setOnClickListener(this);
        storeNameImg.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        storeAddressRl.setOnClickListener(this);
        photoImg.setOnClickListener(this);
        merchantsBtnImg.setOnClickListener(this);
        timeValidityStart.setOnClickListener(this);
        timeValidityEnd.setOnClickListener(this);
        storeTypeRl.setOnClickListener(this);
        sallerTypeRl.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                startStore1();
                break;
            case R.id.btn_next2:
                startStore2();
                break;
            case R.id.btn_submit:
                startStore3();
                break;
            case R.id.close_imgv:
                merchantsNoticeLl.setVisibility(View.GONE);
                break;
            case R.id.agree_img:
                if (ischeck) {
                    ischeck = false;
                    agreeImg.setImageResource(R.drawable.radio_nocheck);
                } else {
                    ischeck = true;
                    agreeImg.setImageResource(R.drawable.radio_checked);
                }
                break;
            case R.id.store_name_img:
                storeNameEt.setText("");
                break;
            case R.id.back_btn:
                Intent intent = new Intent(this, SPMainActivity.class);
                intent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_HOME);
                startActivity(intent);
                break;
            case R.id.store_address_rl:
                Intent regionIntent = new Intent(this, SPCitySelectActivity_.class);
                regionIntent.putExtra("isShowTown", false);           //false表示不需要显示街道,不带默认是true,显示街道
                startActivityForResult(regionIntent, SPMobileConstants.Result_Code_GetValue);
                break;
            case R.id.store_type_rl:
                selectType();
                break;
            case R.id.saller_type_rl:
                startActivityForResult(new Intent(this, SPTypeSelectActivity_.class), 1);
                break;
            case R.id.merchants_btn_img:
                if (isEffective) {
                    ef = 1;
                    isEffective = false;
                    merchantsBtnImg.setImageResource(R.drawable.merchants_btn_open);
                    timeValidityEnd.setVisibility(View.GONE);
                } else {
                    ef = 0;
                    isEffective = true;
                    merchantsBtnImg.setImageResource(R.drawable.merchants_btn_close);
                    timeValidityEnd.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.photo_img:
                selectImage();
                break;
            case R.id.time_validity_start:
                showDateDialog();
                break;
            case R.id.time_validity_end:
                showDateDialog2();
                break;
        }
    }

    private void selectImage() {
        final String[] items = getResources().getStringArray(R.array.user_head_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传照片");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:      //拍照
                        File outputImage = new File(Environment.getExternalStorageDirectory(), "merchants.jpg");
                        try {
                            if (outputImage.exists())
                                outputImage.delete();
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (Build.VERSION.SDK_INT >= 24)
                                imageUri = FileProvider.getUriForFile(SPMerchantsShopActivity.this, "com.tpshop.mall.fileprovider", outputImage);
                            else
                                imageUri = Uri.fromFile(outputImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        Intent intent_pat = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent_pat.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent_pat, REQUEST_CODE_CAMERA);
                        break;
                    case 1:      //相册
                        Intent intent_photo = new Intent(Intent.ACTION_PICK, null);
                        intent_photo.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent_photo, REQUEST_CODE_PHOTO);
                        break;
                }
            }
        });
        builder.show();
    }

    private void selectType() {
        final String[] items = getResources().getStringArray(R.array.store_type);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择类型");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                type = item + 1;
                storeTypeTv.setText(items[item]);
            }
        });
        builder.show();
    }

    private void showDateDialog() {
        if (calendar == null)
            calendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year, month, day);
                timeStartTxt.setText(getString(R.string.user_age_format, year, month + 1, day));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showDateDialog2() {
        if (calendar == null)
            calendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year, month, day);
                timeEndTxt.setText(getString(R.string.user_age_format, year, month + 1, day));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    //填写信息第一步
    private void startStore1() {
        if (sallerNameEt.getText().toString().trim().isEmpty()) {
            showToast("请填写姓名");
            return;
        }
        if (storePhoneEt.getText().toString().trim().isEmpty()) {
            showToast("请填写手机号");
            return;
        }
        if (storeDistrictTv.getText().toString().isEmpty()) {
            showToast("请选择所在地区");
            return;
        }
        if (addressDetailEt.getText().toString().trim().isEmpty()) {
            showToast("请填写详细地址");
            return;
        }
        if (!SPUtils.isPhoneLegal(storePhoneEt.getText().toString())) {
            showToast("手机号格式错误");
            return;
        }
        if (ischeck) {
            RequestParams params = new RequestParams();
            params.put("contacts_name", sallerNameEt.getText().toString());
            params.put("contacts_mobile", storePhoneEt.getText().toString());
            params.put("company_province", selectRegionConsignee.getProvince());
            params.put("company_city", selectRegionConsignee.getCity());
            params.put("company_district", selectRegionConsignee.getDistrict());
            params.put("company_address", addressDetailEt.getText().toString());
            SPUserRequest.setbasicInfo1(params, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    scrollView3.setVisibility(View.GONE);
                    relativeLayout4.setVisibility(View.GONE);
                    merchantsImg2.setImageResource(R.drawable.merchants_shop2_red);
                    merchantsTxt2.setTextColor(getResources().getColor(R.color.light_red));
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    showFailedToast(msg);
                }
            });
        } else {
            showToast("您还没有同意协议");
        }
    }

    //填写信息第二步
    private void startStore2() {
        if (storeNameEt.getText().toString().trim().isEmpty()) {
            showToast("请输入店铺名称");
            return;
        }
        if (storeLoginEt.getText().toString().trim().isEmpty()) {
            showToast("请填写登录名");
            return;
        }
        if (storeTypeTv.getText().toString().isEmpty()) {
            showToast("请选择店铺类型");
            return;
        }
        if (sallerTypeTv.getText().toString().isEmpty()) {
            showToast("请选择经营类目");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("store_name", storeNameEt.getText().toString());
        params.put("seller_name", storeLoginEt.getText().toString());
        params.put("store_type", type);
        List<String> ids = new ArrayList<>();
        for (int id : merchantsType.getThirdTypeId())
            ids.add(merchantsType.getFirstTypeId() + "," + merchantsType.getSecondTypeId() + "," + id);
        params.put("store_class_ids", ids);
        SPUserRequest.setbasicInfo2(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                scrollView3.setVisibility(View.VISIBLE);
                relativeLayout4.setVisibility(View.GONE);
                merchantsImg3.setImageResource(R.drawable.merchants_shop3_red);
                merchantsTxt3.setTextColor(getResources().getColor(R.color.light_red));
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    //填写信息第三步
    private void startStore3() {
        if (imgFile == null) {
            showToast("请上传照片");
            return;
        }
        if (timeStartTxt.getText().toString().equals("请选择")) {
            showToast("请选择开始时间");
            return;
        }
        if (ef == 0 && timeEndTxt.getText().toString().equals("请选择")) {
            showToast("请选择结束时间");
            return;
        }
        if (registEt1.getText().toString().trim().isEmpty()) {
            showToast("请填写营业执照号");
            return;
        }
        if (registEt2.getText().toString().trim().isEmpty()) {
            showToast("请填写字号名称");
            return;
        }
        RequestParams params = new RequestParams();
        try {
            params.put("business_licence_cert", imgFile, "image/jpg");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        params.put("business_date_start", timeStartTxt.getText().toString().replaceAll(" ", ""));
        if (ef == 1)
            params.put("business_date_end", timeEndTxt.getText().toString().replaceAll(" ", ""));
        params.put("business_permanent", ef);
        params.put("business_licence_number", registEt1.getText().toString());
        params.put("legal_person", registEt2.getText().toString());
        SPUserRequest.setbasicInfo3(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                scrollView3.setVisibility(View.GONE);
                relativeLayout4.setVisibility(View.VISIBLE);
                merchantsImg4.setImageResource(R.drawable.merchants_shop4_red);
                merchantsTxt4.setTextColor(getResources().getColor(R.color.light_red));
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_CODE_PHOTO) {      //相册选择后的图片
                    try {
                        mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    } catch (Exception e) {
                        e.printStackTrace();
                        mBitmap = null;
                    }
                } else if (requestCode == REQUEST_CODE_CAMERA) {      //拍照后的图片
                    File fileTemp = new File(Environment.getExternalStorageDirectory(), "merchants.jpg");
                    try {
                        mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(fileTemp));
                    } catch (Exception e) {
                        e.printStackTrace();
                        mBitmap = null;
                    }
                }
                if (mBitmap != null) {
                    saveImage(mBitmap);                  //保存照片
                    photoImg.setImageBitmap(mBitmap);
                }
                break;
            case SPMobileConstants.Result_Code_GetValue:
                if (data == null || data.getSerializableExtra("consignee") == null) return;
                selectRegionConsignee = (SPConsigneeAddress) data.getSerializableExtra("consignee");
                String fullRegion = selectRegionConsignee.getProvinceLabel() + " " + selectRegionConsignee.getCityLabel()
                        + " " + selectRegionConsignee.getDistrictLabel();
                storeDistrictTv.setText(fullRegion);
                break;
            case 101:
                if (data == null || data.getSerializableExtra("merchantsType") == null) return;
                merchantsType = (SPMerchantsType) data.getSerializableExtra("merchantsType");
                String string = merchantsType.getThirdType().replaceAll(" ", "、");
                string = string.substring(0, string.length() - 1);
                sallerTypeTv.setText(string);
                break;
        }
    }

    private void saveImage(Bitmap bitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {             //检测SD卡的可用性
            showToast("SD存储卡不可用");
            return;
        }
        FileOutputStream fos = null;
        File file = new File(imageDataPath);
        file.mkdirs();                                                 //创建文件夹
        String fileName = getSavePathName();                           //图片名字
        try {
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);      //压缩后写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgFile = new File(fileName);
    }

    private String getSavePathName() {
        return imageDataPath + "/" + System.currentTimeMillis() + "_" + imageSavePath;      //图片名字
    }

}
