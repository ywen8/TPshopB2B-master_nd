package com.tpshop.mall.activity.person.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.ApplySeriverAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.widget.NoScrollGridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_apply)
public class SPApplySeriverActivity extends SPBaseActivity implements View.OnClickListener, ApplySeriverAdapter.AddPicOnclick {

    @ViewById(R.id.apply_rl)
    RelativeLayout applyRl;

    @ViewById(R.id.goodImg)
    ImageView goodImg;               //商品图片

    @ViewById(R.id.goodName)
    TextView goodName;               //商品名称

    @ViewById(R.id.goodPrice)
    TextView goodPrice;              //商品价格

    @ViewById(R.id.goodNum)
    TextView goodNum;                //商品数量

    @ViewById(R.id.connect_btn)
    Button connectBtn;               //联系卖家

    @ViewById(R.id.servicebtn1)
    Button servicebtn1;              //仅退款

    @ViewById(R.id.servicebtn2)
    Button servicebtn2;              //退货退款

    @ViewById(R.id.servicebtn3)
    Button servicebtn3;              //换货

    @ViewById(R.id.servicebtn4)
    Button servicebtn4;              //维修

    @ViewById(R.id.cart_minus_btn)
    Button minusBtn;                 //减少数量

    @ViewById(R.id.cart_count_dtxtv)
    EditText countEtxtv;             //数量

    @ViewById(R.id.cart_plus_btn)
    Button plusBtn;                  //增加数量

    @ViewById(R.id.reportImg)
    ImageView reportImg;             //已收到货

    @ViewById(R.id.noreportImg)
    ImageView noreportImg;           //未收到货

    @ViewById(R.id.reasonView)
    TextView reasonView;

    @ViewById(R.id.reasonTv)
    TextView reasonTv;               //申请原因

    @ViewById(R.id.problemEt)
    EditText problemEt;              //问题描述

    @ViewById(R.id.tvt_sum)
    TextView tvtSum;                 //问题描述字数

    @ViewById(R.id.picGrid)
    NoScrollGridView picGrid;        //照片

    @ViewById(R.id.addImg)
    ImageView addImg;                //添加照片

    @ViewById(R.id.store_address)
    TextView storeAddress;           //联系地址

    @ViewById(R.id.service_phone)
    TextView servicePhone;           //联系电话

    @ViewById(R.id.next_btn)
    Button nextBtn;                  //下一步

    @ViewById(R.id.hasgoodLL)
    LinearLayout hasgoodLL;

    @ViewById(R.id.nogoodLL)
    LinearLayout nogoodLL;

    String recId;
    int applyType;
    SPProduct mProduct;
    private int num = 1;
    private Uri imageUri;
    boolean hasReport = true;
    private String phone = "";
    private List<Bitmap> photos;
    private ApplySeriverAdapter adapter;
    List<File> images = new ArrayList<>();
    private String imageDataPath = "/sdcard/photo";
    final static String imageSavePath = "service.png";
    private static final int REQUEST_CODE_PHOTO = 0x1;
    private static final int REQUEST_CODE_CAMERA = 0x2;
    private Uri desUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "serviceimg.jpg"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCustomerTitle(true, true, "申请售后");
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        applyRl.setVisibility(View.GONE);
        picGrid.setNumColumns(4);
        photos = new ArrayList<>();
        adapter = new ApplySeriverAdapter(this, photos, this);
        picGrid.setAdapter(adapter);
    }

    @Override
    public void initEvent() {
        reasonView.setOnClickListener(this);
        connectBtn.setOnClickListener(this);
        servicebtn1.setOnClickListener(this);
        servicebtn2.setOnClickListener(this);
        servicebtn3.setOnClickListener(this);
        servicebtn4.setOnClickListener(this);
        minusBtn.setOnClickListener(this);
        plusBtn.setOnClickListener(this);
        reportImg.setOnClickListener(this);
        noreportImg.setOnClickListener(this);
        addImg.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        problemEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = problemEt.getText().toString();
                if (content.length() <= 500)
                    tvtSum.setText(content.length() + "/" + 500);
                else
                    showToast("问题描述字数过多");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void initData() {
        applyType = 0;
        if (getIntent() == null || getIntent().getStringExtra("rec_id") == null) {
            showToast(getString(R.string.data_error));
            return;
        }
        recId = getIntent().getStringExtra("rec_id");
        RequestParams params = new RequestParams();
        params.put("rec_id", recId);
        showLoadingSmallToast();
        SPPersonRequest.getApplyWithParameter(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                mProduct = (SPProduct) response;
                refreshView();
                applyRl.setVisibility(View.VISIBLE);
            }
        }, new SPFailuredListener(SPApplySeriverActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
                applyRl.setVisibility(View.GONE);
            }
        });
    }

    private void refreshView() {
        goodName.setText(mProduct.getGoodsName());
        goodPrice.setText("价格：￥" + mProduct.getGoodsPrice());
        num = mProduct.getGoodsNum();
        goodNum.setText("数量：x" + num);
        countEtxtv.setText(num + "");
        String address = SPStringUtils.isEmpty(mProduct.getStoreAddress()) ? "" : mProduct.getStoreAddress();
        phone = SPStringUtils.isEmpty(mProduct.getServicePhone()) ? "" : mProduct.getServicePhone();
        storeAddress.setText(address);
        servicePhone.setText(phone);
        String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, mProduct.getGoodsID());
        Glide.with(SPApplySeriverActivity.this).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(goodImg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_btn:      //联系卖家
                if (!SPStringUtils.isEmpty(phone)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    if (intent.resolveActivity(getPackageManager()) != null)
                        startActivity(intent);
                } else {
                    showToast(getString(R.string.no_contact));
                }
                break;
            case R.id.reasonView:       //选择申请原因
                selectReason();
                break;
            case R.id.servicebtn1:
                setService(true, false, false, false);
                break;
            case R.id.servicebtn2:
                setService(false, true, false, false);
                break;
            case R.id.servicebtn3:
                setService(false, false, true, false);
                break;
            case R.id.servicebtn4:
                setService(false, false, false, true);
                break;
            case R.id.cart_minus_btn:
                minCount();
                break;
            case R.id.cart_plus_btn:
                plusCount();
                break;
            case R.id.reportImg:
                checkReport(true);
                break;
            case R.id.noreportImg:
                checkReport(false);
                break;
            case R.id.addImg:
                selectImage();
                break;
            case R.id.next_btn:
                apply();
                break;
        }
    }

    private void selectReason() {
        final String[] items = getResources().getStringArray(R.array.apply_reason);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择原因");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                reasonTv.setText(items[item]);
            }
        });
        builder.show();
    }

    private void minCount() {
        int count = Integer.parseInt(countEtxtv.getText().toString());
        if (count <= 1)
            return;
        else
            count--;
        countEtxtv.setText(count + "");
    }

    private void plusCount() {
        int count = Integer.parseInt(countEtxtv.getText().toString());
        if (count >= num)
            return;
        else
            count++;
        countEtxtv.setText(count + "");
    }

    private void setService(boolean a, boolean b, boolean c, boolean d) {
        if (a) {
            applyType = 0;
            servicebtn1.setBackgroundResource(R.drawable.red_btn_shape);
            servicebtn2.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn3.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn4.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn1.setTextColor(getResources().getColor(R.color.light_red));
            servicebtn2.setTextColor(getResources().getColor(R.color.person_info_text));
            servicebtn3.setTextColor(getResources().getColor(R.color.person_info_text));
            servicebtn4.setTextColor(getResources().getColor(R.color.person_info_text));
            nogoodLL.setVisibility(View.VISIBLE);
        } else if (b) {
            applyType = 1;
            servicebtn1.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn2.setBackgroundResource(R.drawable.red_btn_shape);
            servicebtn3.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn4.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn1.setTextColor(getResources().getColor(R.color.person_info_text));
            servicebtn2.setTextColor(getResources().getColor(R.color.light_red));
            servicebtn3.setTextColor(getResources().getColor(R.color.person_info_text));
            servicebtn4.setTextColor(getResources().getColor(R.color.person_info_text));
            nogoodLL.setVisibility(View.GONE);
            checkReport(true);
        } else if (c) {
            applyType = 2;
            servicebtn1.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn2.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn3.setBackgroundResource(R.drawable.red_btn_shape);
            servicebtn4.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn1.setTextColor(getResources().getColor(R.color.person_info_text));
            servicebtn2.setTextColor(getResources().getColor(R.color.person_info_text));
            servicebtn3.setTextColor(getResources().getColor(R.color.light_red));
            servicebtn4.setTextColor(getResources().getColor(R.color.person_info_text));
            nogoodLL.setVisibility(View.VISIBLE);
        } else if (d) {
            applyType = 3;
            servicebtn1.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn2.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn3.setBackgroundResource(R.drawable.white_btn_shape);
            servicebtn4.setBackgroundResource(R.drawable.red_btn_shape);
            servicebtn1.setTextColor(getResources().getColor(R.color.person_info_text));
            servicebtn2.setTextColor(getResources().getColor(R.color.person_info_text));
            servicebtn3.setTextColor(getResources().getColor(R.color.person_info_text));
            servicebtn4.setTextColor(getResources().getColor(R.color.light_red));
            nogoodLL.setVisibility(View.GONE);
            checkReport(true);
        }
    }

    private void checkReport(boolean a) {
        if (a) {
            hasReport = true;
            reportImg.setImageResource(R.drawable.radio_checked);
            noreportImg.setImageResource(R.drawable.radio_nocheck);
        } else {
            hasReport = false;
            reportImg.setImageResource(R.drawable.radio_nocheck);
            noreportImg.setImageResource(R.drawable.radio_checked);
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
                        File outputImage = new File(Environment.getExternalStorageDirectory(), "service.jpg");
                        try {
                            if (outputImage.exists())
                                outputImage.delete();
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (Build.VERSION.SDK_INT >= 24)
                                imageUri = FileProvider.getUriForFile(SPApplySeriverActivity.this, "com.tpshop.mall.fileprovider", outputImage);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PHOTO:                            //相册选择后的图片
                if (resultCode == RESULT_OK)
                    shearPhoto(data.getData());                 //剪切图片
                break;
            case REQUEST_CODE_CAMERA:                           //拍照后的图片
                if (resultCode == RESULT_OK) {
                    File fileTemp = new File(Environment.getExternalStorageDirectory(), "service.jpg");
                    shearPhoto(Uri.fromFile(fileTemp));         //剪切图片
                }
                break;
            case SPMobileConstants.Result_Code_GetPicture:      //剪切后的图片
                Bitmap mBitmap;
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), desUri);
                } catch (Exception e) {
                    e.printStackTrace();
                    mBitmap = null;
                }
                if (mBitmap != null) {
                    saveImage(mBitmap);                         //保存照片
                    photos.add(mBitmap);
                    if (photos.size() >= 5) {
                        addImg.setVisibility(View.GONE);
                        picGrid.setNumColumns(5);
                    } else {
                        addImg.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void shearPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= 24)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, SPMobileConstants.Result_Code_GetPicture);
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
        images.add(new File(fileName));
    }

    /**
     * 申请售后
     */
    private void apply() {
        String error = "";
        String count = countEtxtv.getText().toString();
        String reason = reasonTv.getText().toString();
        String problem = problemEt.getText().toString();
        if (SPStringUtils.isEmpty(reason)) {
            error = "请选择退换货原因";
        } else if (problem.trim().isEmpty()) {
            error = "请输入问题描述";
        } else if (problem.length() > 500) {
            error = "问题内容过长";
        }
        if (!SPStringUtils.isEmpty(error)) {
            showToast(error);
            return;
        }
        RequestParams params = new RequestParams();
        params.put("rec_id", recId);
        params.put("goods_id", mProduct.getGoodsID());
        params.put("order_id", mProduct.getOrderID());
        params.put("order_sn", mProduct.getOrderSN());
        params.put("spec_key", mProduct.getSpecKey());
        params.put("goods_num", count);
        params.put("type", applyType);
        params.put("reason", reason);
        params.put("describe", problem);
        if (hasReport && images.size() <= 0) {
            showToast("请上传照片");
            return;
        }
        showLoadingSmallToast();
        SPPersonRequest.exchangeApplyWithParameter(params, images, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                setResult(RESULT_OK);
                finish();
            }
        }, new SPFailuredListener(SPApplySeriverActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    private String getSavePathName() {
        return imageDataPath + "/" + System.currentTimeMillis() + "_" + imageSavePath;      //图片名字
    }

    @Override
    public void addPic(int position) {
        addImg.setVisibility(View.VISIBLE);
        picGrid.setNumColumns(4);
        images.remove(position);
    }

}
