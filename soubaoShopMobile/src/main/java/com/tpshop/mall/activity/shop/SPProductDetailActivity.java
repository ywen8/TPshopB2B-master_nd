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
 * Date: @date 2015年11月12日 下午8:08:13
 * Description:商品详情
 *
 * @version V1.0
 */
package com.tpshop.mall.activity.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPBaseActivity;
import com.tpshop.mall.adapter.ItemTitlePagerAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.fragment.SPProductCommentListFragment;
import com.tpshop.mall.fragment.SPProductDetailWebFragment;
import com.tpshop.mall.fragment.SPProductInfoFragment;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.global.SPSaveData;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.condition.SPProductCondition;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.SPSpecPriceModel;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.model.shop.SPGoodsComment;
import com.tpshop.mall.model.shop.SPProductSpec;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPShopUtils;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.NoScrollViewPager;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 飞龙
 */
@EActivity(R.layout.product_details_sec)
public class SPProductDetailActivity extends SPBaseActivity implements View.OnClickListener {

    int mCommentCount;
    private SPStore mStore;
    private String mItemID;                                               //选择规格的itemID
    private String mGoodsID;
    public TextView tv_title;
    private ImageView shareImg;
    private SPProduct mProduct;
    public LinearLayout tv_back;
    public JSONObject mDataJson;
    private UMShareAPI shareAPI;
    private List<String> imgUrls;
    private JSONArray mGalleryArray;
    private RecommendReceiver mReceiver;
    public NoScrollViewPager vp_content;
    public PagerSlidingTabStrip psts_tabs;
    private boolean showOrderItem = false;
    private List<SPGoodsComment> mComments;
    private Map<String, String> selectSpecMap;                            //保存选择的规格ID
    private List<SPSpecPriceModel> mSpecPrices;                           //规格属性对应的价格
    private SPConsigneeAddress consigneeAddress;
    Map<String, String> keyItemMap = new HashMap<>();                     //存储规格id和规格名称
    public Map<String, SPSpecPriceModel> specPriceMap;                    //存储规格对应的价格
    private Map<String, List<SPProductSpec>> specGroupMap;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        shareAPI = UMShareAPI.get(this);
        PlatformConfig.setQQZone(SPMobileConstants.pluginQQAppid, SPMobileConstants.pluginQQSecret);
        PlatformConfig.setWeixin(SPMobileConstants.pluginWeixinAppid, SPMobileConstants.pluginWeixinSecret);
        Intent intent = getIntent();
        if (intent.getStringExtra("goodsID") != null)
            mGoodsID = intent.getStringExtra("goodsID");
        if (intent.getStringExtra("itemID") != null)
            mItemID = intent.getStringExtra("itemID");
        showOrderItem = intent.getBooleanExtra("ShowOrderItem", false);
        if (SPStringUtils.isEmpty(mGoodsID))
            mGoodsID = SPMobileApplication.getInstance().data;
        selectSpecMap = new HashMap<>();
        mReceiver = new RecommendReceiver();
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {
        psts_tabs = (PagerSlidingTabStrip) findViewById(R.id.psts_tabs);
        vp_content = (NoScrollViewPager) findViewById(R.id.vp_content);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back = (LinearLayout) findViewById(R.id.ll_back);
        shareImg = (ImageView) findViewById(R.id.share_img);
    }

    @Override
    public void initEvent() {
        tv_back.setOnClickListener(this);
        shareImg.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(SPMobileConstants.ACTION_GOODS_RECOMMEND);
        registerReceiver(mReceiver, filter);
    }

    public void initData() {
        getProductDetails();
        SPSaveData.putValue(this, SPMobileConstants.KEY_CART_COUNT, 1);
    }

    /**
     * 查询商品信息
     */
    private void getProductDetails() {
        SPProductCondition condition = new SPProductCondition();
        if (mGoodsID == null)
            condition.goodsID = -1;
        else
            condition.goodsID = Integer.valueOf(mGoodsID);
        showLoadingSmallToast();
        SPShopRequest.getProductByID(condition, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                try {
                    mDataJson = (JSONObject) response;
                    if (mDataJson != null && mDataJson.has("product"))
                        mProduct = (SPProduct) mDataJson.get("product");
                    if (mDataJson != null && mDataJson.has("store"))
                        mStore = (SPStore) mDataJson.get("store");
                    if (mDataJson != null && mDataJson.has("gallery"))
                        mGalleryArray = mDataJson.getJSONArray("gallery");
                    if (mDataJson != null && mDataJson.has("price"))
                        mSpecPrices = (List<SPSpecPriceModel>) mDataJson.get("price");
                    if (mDataJson != null && mDataJson.has("comments")) {
                        mComments = (List<SPGoodsComment>) mDataJson.get("comments");
                        mCommentCount = mComments.size();
                    }
                    if (mDataJson != null && mDataJson.has("address"))
                        consigneeAddress = (SPConsigneeAddress) mDataJson.get("address");
                    if (mProduct.getSpecGroupMap() != null)
                        specGroupMap = mProduct.getSpecGroupMap();
                    dealModel();
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(e.getMessage());
                }
                onDataLoadFinish();
            }
        }, new SPFailuredListener(SPProductDetailActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                showToast(msg);
            }
        });
    }

    public void dealModel() {
        imgUrls = new ArrayList<>();
        if (mGalleryArray != null) {
            try {
                for (int i = 0; i < mGalleryArray.length(); i++) {
                    JSONObject jsonObject = mGalleryArray.getJSONObject(i);
                    String url = jsonObject.getString("image_url");
                    imgUrls.add(url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dealProductSpec();
    }

    public void dealProductSpec() {
        if (mProduct != null) {
            if (mSpecPrices != null && mSpecPrices.size() > 0) {
                specPriceMap = new HashMap<>();
                for (SPSpecPriceModel specPrice : mSpecPrices) {
                    specPrice.setSpecName(keyItemMap.get(specPrice.getKey()));
                    specPriceMap.put(specPrice.getKey(), specPrice);
                }
            }
        }
        selectSpecMap.clear();
        Set<String> specGroupKeys = specGroupMap.keySet();
        Iterator<String> iterator = specGroupKeys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            List<SPProductSpec> specs = specGroupMap.get(key);
            if (specs != null && specs.size() > 0) {
                SPProductSpec productSpec = specs.get(0);      //默认选中每组规格中的第一个规格
                selectSpecMap.put(key, productSpec.getItemID());
            }
        }
        Map<String, String> mItemIdForGroupNameMap = new HashMap<>();
        for (Map.Entry<String, List<SPProductSpec>> entry : specGroupMap.entrySet()) {
            List<SPProductSpec> specList = entry.getValue();
            String key = entry.getKey();
            for (SPProductSpec productSpec : specList)
                mItemIdForGroupNameMap.put(productSpec.getItemID(), key);      //将每一个item_id对应的规格组名称保存起来
        }
        if (SPStringUtils.isEmpty(mItemID)) return;
        Map<String, String> tempSelectSpecMap = SPShopUtils.getSelectSpecMapByItemId(Integer.valueOf(mItemID), specPriceMap,
                mItemIdForGroupNameMap);
        if (tempSelectSpecMap == null) return;
        selectSpecMap = tempSelectSpecMap;             //选中传递过来的itemId对应规格组合
        mItemID = null;                                //使用过后清理itemId
    }

    public void onDataLoadFinish() {
        shareImg.setVisibility(View.VISIBLE);
        SPProductInfoFragment goodsInfoFragment = new SPProductInfoFragment();
        SPProductDetailWebFragment webFragment = new SPProductDetailWebFragment();
        SPProductCommentListFragment goodsCommentFragment = SPProductCommentListFragment.newInstence(1);
        fragmentList.add(goodsInfoFragment);
        fragmentList.add(webFragment);
        fragmentList.add(goodsCommentFragment);
        ItemTitlePagerAdapter mPagerAdapter = new ItemTitlePagerAdapter(getSupportFragmentManager(),
                fragmentList, new String[]{"商品", "详情", "评价"});
        vp_content.setAdapter(mPagerAdapter);
        vp_content.setOffscreenPageLimit(3);
        psts_tabs.setViewPager(vp_content);
        if (showOrderItem)
            vp_content.setCurrentItem(2);
    }

    /**
     * 滑动到某一页
     */
    public void scrollPosition(int position) {
        vp_content.setCurrentItem(position);
    }

    public SPProduct getProduct() {
        return mProduct;
    }

    public SPConsigneeAddress getConsigneeAddress() {
        if (consigneeAddress == null) {
            consigneeAddress = new SPConsigneeAddress();
            consigneeAddress.setAddress("请选择地址");
            consigneeAddress.setAddressID("");
            consigneeAddress.setDistrict("");
        }
        return consigneeAddress;
    }

    /**
     * 获取商品ID
     */
    public String getProductId() {
        if (SPStringUtils.isEmpty(this.mGoodsID)) return "0";
        return this.mGoodsID;
    }

    /**
     * 获取推荐商品
     */
    public List<SPProduct> getRecommenProducts() {
        return mProduct.getRecommends();
    }

    public Map<String, SPSpecPriceModel> getSpecPriceMap() {
        return this.specPriceMap;
    }

    /**
     * 获取选中的商品规格
     */
    public Map<String, String> getSelectSpecMap() {
        return selectSpecMap;
    }

    /**
     * 获产品取规格组
     */
    public Map<String, List<SPProductSpec>> getSepcGroupMap() {
        return specGroupMap;
    }

    public List<String> getImgUrls() {
        return this.imgUrls;
    }

    public SPStore getStore() {
        return mStore;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                this.finish();
                break;
            case R.id.share_img:      //分享
                share();
                break;
        }
    }

    private void share() {
        ShareAction shareAction = new ShareAction(this);
        String url = SPMobileConstants.BASE_URL_PREFIX + "Mobile&c=Goods&a=goodsInfo" + "&id=" + mGoodsID + "&first_leader="
                + SPMobileApplication.getInstance().getLoginUser().getUserID();
        UMWeb web = new UMWeb(url);
        web.setTitle(getString(R.string.app_name));
        if (imgUrls.size() < 1)
            web.setThumb(new UMImage(this, R.drawable.icon_product_null));
        else
            web.setThumb(new UMImage(this, SPUtils.getImageUrl(imgUrls.get(0))));
        web.setDescription(mProduct.getGoodsName());
        shareAction.withMedia(web);
        shareAction.setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
        shareAction.setCallback(umShareListener);
        shareAction.open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            showToast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            showToast(throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            showToast("分享取消");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareAPI.release();
        unregisterReceiver(mReceiver);
    }

    class RecommendReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String goodsId = SPMobileApplication.getInstance().data;
            if (intent.getAction().equals(SPMobileConstants.ACTION_GOODS_RECOMMEND)) {
                Intent detailIntent = new Intent(SPProductDetailActivity.this, SPProductDetailActivity_.class);
                detailIntent.putExtra("goodsId", goodsId);
                SPProductDetailActivity.this.startActivity(detailIntent);
            }
        }
    }

}
