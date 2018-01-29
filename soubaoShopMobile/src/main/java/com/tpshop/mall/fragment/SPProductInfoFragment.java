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
 * Date: @date 2015年10月20日 下午7:19:26
 * Description:MineFragment
 *
 * @version V1.0
 */
package com.tpshop.mall.fragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.common.BadgeView;
import com.tpshop.mall.activity.common.SPImagePreviewActivity_;
import com.tpshop.mall.activity.person.address.SPConsigneeAddressListActivity_;
import com.tpshop.mall.activity.shop.SPConfirmOrderActivity_;
import com.tpshop.mall.activity.shop.SPProductDetailActivity;
import com.tpshop.mall.activity.shop.SPStoreHomeActivity_;
import com.tpshop.mall.adapter.ItemRecommendAdapter;
import com.tpshop.mall.adapter.NetworkImageHolderView;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.entity.SPActivityItem;
import com.tpshop.mall.entity.SerializableMap;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.global.SPSaveData;
import com.tpshop.mall.global.SPShopCartManager;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.person.SPPersonRequest;
import com.tpshop.mall.http.shop.SPShopRequest;
import com.tpshop.mall.http.shop.SPStoreRequest;
import com.tpshop.mall.model.SPProduct;
import com.tpshop.mall.model.SPSpecPriceModel;
import com.tpshop.mall.model.person.SPConsigneeAddress;
import com.tpshop.mall.model.shop.SPActivity;
import com.tpshop.mall.model.shop.SPStore;
import com.tpshop.mall.utils.SPShopUtils;
import com.tpshop.mall.utils.SPUtils;
import com.tpshop.mall.widget.BottomPopUpDialog;
import com.tpshop.mall.widget.CountdownView;
import com.tpshop.mall.widget.SPDrawableHorizontalButton;
import com.tpshop.mall.widget.SlideDetailsLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品信息
 */
public class SPProductInfoFragment extends SPBaseFragment implements View.OnClickListener, SlideDetailsLayout.OnSlideDetailsListener,
        OnItemClickListener {

    SPStore mStore;
    WebView mWebView;
    SPProduct mProduct;
    private String shopPrice;
    CountdownView group_coutdownv;
    public FrameLayout fl_content;
    private LayoutInflater inflater;
    private BadgeView badge, badge2;
    private ScrollView sv_goods_info;
    private BottomPopUpDialog dialog;
    private boolean isDispatch = true;
    private LinearLayout rl_group_info;
    private SlideDetailsLayout sv_switch;
    public SPProductDetailActivity activity;
    SPConsigneeAddress selectRegionConsignee;                //选中区域
    private Map<String, String> selectSpecMap;               //保存选择的规格ID
    private FloatingActionButton fab_up_slide;
    private LinearLayout ll_saller, ll_saller_info;
    private SpecChangeReceiver mSpecChangeReceiver;
    private Map<String, SPSpecPriceModel> specPriceMap;
    private SPDrawableHorizontalButton service_date_btn;
    private ShopCartChangeReceiver mShopCartChangeReceiver;
    public ConvenientBanner vp_item_goods_img, vp_recommend;
    private View nomal_product_layout, virtual_product_layout;
    private ImageView iv_collect, iv_brand, iv_attention, iv_attention2, iv_pull_up, promote;
    private Button btn_enter_store, btn_customer, btn_promptly_buy, btn_buy_cart, btn_add_cart;
    public RelativeLayout rl_bottom_cart, rl_bottom_cart2, rl_goods_price, rl_cart_count, rl_cart_count2;
    private TextView tv_goods_detail, tv_goods_config, tv_sale_count, tv_store_count, tv_group_old_price,
            tv_group_num, tv_group_price, tv_send_addr, dispatch_fee_tv;
    public LinearLayout ll_current_spec, ll_comment, ll_recommend, ll_pull_up, ll_collect, ll_attention,
            ll_attention2, ll_send_addr, ll_enter_store, ll_enter_store2;
    public TextView tv_goods_title, tv_goods_price, tv_goods_desc, tv_market_price, tv_integral_price, tv_current_spec,
            tv_comment_count, tv_comment_percent, tv_pull_up, tv_service_store, tv_store_name, tv_seller_name, tv_own_shop,
            tv_desc_score, tv_service_score, tv_expres_score, tv_send_integral;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SPProductDetailActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter specChagnefilter = new IntentFilter(SPMobileConstants.ACTION_SPEC_CHNAGE);          //监听规格变化
        activity.registerReceiver(mSpecChangeReceiver = new SpecChangeReceiver(), specChagnefilter);
        IntentFilter filter = new IntentFilter(SPMobileConstants.ACTION_SHOPCART_CHNAGE);                //监听购物车数据变化广播
        activity.registerReceiver(mShopCartChangeReceiver = new ShopCartChangeReceiver(), filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        vp_item_goods_img.startTurning(4000);        //开始自动翻页
    }

    @Override
    public void onPause() {
        super.onPause();
        vp_item_goods_img.stopTurning();        //停止自动翻页
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_goods_info, null);
        initView(rootView);
        initListener();
        initDatas();
        loadCartCount();
        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView(View rootView) {
        mWebView = (WebView) rootView.findViewById(R.id.common_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setDisplayZoomControls(false);                                       //隐藏webview缩放按钮
        mWebView.getSettings().setSupportZoom(false);                                               //设定不支持缩放
        mWebView.getSettings().setUseWideViewPort(true);                                            //适应屏幕大小
        mWebView.getSettings().setLoadWithOverviewMode(true);
        FrameLayout banner_lyaout = (FrameLayout) rootView.findViewById(R.id.banner_lyaout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SPUtils.getWindowWidth(activity), SPUtils.getWindowWidth(activity));
        banner_lyaout.setLayoutParams(params);
        vp_item_goods_img = (ConvenientBanner) rootView.findViewById(R.id.vp_item_goods_img);       //商品轮播图
        group_coutdownv = (CountdownView) rootView.findViewById(R.id.group_coutdownv);              //活动倒计时
        tv_group_old_price = (TextView) rootView.findViewById(R.id.tv_group_old_price);             //市场原价
        promote = (ImageView) rootView.findViewById(R.id.promote);                                  //促销标识
        tv_group_num = (TextView) rootView.findViewById(R.id.tv_group_num);                         //参团人数
        tv_group_price = (TextView) rootView.findViewById(R.id.tv_group_price);                     //活动价
        rl_group_info = (LinearLayout) rootView.findViewById(R.id.rl_group_info);                   //活动面板
        rl_goods_price = (RelativeLayout) rootView.findViewById(R.id.rl_goods_price);               //价格信息
        tv_goods_title = (TextView) rootView.findViewById(R.id.tv_goods_title);                     //商品名称
        tv_goods_price = (TextView) rootView.findViewById(R.id.tv_goods_price);                     //店铺价格
        tv_goods_desc = (TextView) rootView.findViewById(R.id.tv_goods_desc);                       //商品描述
        tv_market_price = (TextView) rootView.findViewById(R.id.tv_market_price);                   //市场价
        tv_integral_price = (TextView) rootView.findViewById(R.id.tv_integral_price);               //积分价格
        tv_send_integral = (TextView) rootView.findViewById(R.id.tv_send_integral);                 //赠送积分
        tv_sale_count = (TextView) rootView.findViewById(R.id.tv_sale_count);                       //销量
        tv_store_count = (TextView) rootView.findViewById(R.id.tv_store_count);                     //库存
        ll_current_spec = (LinearLayout) rootView.findViewById(R.id.ll_current_spec);               //选中规格
        tv_current_spec = (TextView) rootView.findViewById(R.id.tv_current_spec);                   //选中规格
        tv_service_store = (TextView) rootView.findViewById(R.id.tv_service_store);                 //服务店铺
        ll_send_addr = (LinearLayout) rootView.findViewById(R.id.ll_send_addr);                     //配送至
        tv_send_addr = (TextView) rootView.findViewById(R.id.tv_send_addr);                         //配送至
        dispatch_fee_tv = (TextView) rootView.findViewById(R.id.dispatch_fee_tv);                   //运费信息
        ll_comment = (LinearLayout) rootView.findViewById(R.id.ll_comment);                         //评论
        tv_comment_count = (TextView) rootView.findViewById(R.id.tv_comment_count);                 //评论数量
        tv_comment_percent = (TextView) rootView.findViewById(R.id.tv_comment_percent);             //好评率
        vp_recommend = (ConvenientBanner) rootView.findViewById(R.id.vp_recommend);                 //推荐商品
        iv_brand = (ImageView) rootView.findViewById(R.id.iv_brand);                                //店铺图标
        btn_customer = (Button) rootView.findViewById(R.id.btn_contact_customer);                   //联系客服
        btn_enter_store = (Button) rootView.findViewById(R.id.btn_enter_store);                     //进入店铺
        tv_store_name = (TextView) rootView.findViewById(R.id.tv_store_name);                       //店铺名称
        tv_seller_name = (TextView) rootView.findViewById(R.id.tv_seller_name);                     //卖家名称
        tv_own_shop = (TextView) rootView.findViewById(R.id.tv_own_shop);                           //是否自营店
        tv_desc_score = (TextView) rootView.findViewById(R.id.tv_desc_score);                       //商品描述
        tv_service_score = (TextView) rootView.findViewById(R.id.tv_service_score);                 //商家服务
        tv_expres_score = (TextView) rootView.findViewById(R.id.tv_express_score);                  //物流服务
        ll_saller = (LinearLayout) rootView.findViewById(R.id.ll_saller);                           //促销
        ll_saller_info = (LinearLayout) rootView.findViewById(R.id.ll_saller_info);                 //促销信息
        ll_collect = (LinearLayout) rootView.findViewById(R.id.ll_collect);                         //选中收藏
        nomal_product_layout = rootView.findViewById(R.id.nomal_product_layout);                    //普通商品底部操作布局
        virtual_product_layout = rootView.findViewById(R.id.virtual_product_layout);                //虚拟商品底部操作布局
        ll_attention = (LinearLayout) rootView.findViewById(R.id.ll_attention);                     //普通商品选中关注
        ll_attention2 = (LinearLayout) rootView.findViewById(R.id.ll_attention2);                   //虚拟商品选中关注
        iv_attention = (ImageView) rootView.findViewById(R.id.iv_attention);                        //普通商品关注图
        iv_attention2 = (ImageView) rootView.findViewById(R.id.iv_attention2);                      //虚拟商品关注图
        ll_enter_store = (LinearLayout) rootView.findViewById(R.id.ll_enter_store);                 //普通商品进入店铺
        ll_enter_store2 = (LinearLayout) rootView.findViewById(R.id.ll_enter_store2);               //虚拟商品进入店铺
        rl_bottom_cart = (RelativeLayout) rootView.findViewById(R.id.rl_bottom_cart);               //普通商品进入购物车
        rl_bottom_cart2 = (RelativeLayout) rootView.findViewById(R.id.rl_bottom_cart2);             //虚拟商品进入购物车
        btn_buy_cart = (Button) rootView.findViewById(R.id.btn_buy_cart);                           //虚拟商品立即购买
        iv_collect = (ImageView) rootView.findViewById(R.id.iv_collect);                            //收藏
        rl_cart_count = (RelativeLayout) rootView.findViewById(R.id.rl_cart_count);                 //购物车数量
        rl_cart_count2 = (RelativeLayout) rootView.findViewById(R.id.rl_cart_count2);               //购物车数量
        btn_promptly_buy = (Button) rootView.findViewById(R.id.btn_promptly_buy);                   //普通商品立即购买
        btn_add_cart = (Button) rootView.findViewById(R.id.btn_add_cart);                           //加入到购物车
        fab_up_slide = (FloatingActionButton) rootView.findViewById(R.id.fab_up_slide);
        service_date_btn = (SPDrawableHorizontalButton) rootView.findViewById(R.id.service_date_btn);
        sv_switch = (SlideDetailsLayout) rootView.findViewById(R.id.sv_switch);
        sv_goods_info = (ScrollView) rootView.findViewById(R.id.sv_goods_info);
        fl_content = (FrameLayout) rootView.findViewById(R.id.fl_content);
        ll_comment = (LinearLayout) rootView.findViewById(R.id.ll_comment);
        ll_recommend = (LinearLayout) rootView.findViewById(R.id.ll_recommend);
        ll_pull_up = (LinearLayout) rootView.findViewById(R.id.ll_pull_up);
        tv_goods_detail = (TextView) rootView.findViewById(R.id.tv_goods_detail);
        tv_goods_config = (TextView) rootView.findViewById(R.id.tv_goods_config);
        tv_pull_up = (TextView) rootView.findViewById(R.id.tv_pull_up);
        iv_pull_up = (ImageView) rootView.findViewById(R.id.iv_pull_up);
        badge = new BadgeView(activity, rl_cart_count);
        badge2 = new BadgeView(activity, rl_cart_count2);
        setLoopView();
        setRecommendGoods();
        tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);                           //设置文字中间一条横线
        tv_group_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        fab_up_slide.hide();
        vp_item_goods_img.setPageIndicator(new int[]{R.mipmap.index_white, R.mipmap.index_red});      //设置两个点图片作为翻页指示器,不设置则没有指示器
        vp_item_goods_img.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        vp_recommend.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
        vp_recommend.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        dialog = new BottomPopUpDialog.Builder().setDialogData(activity, activity.getProduct()).create();
    }

    private void initListener() {
        fab_up_slide.setOnClickListener(this);
        ll_current_spec.setOnClickListener(this);
        ll_pull_up.setOnClickListener(this);
        sv_switch.setOnSlideDetailsListener(this);
        ll_collect.setOnClickListener(this);
        ll_attention.setOnClickListener(this);
        ll_attention2.setOnClickListener(this);
        ll_send_addr.setOnClickListener(this);
        ll_current_spec.setOnClickListener(this);
        ll_comment.setOnClickListener(this);
        ll_enter_store2.setOnClickListener(this);
        ll_enter_store.setOnClickListener(this);
        btn_enter_store.setOnClickListener(this);
        btn_customer.setOnClickListener(this);
        rl_bottom_cart.setOnClickListener(this);
        rl_bottom_cart2.setOnClickListener(this);
        btn_promptly_buy.setOnClickListener(this);
        btn_buy_cart.setOnClickListener(this);
        btn_add_cart.setOnClickListener(this);
        vp_item_goods_img.setOnItemClickListener(this);
        vp_recommend.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent detailIntent = new Intent(getActivity(), SPProductDetailActivity.class);
                detailIntent.putExtra("goodsID", mProduct.getRecommends().get(position).getGoodsID());
                getActivity().startActivity(detailIntent);
            }
        });
    }

    @Override
    public void initSubView(View view) {
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
    }

    @Override
    public void initData() {
    }

    private void initDatas() {
        List<TextView> tabTextList = new ArrayList<>();
        tabTextList.add(tv_goods_detail);
        tabTextList.add(tv_goods_config);
        this.selectSpecMap = activity.getSelectSpecMap();
        this.specPriceMap = activity.getSpecPriceMap();
        String title = activity.getProduct().getGoodsName();
        String remark = activity.getProduct().getGoodsRemark();
        if (!SPStringUtils.isEmpty(title)) tv_goods_title.setText(title);
        tv_market_price.setText("¥" + activity.getProduct().getMarketPrice());
        mStore = activity.getStore();
        mProduct = activity.getProduct();
        if (mProduct.getIsVirtual() == 1 || mProduct.getExchangeIntegral() > 0) {      //虚拟商品(积分商品)
            if (mProduct.getIsVirtual() != 1)
                btn_buy_cart.setText("立即兑换");
            virtual_product_layout.setVisibility(View.VISIBLE);
            nomal_product_layout.setVisibility(View.GONE);
        } else {       //普通商品
            virtual_product_layout.setVisibility(View.GONE);
            nomal_product_layout.setVisibility(View.VISIBLE);
        }
        shopPrice = mProduct.getShopPrice();
        loadWeb();
        if (mProduct.getGiveIntegral() > 0) {
            tv_send_integral.setVisibility(View.VISIBLE);
            tv_send_integral.setText("赠送" + mProduct.getGiveIntegral() + "积分");
        } else {
            tv_send_integral.setVisibility(View.GONE);
        }
        if (!SPStringUtils.isEmpty(remark)) {
            tv_goods_desc.setText(remark);
            tv_goods_desc.setVisibility(View.VISIBLE);
        } else {
            tv_goods_desc.setVisibility(View.GONE);
        }
        if (mStore == null) return;
        String logoUrl = SPMobileConstants.BASE_HOST + mStore.getStoreLogo();
        Glide.with(this).load(logoUrl).asBitmap().fitCenter().placeholder(R.drawable.icon_brand_null)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_brand);
        tv_store_name.setText(mStore.getStoreName());
        tv_seller_name.setText(mStore.getSellerName());
        if (mStore.getStoreId() == 1 || mStore.getIsOwnShop() == 1) {      //平台自营
            Drawable drawable = this.getResources().getDrawable(R.drawable.icon_own_shop);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_store_name.setCompoundDrawables(drawable, null, null, null);
            if (mStore.getStoreId() == 1) tv_store_name.setText("");
        } else {
            tv_store_name.setCompoundDrawables(null, null, null, null);
        }
        service_date_btn.setText(mStore.getServiceDate() + "天保修");
        tv_desc_score.setText("宝贝描述" + mStore.getDesccredit());
        tv_service_score.setText("卖家服务:" + mStore.getServicecredit());
        tv_expres_score.setText("物流速度:" + mStore.getDeliverycredit());
        tv_comment_count.setText(mProduct.getCommentTitleModel().getAll() + "条评价");
        tv_sale_count.setText("销量：" + mProduct.getSalesSum());
        String serviceStr = "本商品由" + mStore.getStoreName() + "为您提供售后服务";
        int startIndex = 4;
        int endIndex = serviceStr.length() - 8;
        SpannableString serviceSpanStr = new SpannableString(serviceStr);
        serviceSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), startIndex, endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);         //设置区域内文字颜色为黑色
        tv_service_store.setText(serviceSpanStr);
        tv_comment_percent.setText("好评率" + mProduct.getCommentTitleModel().getGoodRate() + "%");
        selectRegionConsignee = SPSaveData.getAddress(activity);
        if (SPStringUtils.isEmpty(selectRegionConsignee.getAddressID()))
            selectRegionConsignee = activity.getConsigneeAddress();
        tv_send_addr.setText(selectRegionConsignee.getAddress());
        refreshPriceView();
        refreshCollectButton();
        refreshAttentionButton();
    }

    public void refreshPriceView() {
        if (selectSpecMap != null && selectSpecMap.size() > 0) {      //有规格的商品
            String priceModelKey = SPShopUtils.getPriceModelkey(selectSpecMap.values());
            int itemId = SPShopUtils.getItemId(priceModelKey, specPriceMap);
            if (itemId != 0) {      //规格存在,获取该规格的活动
                refreshActivity(itemId);
                String keyFmat = SPShopUtils.getGoodsSpec(priceModelKey, specPriceMap);      //key存储的规格名称
                int storeCount = SPShopUtils.getShopStoreCount(priceModelKey, specPriceMap);
                if (storeCount <= 0) {
                    btn_promptly_buy.setEnabled(false);
                    btn_promptly_buy.setBackgroundColor(getResources().getColor(R.color.button_bg_gray));
                } else {
                    btn_promptly_buy.setEnabled(true);
                    btn_promptly_buy.setBackgroundResource(R.drawable.button_yellow_selector);
                }
                tv_current_spec.setText(keyFmat);      //选中的规格
            } else {      //该种规格不存在
                String priceFmt = "¥" + shopPrice;
                SpannableString spanStr = SPUtils.getFirstCharScale(activity, priceFmt);
                tv_goods_price.setText(spanStr);
                mProduct.setShopPrice(shopPrice);
                dialog.setPrice(priceFmt);
                tv_integral_price.setVisibility(View.GONE);
                btn_promptly_buy.setEnabled(false);
                btn_promptly_buy.setBackgroundColor(getResources().getColor(R.color.button_bg_gray));
                ll_saller.setVisibility(View.GONE);
                tv_current_spec.setText("无该种规格");
                tv_store_count.setText("库存：0");
                dialog.setCount(0);
                if (!SPStringUtils.isEmpty(selectRegionConsignee.getDistrict())) {
                    String redionId = selectRegionConsignee.getDistrict();
                    refreshDispatch(redionId);
                }
            }
        } else {      //无规格的商品
            if (mProduct.getActivity().getPromType() == 1 || mProduct.getActivity().getPromType() == 2
                    || mProduct.getActivity().getPromType() == 3) {      //有活动
                rl_group_info.setVisibility(View.VISIBLE);                            //活动信息显示
                rl_goods_price.setVisibility(View.GONE);                              //普通商品信息隐藏
                long endTime = mProduct.getActivity().getEndTime() * 1000;            //结束时间
                long curTime = mProduct.getActivity().getCurTime() * 1000;            //当前时间
                long arriveTime = endTime - curTime;
                group_coutdownv.start(arriveTime);                                    //活动倒计时
                tv_group_price.setText("¥" + mProduct.getActivity().getPromPrice());
                tv_group_old_price.setText("原价" + mProduct.getMarketPrice());
                if (mProduct.getActivity().getPromType() == 2) {      //是团购
                    tv_group_num.setText("已有" + mProduct.getActivity().getVirtualNum() + "人参团");
                    tv_group_num.setVisibility(View.VISIBLE);
                    tv_sale_count.setVisibility(View.GONE);
                } else {      //不是团购
                    tv_group_num.setVisibility(View.GONE);
                    tv_sale_count.setVisibility(View.VISIBLE);
                }
                if (mProduct.getActivity().getPromType() == 3)    //是商品促销
                    promote.setVisibility(View.VISIBLE);
                else
                    promote.setVisibility(View.GONE);             //不是商品促销
            } else {      //不是活动
                rl_group_info.setVisibility(View.GONE);           //活动信息隐藏
                rl_goods_price.setVisibility(View.VISIBLE);       //普通商品信息显示
                tv_sale_count.setVisibility(View.VISIBLE);
                String priceFmt = "¥" + shopPrice;
                SpannableString spanStr = SPUtils.getFirstCharScale(activity, priceFmt);
                tv_goods_price.setText(spanStr);
                if (mProduct.getExchangeIntegral() > 0) {
                    String exchangeIntegral = String.valueOf(mProduct.getExchangeIntegral());
                    BigDecimal b1 = new BigDecimal(shopPrice);
                    BigDecimal b2 = new BigDecimal(exchangeIntegral);
                    BigDecimal b3 = new BigDecimal(mProduct.getPointRate());
                    String integralPrice;
                    if ((b1.subtract(b2.divide(b3))).doubleValue() <= 0)
                        integralPrice = "可用: " + (int) Math.ceil((b1.multiply(b3)).doubleValue()) + "积分";
                    else
                        integralPrice = "可用: ¥" + b1.subtract(b2.divide(b3)) + "+" + exchangeIntegral + "积分";
                    tv_integral_price.setText(integralPrice);
                    tv_integral_price.setVisibility(View.VISIBLE);
                    dialog.setPrice(integralPrice.replace("可用: ", ""));
                } else {
                    tv_integral_price.setVisibility(View.GONE);
                }
            }
            if (mProduct.getIsVirtual() == 1) {      //虚拟商品
                ll_saller.setVisibility(View.GONE);
            } else {      //普通商品
                List<SPActivityItem> items = mProduct.getActivity().getDatas();
                if (items != null && items.size() > 0) {
                    ll_saller.setVisibility(View.VISIBLE);
                    ll_saller_info.removeAllViews();
                    for (SPActivityItem spActivityItem : items) {
                        View view = inflater.inflate(R.layout.saller_info_item, null);
                        TextView tv_saller_type = (TextView) view.findViewById(R.id.tv_saller_type);        //促销类型
                        TextView tv_saller_info = (TextView) view.findViewById(R.id.tv_saller_info);        //促销详情
                        tv_saller_type.setText(spActivityItem.getTitle());
                        tv_saller_info.setText(spActivityItem.getContent());
                        ll_saller_info.addView(view);
                    }
                } else {
                    ll_saller.setVisibility(View.GONE);
                }
            }
            int storeCount;
            if (mProduct.getActivity().getPromType() == 1 || mProduct.getActivity().getPromType() == 2)
                storeCount = mProduct.getActivity().getPromStoreCount();
            else
                storeCount = mProduct.getStoreCount();
            tv_store_count.setText("库存：" + storeCount);
            dialog.setCount(storeCount);
            if (storeCount <= 0) {
                btn_promptly_buy.setEnabled(false);
                btn_promptly_buy.setBackgroundColor(getResources().getColor(R.color.button_bg_gray));
            } else {
                btn_promptly_buy.setEnabled(true);
                btn_promptly_buy.setBackgroundResource(R.drawable.button_yellow_selector);
            }
            tv_current_spec.setText("1件");
            if (!SPStringUtils.isEmpty(selectRegionConsignee.getDistrict())) {
                String redionId = selectRegionConsignee.getDistrict();
                refreshDispatch(redionId);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pull_up:          //上拉查看图文详情
                sv_switch.smoothOpen(true);
                tv_pull_up.setText(getString(R.string.pull_up_goodsweb));
                iv_pull_up.setImageResource(R.mipmap.icon_top);
                break;
            case R.id.fab_up_slide:        //点击滑动到顶部
                sv_goods_info.smoothScrollTo(0, 0);
                sv_switch.smoothClose(true);
                tv_pull_up.setText(getString(R.string.pull_down_goodsweb));
                iv_pull_up.setImageResource(R.mipmap.icon_down);
                break;
            case R.id.ll_collect:          //收藏商品
                collectGoods();
                break;
            case R.id.ll_send_addr:        //配送至
                Intent regionIntent = new Intent(activity, SPConsigneeAddressListActivity_.class);
                regionIntent.putExtra("getAddress", "1");
                startActivityForResult(regionIntent, SPMobileConstants.Result_Code_GetValue);
                break;
            case R.id.ll_current_spec:     //已选规格
                showBottom();
                break;
            case R.id.ll_comment:          //好评率
                activity.scrollPosition(2);
                break;
            case R.id.ll_attention:        //关注
            case R.id.ll_attention2:       //关注
                attentionStore();
                break;
            case R.id.ll_enter_store:      //进入店铺
            case R.id.ll_enter_store2:
            case R.id.btn_enter_store:
                if (mStore.getStoreId() == 1) {
                    showToast(getString(R.string.tpshop_own_shop));
                    return;
                }
                Intent storeIntent = new Intent(getActivity(), SPStoreHomeActivity_.class);
                storeIntent.putExtra("storeId", activity.getStore().getStoreId());
                startActivity(storeIntent);
                break;
            case R.id.rl_bottom_cart:      //进入购物车
            case R.id.rl_bottom_cart2:
                gotoShopcart();
                break;
            case R.id.btn_add_cart:        //加入购物车
                showBottom();
                break;
            case R.id.btn_promptly_buy:    //立即购买
                buyNowClick();
                break;
            case R.id.btn_buy_cart:        //虚拟商品立即购买(积分商城商品兑换)
                showBottom();
                break;
            case R.id.btn_contact_customer:
                connectCustomer(activity.getStore().getStoreQQ());
                break;
        }
    }

    public void showBottom() {
        dialog.show(getChildFragmentManager(), "tag");
    }

    /**
     * 给商品轮播图设置图片路径
     */
    public void setLoopView() {
        List<String> imgUrls = activity.getImgUrls();
        vp_item_goods_img.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new NetworkImageHolderView();
            }
        }, imgUrls);
    }

    /**
     * 设置推荐商品
     */
    public void setRecommendGoods() {
        List<SPProduct> data = this.activity.getRecommenProducts();
        List<List<SPProduct>> handledData = SPShopUtils.handleRecommendGoods(data);
        vp_recommend.setManualPageable(handledData.size() != 1);      //设置如果只有一组数据时不能滑动
        vp_recommend.setCanLoop(handledData.size() != 1);
        vp_recommend.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new ItemRecommendAdapter();
            }
        }, handledData);
    }

    @Override
    public void onStatucChanged(SlideDetailsLayout.Status status) {
        if (status == SlideDetailsLayout.Status.OPEN) {      //当前为图文详情页
            fab_up_slide.show();
            activity.vp_content.setNoScroll(true);
            activity.tv_title.setVisibility(View.VISIBLE);
            activity.psts_tabs.setVisibility(View.GONE);
        } else {      //当前为商品详情页
            fab_up_slide.hide();
            activity.vp_content.setNoScroll(false);
            activity.tv_title.setVisibility(View.GONE);
            activity.psts_tabs.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 刷新收藏按钮
     */
    public void refreshCollectButton() {
        if (activity.getProduct().getIsCollect() == 1)      //收藏
            iv_collect.setImageResource(R.drawable.icon_collect_checked);
        else      //未收藏
            iv_collect.setImageResource(R.drawable.icon_collect_normal);
    }

    public void refreshAttentionButton() {
        if (activity.getStore().getIsCollect() == 1) {      //关注
            if (activity.getProduct().getIsVirtual() == 1 || mProduct.getExchangeIntegral() > 0)
                iv_attention2.setImageResource(R.drawable.icon_attention_checked);
            else
                iv_attention.setImageResource(R.drawable.icon_attention_checked);
        } else {      //未关注
            if (activity.getProduct().getIsVirtual() == 1 || mProduct.getExchangeIntegral() > 0)
                iv_attention2.setImageResource(R.drawable.icon_attention_normal);
            else
                iv_attention.setImageResource(R.drawable.icon_attention_normal);
        }
    }

    /**
     * 进入购物车
     */
    public void gotoShopcart() {
        Intent shopcartIntent = new Intent(activity, SPMainActivity.class);
        shopcartIntent.putExtra(SPMainActivity.SELECT_INDEX, SPMainActivity.INDEX_SHOPCART);
        startActivity(shopcartIntent);
    }

    /**
     * 图片预览
     */
    @Override
    public void onItemClick(int position) {
        SPMobileApplication.getInstance().setImageUrl(activity.getImgUrls());
        Intent previewIntent = new Intent(activity, SPImagePreviewActivity_.class);
        previewIntent.putExtra("index", position);
        startActivity(previewIntent);
    }

    /**
     * 监听规格(价格)改变广播
     */
    class SpecChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SPMobileConstants.ACTION_SPEC_CHNAGE)) {
                SerializableMap serializableMap = (SerializableMap) intent.getSerializableExtra("spec");
                Integer selectCount = intent.getIntExtra("count", 0);
                selectSpecMap = serializableMap.getMap();
                String priceModelKey = SPShopUtils.getPriceModelkey(selectSpecMap.values());
                int itemId = SPShopUtils.getItemId(priceModelKey, specPriceMap);
                if (itemId != 0) {      //该规格存在
                    refreshActivity(itemId);
                    String keyFmat = SPShopUtils.getGoodsSpec(priceModelKey, specPriceMap);
                    tv_current_spec.setText(keyFmat);      //选中的规格
                } else {      //该规格不存在
                    String priceFmt = "¥" + shopPrice;
                    int storeCount = mProduct.getStoreCount();
                    SpannableString spanStr = SPUtils.getFirstCharScale(getActivity(), priceFmt);
                    if (mProduct.getExchangeIntegral() <= 0) {      //不是积分商品
                        tv_goods_price.setText(spanStr);
                        mProduct.setShopPrice(shopPrice);
                        dialog.setPrice(priceFmt);
                        tv_integral_price.setVisibility(View.GONE);
                    }
                    if (selectSpecMap != null && selectSpecMap.size() > 0) {
                        btn_promptly_buy.setEnabled(false);
                        btn_promptly_buy.setBackgroundColor(getResources().getColor(R.color.button_bg_gray));
                    }
                    tv_current_spec.setText(selectCount + "件");
                    tv_store_count.setText("库存：" + storeCount);
                    dialog.setCount(storeCount);
                }
            }
        }
    }

    private void refreshActivity(int itemId) {
        SPShopRequest.getGoodActivity(activity.getProductId(), itemId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                SPActivity spActivity = (SPActivity) response;
                refreshPrice(spActivity);
                if (!SPStringUtils.isEmpty(selectRegionConsignee.getDistrict())) {
                    String redionId = selectRegionConsignee.getDistrict();
                    refreshDispatch(redionId);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    private void refreshPrice(SPActivity spActivity) {
        switch (spActivity.getPromType()) {
            case 0:      //0:默认
                refreshView(null);
                break;
            case 1:      //1:抢购
                refreshView(spActivity);
                break;
            case 2:      //2:团购
                refreshView(spActivity);
                break;
            case 3:      //3:促销
                refreshView(spActivity);
                break;
            case 4:      //4:预售
                refreshView(null);
                break;
            case 6:      //拼团商品
                refreshView(null);
                break;
        }
        if (mProduct.getIsVirtual() == 1) {      //虚拟商品
            ll_saller.setVisibility(View.GONE);
        } else {      //普通商品
            List<SPActivityItem> items = spActivity.getDatas();
            if (items != null && items.size() > 0) {
                ll_saller.setVisibility(View.VISIBLE);
                ll_saller_info.removeAllViews();
                for (SPActivityItem spActivityItem : items) {
                    View view = inflater.inflate(R.layout.saller_info_item, null);
                    TextView tv_saller_type = (TextView) view.findViewById(R.id.tv_saller_type);        //促销类型
                    TextView tv_saller_info = (TextView) view.findViewById(R.id.tv_saller_info);        //促销详情
                    tv_saller_type.setText(spActivityItem.getTitle());
                    tv_saller_info.setText(spActivityItem.getContent());
                    ll_saller_info.addView(view);
                }
            } else {
                ll_saller.setVisibility(View.GONE);
            }
        }
    }

    private void refreshView(SPActivity spActivity) {
        int storeCount;
        String priceModelKey = SPShopUtils.getPriceModelkey(selectSpecMap.values());
        if (spActivity == null) {      //无活动
            rl_group_info.setVisibility(View.GONE);          //活动信息隐藏
            rl_goods_price.setVisibility(View.VISIBLE);      //普通商品信息显示
            String price = SPShopUtils.getShopprice(priceModelKey, specPriceMap);
            if (price.equals("")) price = shopPrice;
            String priceFmt = "¥" + price;
            SpannableString spanStr = SPUtils.getFirstCharScale(getActivity(), priceFmt);
            tv_goods_price.setText(spanStr);
            mProduct.setShopPrice(price);
            if (mProduct.getExchangeIntegral() > 0) {
                String exchangeIntegral = String.valueOf(mProduct.getExchangeIntegral());
                BigDecimal b1 = new BigDecimal(price);
                BigDecimal b2 = new BigDecimal(exchangeIntegral);
                BigDecimal b3 = new BigDecimal(mProduct.getPointRate());
                String integralPrice;
                if ((b1.subtract(b2.divide(b3))).doubleValue() <= 0)
                    integralPrice = "可用: " + (int) Math.ceil((b1.multiply(b3)).doubleValue()) + "积分";
                else
                    integralPrice = "可用: ¥" + b1.subtract(b2.divide(b3)) + "+" + exchangeIntegral + "积分";
                tv_integral_price.setText(integralPrice);
                tv_integral_price.setVisibility(View.VISIBLE);
                dialog.setPrice(integralPrice.replace("可用: ", ""));
            } else {
                tv_integral_price.setVisibility(View.GONE);
                dialog.setPrice(priceFmt);
            }
            storeCount = SPShopUtils.getShopStoreCount(priceModelKey, specPriceMap);
            tv_sale_count.setVisibility(View.VISIBLE);
        } else {      //有活动
            if (spActivity.getPromType() == 1 || spActivity.getPromType() == 2 || spActivity.getPromType() == 3) {
                rl_group_info.setVisibility(View.VISIBLE);          //活动信息显示
                rl_goods_price.setVisibility(View.GONE);            //普通商品信息隐藏
                long endTime = spActivity.getEndTime() * 1000;      //活动结束时间
                long curTime = spActivity.getCurTime() * 1000;      //当前时间
                long arriveTime = endTime - curTime;
                group_coutdownv.start(arriveTime);                  //活动倒计时
                tv_group_price.setText("¥" + spActivity.getPromPrice());
                tv_group_old_price.setText("原价" + mProduct.getMarketPrice());
                if (spActivity.getPromType() == 2) {      //是团购
                    tv_group_num.setText("已有" + spActivity.getVirtualNum() + "人参团");
                    tv_group_num.setVisibility(View.VISIBLE);
                    tv_sale_count.setVisibility(View.GONE);
                } else {      //不是团购
                    tv_group_num.setVisibility(View.GONE);
                    tv_sale_count.setVisibility(View.VISIBLE);
                }
                if (spActivity.getPromType() == 3)                //是商品促销
                    promote.setVisibility(View.VISIBLE);
                else
                    promote.setVisibility(View.GONE);             //不是商品促销
            } else {
                rl_group_info.setVisibility(View.GONE);           //活动信息隐藏
                rl_goods_price.setVisibility(View.VISIBLE);       //普通商品信息显示
                String priceFmt = "¥" + spActivity.getPromPrice();
                SpannableString spanStr = SPUtils.getFirstCharScale(getActivity(), priceFmt);
                tv_goods_price.setText(spanStr);
                tv_group_num.setVisibility(View.GONE);
                tv_sale_count.setVisibility(View.VISIBLE);
            }
            mProduct.setShopPrice(spActivity.getPromPrice() + "");
            if (mProduct.getExchangeIntegral() > 0) {
                String exchangeIntegral = String.valueOf(mProduct.getExchangeIntegral());
                BigDecimal b1 = new BigDecimal(String.valueOf(spActivity.getPromPrice()));
                BigDecimal b2 = new BigDecimal(exchangeIntegral);
                BigDecimal b3 = new BigDecimal(mProduct.getPointRate());
                String integralPrice;
                if ((b1.subtract(b2.divide(b3))).doubleValue() <= 0)
                    integralPrice = "可用: " + (int) Math.ceil((b1.multiply(b3)).doubleValue()) + "积分";
                else
                    integralPrice = "可用: ¥" + b1.subtract(b2.divide(b3)) + "+" + exchangeIntegral + "积分";
                tv_integral_price.setText(integralPrice);
                dialog.setPrice(integralPrice.replace("可用: ", ""));
            } else {
                tv_integral_price.setVisibility(View.GONE);
                dialog.setPrice("¥" + spActivity.getPromPrice());
            }
            if (spActivity.getPromType() == 1 || spActivity.getPromType() == 2)
                storeCount = spActivity.getPromStoreCount();
            else
                storeCount = SPShopUtils.getShopStoreCount(priceModelKey, specPriceMap);
        }
        dialog.setCount(storeCount);
        if (storeCount > 0)
            tv_store_count.setText("库存：" + storeCount);
        else
            tv_store_count.setText("库存：0");
        if (storeCount > 0 && isDispatch) {
            btn_promptly_buy.setEnabled(true);
            btn_promptly_buy.setBackgroundResource(R.drawable.button_yellow_selector);
        } else {
            btn_promptly_buy.setEnabled(false);
            btn_promptly_buy.setBackgroundColor(getResources().getColor(R.color.button_bg_gray));
        }
    }

    /**
     * 监听购物车数量变化广播
     */
    class ShopCartChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SPMobileConstants.ACTION_SHOPCART_CHNAGE))
                loadCartCount();
        }
    }

    /**
     * 刷新购物车数据
     */
    public void loadCartCount() {
        SPShopCartManager shopCartManager = SPShopCartManager.getInstance(activity);
        int shopCount = shopCartManager.getShopCount();
        if (shopCount <= 0) {
            SPShopCartManager.getInstance(activity).reloadCart();
            badge.setText("0");
            badge.hide();
            badge2.setText("0");
            badge2.hide();
        } else {
            badge.setText(String.valueOf(shopCount));
            badge.show();
            badge2.setText(String.valueOf(shopCount));
            badge2.show();
        }
    }

    /**
     * 关注店铺
     */
    public void attentionStore() {
        if (!SPMobileApplication.getInstance().isLogined()) {
            showToastUnLogin();
            toLoginPage("productDetail");
            return;
        }
        int storeId = activity.getStore().getStoreId();
        showLoadingSmallToast();
        SPStoreRequest.collectOrCancelStoreWithID(storeId, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showToast(msg);
                if (msg.equals("关注成功")) {
                    if (mProduct.getIsVirtual() == 1 || mProduct.getExchangeIntegral() > 0)
                        iv_attention2.setImageResource(R.drawable.icon_attention_checked);
                    else
                        iv_attention.setImageResource(R.drawable.icon_attention_checked);
                } else {
                    if (mProduct.getIsVirtual() == 1 || mProduct.getExchangeIntegral() > 0)
                        iv_attention2.setImageResource(R.drawable.icon_attention_normal);
                    else
                        iv_attention.setImageResource(R.drawable.icon_attention_normal);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                if (SPUtils.isTokenExpire(errorCode)) {       //如果是token超时,跳转到登录页
                    toLoginPage("productDetail");
                    return;
                }
                showToast(msg);
            }
        });
    }

    /**
     * 收藏商品
     */
    public void collectGoods() {
        String goodsID = activity.getProduct().getGoodsID();
        if (!SPMobileApplication.getInstance().isLogined()) {
            showToastUnLogin();
            toLoginPage("productDetail");
            return;
        }
        showLoadingSmallToast();
        SPPersonRequest.collectOrCancelGoodsWithID(goodsID, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                hideLoadingSmallToast();
                showToast(msg);
                if (msg.equals("收藏成功"))
                    iv_collect.setImageResource(R.drawable.icon_collect_checked);
                else
                    iv_collect.setImageResource(R.drawable.icon_collect_normal);
            }
        }, new SPFailuredListener(activity) {
            @Override
            public void onRespone(String msg, int errorCode) {
                hideLoadingSmallToast();
                if (SPUtils.isTokenExpire(errorCode)) {
                    toLoginPage("productDetail");
                    return;
                }
                showToast(msg);
            }
        });
    }

    /**
     * 联系客服
     */
    public void connectCustomer(String qq) {
        try {
            String customerUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(customerUrl)));
        } catch (Exception e) {
            e.printStackTrace();
            showToast(getString(R.string.no_install_qq));
        }
    }

    /**
     * 立即购买
     */
    public void buyNowClick() {
        String priceModelKey = SPShopUtils.getPriceModelkey(selectSpecMap.values());
        int itemId = SPShopUtils.getItemId(priceModelKey, specPriceMap);
        Intent confirmIntent = new Intent(getActivity(), SPConfirmOrderActivity_.class);
        confirmIntent.putExtra("isBuyNow", true);
        confirmIntent.putExtra("goodId", activity.getProduct().getGoodsID());
        confirmIntent.putExtra("itemId", itemId + "");
        confirmIntent.putExtra("num", "1");
        activity.startActivity(confirmIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPMobileConstants.Result_Code_GetValue) {
            if (data == null || data.getSerializableExtra("consignee") == null) return;
            selectRegionConsignee = (SPConsigneeAddress) data.getSerializableExtra("consignee");
            String fullRegion = selectRegionConsignee.getProvinceName() + selectRegionConsignee.getCityName()
                    + selectRegionConsignee.getDistrictName();
            selectRegionConsignee.setAddress(fullRegion);
            SPSaveData.saveAddress(activity, selectRegionConsignee);
            tv_send_addr.setText(fullRegion);
            String redionId = selectRegionConsignee.getDistrict();
            refreshDispatch(redionId);
        }
    }

    //刷新商品物流存货
    private void refreshDispatch(String redionId) {
        RequestParams params = new RequestParams();
        params.put("goods_id", activity.getProductId());
        params.put("region_id", redionId);
        SPShopRequest.getDispatch(params, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                int count = Integer.valueOf(tv_store_count.getText().toString().replace("库存：", ""));
                double result = (double) response;
                if (result > 0) {            //物流信息
                    dispatch_fee_tv.setText(result + "元");
                    dialog.canDispatch(true);
                } else if (result == 0) {
                    dispatch_fee_tv.setText("包邮");
                    dialog.canDispatch(true);
                } else {
                    dispatch_fee_tv.setText(msg);
                    isDispatch = false;
                    dialog.canDispatch(false);
                }
                if (result < 0 || count <= 0) {         //不配送(库存为0)
                    btn_promptly_buy.setEnabled(false);
                    btn_promptly_buy.setBackgroundColor(getResources().getColor(R.color.button_bg_gray));
                } else {
                    btn_promptly_buy.setEnabled(true);
                    btn_promptly_buy.setBackgroundResource(R.drawable.button_yellow_selector);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
            }
        });
    }

    public void loadWeb() {
        String url = String.format(SPMobileConstants.URL_GOODS_DETAIL_CONTENT, mProduct.getGoodsID());
        mWebView.loadUrl(url);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(mSpecChangeReceiver);
        activity.unregisterReceiver(mShopCartChangeReceiver);
        SPSaveData.putValue(activity, SPMobileConstants.KEY_CART_COUNT, 1);
    }

}
