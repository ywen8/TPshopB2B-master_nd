package com.tpshop.mall.activity.shop;

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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

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
import com.tpshop.mall.http.person.SPUserRequest;
import com.tpshop.mall.model.SPCommentData;
import com.tpshop.mall.widget.NoScrollGridView;
import com.tpshop.mall.widget.SPStarView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.commodity_evaluation)
public class SPCommodityEvaluationActivity extends SPBaseActivity implements View.OnClickListener, TextWatcher, ApplySeriverAdapter.AddPicOnclick {

    @ViewById(R.id.Edit_ll)
    LinearLayout EditLl;

    @ViewById(R.id.product_pic_imgv)
    ImageView productImg;                                 //商品图片

    @ViewById(R.id.product_name_txtv)
    TextView productTxt;                                  //商品名字

    @ViewById(R.id.comment_content_edtv)
    EditText commentEdit;                                 //输入文本

    @ViewById(R.id.limit_txtv)
    TextView commentTxtNum;                               //评价文字个数

    @ViewById(R.id.picGrid)
    NoScrollGridView picGrid;                             //照片

    @ViewById(R.id.addImg)
    ImageView addImg;                                     //添加照片

    @ViewById(R.id.anonymous_evaluation_rb)
    ToggleButton anonymousEvaluationRb;                   //匿名评价

    @ViewById(R.id.comment_descript_starv)
    SPStarView commentDescriptStarv;                      //商品星级

    @ViewById(R.id.comment_deliver_starv)
    SPStarView commentDeliverStarv;                       //物流星级

    @ViewById(R.id.comment_service_starv)
    SPStarView commentServiceStarv;                       //服务星级

    @ViewById(R.id.comment_good_starv)
    SPStarView commentGoodStarv;                          //商品评价

    @ViewById(R.id.comment_ll1)
    LinearLayout comment_ll1;                             //商品评价界面

    @ViewById(R.id.comment_ll2)
    LinearLayout comment_ll2;                             //服务评价界面

    @ViewById(R.id.comment_descript_starv2)
    SPStarView commentDescriptStarv2;                     //服务星级

    @ViewById(R.id.comment_deliver_starv2)
    SPStarView commentDeliverStarv2;                      //物流星级

    @ViewById(R.id.comment_service_starv2)
    SPStarView commentServiceStarv2;                      //商品星级

    int pictureIndex;                                     //图片index
    private Uri imageUri;
    SPCommentData mComment;
    private String orderId;
    private List<Bitmap> photos;
    private ApplySeriverAdapter adapter;
    List<File> images = new ArrayList<>();
    private boolean isServiceComment = false;                         //是否是服务评价
    final static String imageSavePath = "comment.png";
    private static final int REQUEST_CODE_PHOTO = 0x1;
    private static final int REQUEST_CODE_CAMERA = 0x2;
    private String imageDataPath = "/sdcard/headPhoto";               //存放照片路径
    private Uri desUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "commentimg.jpg"));

    @Override
    protected void onCreate(Bundle arg0) {
        super.setCustomerTitle(true, true, getString(R.string.Commodity_evaluation_title));
        super.onCreate(arg0);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        commentEdit.setBackgroundColor(this.getResources().getColor(R.color.botton_nav_bg));
        commentEdit.addTextChangedListener(this);
        EditLl.setBackgroundColor(this.getResources().getColor(R.color.botton_nav_bg));
        FrameLayout titlebarLayout = (FrameLayout) findViewById(R.id.titlebar_normal_layout);
        TextView tvCommit = new TextView(this);
        tvCommit.setText("提交");
        tvCommit.setTextColor(this.getResources().getColor(R.color.light_red));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        tvCommit.setPadding(0, 0, 10, 0);
        params.setMargins(0, 0, 10, 0);
        tvCommit.setGravity(Gravity.CENTER_VERTICAL);
        tvCommit.setTextSize(16);
        photos = new ArrayList<>();
        adapter = new ApplySeriverAdapter(this, photos, this);
        picGrid.setNumColumns(4);
        picGrid.setAdapter(adapter);
        picGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pictureIndex = position;
            }
        });
        titlebarLayout.addView(tvCommit, params);
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitComment();
            }
        });
        addImg.setOnClickListener(this);
        anonymousEvaluationRb.setChecked(true);       //默认匿名
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        if (getIntent() == null) {
            showToast(getString(R.string.data_error));
            return;
        }
        if (getIntent().getSerializableExtra("comment") != null) {      //商品评价
            isServiceComment = false;
            mComment = (SPCommentData) getIntent().getSerializableExtra("comment");
            comment_ll1.setVisibility(View.VISIBLE);
            comment_ll2.setVisibility(View.GONE);
            productTxt.setText(mComment.getGoodsName());
            String imgUrl1 = SPCommonUtils.getThumbnail(SPMobileConstants.FLEXIBLE_THUMBNAIL, mComment.getGoodsId());
            Glide.with(this).load(imgUrl1).asBitmap().fitCenter().placeholder(R.drawable.icon_product_null)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(productImg);
        }
        if (getIntent().getStringExtra("orderId") != null) {      //服务评价
            isServiceComment = true;
            orderId = getIntent().getStringExtra("orderId");
            comment_ll1.setVisibility(View.GONE);
            comment_ll2.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 提交评论
     */
    private void commitComment() {
        if (isServiceComment)
            ServicesComment();
        else
            GoodsComment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addImg:
                selectImage();
                break;
            case R.id.anonymous_evaluation_rb:
                if (anonymousEvaluationRb.isChecked())
                    anonymousEvaluationRb.setBackgroundResource(R.drawable.radio_nocheck);
                else
                    anonymousEvaluationRb.setBackgroundResource(R.drawable.radio_checked);
                break;
        }
    }

    private void selectImage() {
        final String[] items = getResources().getStringArray(R.array.user_head_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传图片");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:      //拍照
                        File outputImage = new File(Environment.getExternalStorageDirectory(), "comment.jpg");
                        try {
                            if (outputImage.exists())
                                outputImage.delete();
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (Build.VERSION.SDK_INT >= 24)
                                imageUri = FileProvider.getUriForFile(SPCommodityEvaluationActivity.this,
                                        "com.tpshop.mall.fileprovider", outputImage);
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

    //商品评价
    private void GoodsComment() {
        String error = "";
        String content = commentEdit.getText().toString();
        if (content.trim().isEmpty()) {
            error = "请输入评价内容";
        } else if (content.length() > 120) {
            error = "评价内容过长";
        } else if (commentDescriptStarv.getRank() + 1 < 1) {
            error = "请对商品等级进行评价";
        } else if (commentDeliverStarv.getRank() + 1 < 1) {
            error = "请对物流等级进行评价";
        } else if (commentServiceStarv.getRank() + 1 < 1) {
            error = "请对服务等级进行评价";
        } else if (commentGoodStarv.getRank() + 1 < 1) {
            error = "请对商品评价进行评价";
        }
        if (!SPStringUtils.isEmpty(error)) {
            showToast(error);
            return;
        }
        SPCommentData commentCondition = new SPCommentData();
        commentCondition.setRecId(mComment.getRecId());
        commentCondition.setOrderId(mComment.getOrderId());
        commentCondition.setGoodsId(mComment.getGoodsId());
        commentCondition.setGoodsRank(commentDescriptStarv.getRank() + 1);
        commentCondition.setDeliverRank(commentDeliverStarv.getRank() + 1);
        commentCondition.setServiceRank(commentServiceStarv.getRank() + 1);
        commentCondition.setGoodsScore(commentGoodStarv.getRank() + 1);
        commentCondition.setContent(content);
        commentCondition.setImages(images);
        if (anonymousEvaluationRb.isChecked())
            commentCondition.setIs_anonymous(1);
        else
            commentCondition.setIs_anonymous(0);
        showLoadingSmallToast();
        SPPersonRequest.commentGoodsWithGoodsID(commentCondition, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                Intent intent = new Intent(SPMobileConstants.ACTION_COMMENT_CHANGE);
                sendBroadcast(intent);
                finish();
            }
        }, new SPFailuredListener(SPCommodityEvaluationActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
    }

    //服务评价
    private void ServicesComment() {
        String error = "";
        if (commentDescriptStarv2.getRank() + 1 < 1) {
            error = "请对服务等级进行评价";
        } else if (commentDeliverStarv2.getRank() + 1 < 1) {
            error = "请对物流等级进行评价";
        } else if (commentServiceStarv2.getRank() + 1 < 1) {
            error = "请对商品等级进行评价";
        }
        if (!SPStringUtils.isEmpty(error)) {
            showToast(error);
            return;
        }
        RequestParams params = new RequestParams();
        params.put("order_id", orderId);
        params.put("service_rank", commentDescriptStarv2.getRank() + 1);
        params.put("deliver_rank", commentDeliverStarv2.getRank() + 1);
        params.put("goods_rank", commentServiceStarv2.getRank() + 1);
        showLoadingSmallToast();
        SPUserRequest.addserviceComment(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showSuccessToast(msg);
                Intent intent = new Intent(SPMobileConstants.ACTION_COMMENT_CHANGE);
                sendBroadcast(intent);
                finish();
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showFailedToast(msg);
            }
        });
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
                    File fileTemp = new File(Environment.getExternalStorageDirectory(), "comment.jpg");
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

    private String getSavePathName() {
        return getSavePathName(System.currentTimeMillis());
    }

    private String getSavePathName(Long index) {
        return imageDataPath + "/" + index + "_" + imageSavePath;      //图片名字
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
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, SPMobileConstants.Result_Code_GetPicture);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        int editStart = commentEdit.getSelectionStart();
        int editEnd = commentEdit.getSelectionEnd();
        int templength = commentEdit.length();
        commentTxtNum.setText("120/" + templength);
        if (templength > 120) {
            showToast("你输入的字数已经超过了限制！");
            s.delete(editStart - 1, editEnd);
            commentEdit.setText(s);
            commentEdit.setSelection(editStart);
        }
    }

    @Override
    public void addPic(int position) {
        addImg.setVisibility(View.VISIBLE);
        picGrid.setNumColumns(4);
        images.remove(position);
    }

}
