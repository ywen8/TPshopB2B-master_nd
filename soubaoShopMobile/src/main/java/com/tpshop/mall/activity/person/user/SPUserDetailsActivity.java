/*
 * tpshop
 * ============================================================================
 * * 版权所有 2015-2027 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: Ben  16/07/11 $
 * $description: 商城 -> 我的 -> 头像 -> 个人资料
 */
package com.tpshop.mall.activity.person.user;

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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.activity.person.address.SPConsigneeAddressListActivity_;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.person.SPUser;
import com.tpshop.mall.utils.SPUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

@EActivity(R.layout.activity_spuser_details)
public class SPUserDetailsActivity extends SPBaseActivity implements View.OnClickListener {

    @ViewById(R.id.head_mimgv)
    SimpleDraweeView headImage;

    @ViewById(R.id.phone_num_txt)
    TextView phoneNumTxt;

    @ViewById(R.id.nickname_txtv)
    TextView nickName;

    @ViewById(R.id.sex_txtv)
    TextView sexTxt;

    @ViewById(R.id.age_txtv)
    TextView ageTxt;

    @ViewById(R.id.mail_detail_txt)
    TextView mailTxt;

    @ViewById(R.id.modify_pwd_title_txt)
    TextView modifyPwdTxt;

    @ViewById(R.id.pay_pwd_title_txt)
    TextView payPwdTxt;

    @ViewById(R.id.address_title_txt)
    TextView addressTxt;

    @ViewById(R.id.btn_save)
    Button btnSave;

    Bitmap mBitmap;
    long birthday = 0;
    Calendar calendar;
    SPUser mUser = null;
    private Uri imageUri;
    private String strSex;
    private String[] sexA = null;
    private static final int REQUEST_CODE_GENDER = 0x101;
    private static final int REQUEST_CODE_NICK = 0x102;
    private static final int REQUEST_CODE_EMAIL = 0x103;
    private static final int REQUEST_CODE_MOBILE = 0x104;
    private static final int REQUEST_CODE_PHOTO = 0x1;
    private static final int REQUEST_CODE_CAMERA = 0x2;
    private static final int REQUEST_GET_CROP = 0x3;
    private String path = Environment.getExternalStorageDirectory().getPath();
    private Uri desUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "headimg.jpg"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_user_info));
        super.onCreate(savedInstanceState);
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
        headImage.setOnClickListener(this);
        phoneNumTxt.setOnClickListener(this);
        nickName.setOnClickListener(this);
        sexTxt.setOnClickListener(this);
        ageTxt.setOnClickListener(this);
        mailTxt.setOnClickListener(this);
        modifyPwdTxt.setOnClickListener(this);
        payPwdTxt.setOnClickListener(this);
        addressTxt.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void initData() {
        sexA = getResources().getStringArray(R.array.user_sex_name);
        mUser = SPMobileApplication.getInstance().getLoginUser();
        if (mUser != null) {
            try {
                birthday = Long.parseLong(mUser.getBirthday() == null ? "0" : mUser.getBirthday());
                calendar = Calendar.getInstance();
                if (birthday != 0)
                    calendar.setTimeInMillis(birthday * 1000);
                ageTxt.setText(getString(R.string.user_age_format, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            strSex = mUser.getSex();
            sexTxt.setText(sexA[stringToInt(mUser.getSex(), 0)]);
            nickName.setText(mUser.getNickName());
            mailTxt.setText(mUser.getEmail());
            phoneNumTxt.setText(mUser.getMobile());
            if (SPMobileApplication.getInstance().isLogined()) {
                SPUser user = SPMobileApplication.getInstance().getLoginUser();
                String headPic = user.getHeadPic();
                if (!SPStringUtils.isEmpty(headPic)) {
                    headPic = SPUtils.getImageUrl(headPic);
                    SPMobileConstants.KEY_HEAD_PIC = headPic;
                    headImage.setImageURI(SPUtils.getImageUri(this, SPMobileConstants.KEY_HEAD_PIC));
                }
            }
        }
    }

    private int stringToInt(String str, int defaultValue) {
        int res = defaultValue;
        try {
            res = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sex_txtv:
                Intent ageIntent = new Intent(this, SPUserGenderActivity_.class);
                ageIntent.putExtra("data", sexA);
                ageIntent.putExtra("defaultIndex", sexToIndex(sexTxt.getText().toString()));
                startActivityForResult(ageIntent, REQUEST_CODE_GENDER);
                break;
            case R.id.nickname_txtv:
                Intent invokeIntent = new Intent(this, SPChangeNickNameActivity_.class);
                invokeIntent.putExtra("value", mUser.getNickName());
                startActivityForResult(invokeIntent, REQUEST_CODE_NICK);
                break;
            case R.id.age_txtv:
                showDateDialog();
                break;
            case R.id.phone_num_txt:
                Intent phoneIntent = new Intent(this, SPChangeMobileActivity_.class);
                phoneIntent.putExtra("value", mUser.getMobile());
                startActivityForResult(phoneIntent, REQUEST_CODE_MOBILE);
                break;
            case R.id.mail_detail_txt:
                Intent emailIntent = new Intent(this, SPChangeEmailActivity_.class);
                emailIntent.putExtra("value", mUser.getEmail());
                startActivityForResult(emailIntent, REQUEST_CODE_EMAIL);
                break;
            case R.id.modify_pwd_title_txt:
                Intent modifyPwdIntent = new Intent(this, SPModifyPasswordActivity_.class);
                startActivity(modifyPwdIntent);
                break;
            case R.id.pay_pwd_title_txt:
                Intent pwdIntent = new Intent(this, SPPayPwdActivity_.class);
                pwdIntent.putExtra("value", mUser.getMobile());
                startActivity(pwdIntent);
                break;
            case R.id.address_title_txt:
                Intent addressIntent = new Intent(this, SPConsigneeAddressListActivity_.class);
                startActivity(addressIntent);
                break;
            case R.id.head_mimgv:
                selectImage();
                break;
            case R.id.btn_save:
                updateUserInfo();
                break;
        }
    }

    private int sexToIndex(String sex) {
        int index;
        if (sex.equals("女"))
            index = 2;
        else
            index = 1;
        return index;
    }

    private void selectImage() {
        final String[] items = getResources().getStringArray(R.array.user_head_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.user_head_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:      //拍照
                        File outputImage = new File(Environment.getExternalStorageDirectory(), "head.jpg");
                        try {
                            if (outputImage.exists())
                                outputImage.delete();
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (Build.VERSION.SDK_INT >= 24)
                                imageUri = FileProvider.getUriForFile(SPUserDetailsActivity.this, "com.tpshop.mall.fileprovider", outputImage);
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

    private void showDateDialog() {
        if (calendar == null) {
            calendar = Calendar.getInstance();
            if (birthday != 0)
                calendar.setTimeInMillis(birthday);
        }
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year, month, day);
                ageTxt.setText(getString(R.string.user_age_format, year, month + 1, day));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_NICK:
                if (resultCode == RESULT_OK)
                    nickName.setText(data.getStringExtra("value"));
                break;
            case REQUEST_CODE_EMAIL:
                if (resultCode == RESULT_OK)
                    mailTxt.setText(data.getStringExtra("value"));
                break;
            case REQUEST_CODE_MOBILE:
                if (resultCode == RESULT_OK)
                    phoneNumTxt.setText(data.getStringExtra("value"));
                break;
            case REQUEST_CODE_GENDER:
                if (resultCode == RESULT_OK) {
                    int index = data.getIntExtra("index", 0) + 1;
                    strSex = "" + index;
                    sexTxt.setText(sexA[index]);
                }
                break;
            case REQUEST_CODE_PHOTO:                         //相册选择后的图片
                if (resultCode == RESULT_OK)
                    shearPhoto(data.getData());              //剪切图片
                break;
            case REQUEST_CODE_CAMERA:                        //拍照后的图片
                if (resultCode == RESULT_OK) {
                    File fileTemp = new File(Environment.getExternalStorageDirectory(), "head.jpg");
                    shearPhoto(Uri.fromFile(fileTemp));      //剪切图片
                }
                break;
            case REQUEST_GET_CROP:                           //剪切后的图片
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), desUri);
                } catch (Exception e) {
                    e.printStackTrace();
                    mBitmap = null;
                }
                if (mBitmap != null) {
                    setPictureToSD(mBitmap);                 //保存到本地
                    File file = new File(path + "/head.jpg");
                    SPUserRequest.uploadHeadPic(file, new SPSuccessListener() {
                        @Override
                        public void onRespone(String msg, Object response) {
                            showSuccessToast(msg);
                            SPMobileConstants.KEY_HEAD_PIC = path + "/head.jpg";
                            headImage.setImageURI(SPUtils.getImageUri(SPUserDetailsActivity.this, SPMobileConstants.KEY_HEAD_PIC));
                        }
                    }, new SPFailuredListener(SPUserDetailsActivity.this) {
                        @Override
                        public void onRespone(String msg, int errorCode) {
                            showFailedToast(msg);
                        }
                    });
                }
                break;
        }
    }

    private void setPictureToSD(Bitmap bitmap) {
        FileOutputStream fos = null;
        File file = new File(path);
        file.mkdirs();                                                  //创建文件夹
        String fileName = path + "/head.jpg";                           //图片名字
        try {
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);      //压缩后写入文件
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
    }

    /**
     * 直接调用系统的剪切功能
     */
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
        startActivityForResult(intent, REQUEST_GET_CROP);
    }

    private void updateUserInfo() {
        String nickNameStr = nickName.getText().toString();
        String emailStr = mailTxt.getText().toString();
        String sex = strSex;
        String age = ageTxt.getText().toString().replace(" ", "");
        SPUser user = mUser;
        user.setNickName(nickNameStr);
        user.setEmail(emailStr);
        user.setSex(sex);
        user.setBirthday(age);
        SPUserRequest.updateUserInfo(user, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                showSuccessToast(msg);
                sendBroadcast(new Intent(SPMobileConstants.ACTION_LOGIN_CHNAGE));
                finish();
            }
        }, new SPFailuredListener(SPUserDetailsActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

}
