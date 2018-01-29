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
 * Description:商城Fragment
 *
 * @version V1.0
 */
package com.tpshop.mall.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.soubao.tpshop.utils.SPCommonUtils;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPSearchCommonActivity_;
import com.tpshop.mall.activity.person.user.SPMessageCenterActivity_;
import com.tpshop.mall.activity.shop.SPProductDetailActivity_;
import com.tpshop.mall.activity.shop.SPProductListActivity;
import com.tpshop.mall.adapter.SPCategoryLeftAdapter;
import com.tpshop.mall.adapter.SPCategoryRightAdapter;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.global.SPMobileApplication;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.http.category.SPCategoryRequest;
import com.tpshop.mall.model.SPCategory;
import com.tpshop.mall.zxing.MipcaActivityCapture;

import java.util.HashMap;
import java.util.List;

/**
 * 首页 -> 分类
 */
public class SPCategoryFragment extends SPBaseFragment implements View.OnClickListener, SPCategoryRightAdapter.OnAdListener {

    View emptypView;
    Button refreshBtn;
    public Context mContext;
    SPCategory mLeftCategory;                                //左边分类
    private ListView mLeftLstv;                              //左边大分类listview
    SPCategoryLeftAdapter mLeftAdapter;
    static SPCategoryFragment mFragment;
    SPCategoryRightAdapter mRightAdapter;
    private List<SPCategory> mLeftCategorys;                 //分类数据集合
    private List<SPCategory> mRightCategorys;
    private StickyGridHeadersGridView mRightGdv;             //右边小分类listview
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private HashMap<Integer, List<SPCategory>> mRightCategorysCache;

    public static SPCategoryFragment newInstance() {
        if (mFragment == null)
            mFragment = new SPCategoryFragment();
        return mFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRightCategorysCache = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, null, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        super.init(view);
        return view;
    }

    @Override
    public void initSubView(View view) {
        emptypView = view.findViewById(R.id.empty_rlayout);
        refreshBtn = (Button) view.findViewById(R.id.refresh_btn);
        mLeftLstv = (ListView) view.findViewById(R.id.category_left_lstv);
        mRightGdv = (StickyGridHeadersGridView) view.findViewById(R.id.category_right_gdvv);
        mRightGdv.setAreHeadersSticky(false);         //设置标题栏不随着列表滑动而滑动
        mLeftAdapter = new SPCategoryLeftAdapter(getActivity());
        mRightAdapter = new SPCategoryRightAdapter(getActivity(), this);
        mLeftLstv.setAdapter(mLeftAdapter);
        mRightGdv.setAdapter(mRightAdapter);
        view.findViewById(R.id.scan_imgv).setOnClickListener(this);
        view.findViewById(R.id.msg_imgv).setOnClickListener(this);
        view.findViewById(R.id.search_key_edtv).setOnClickListener(this);
    }

    @Override
    public void initEvent() {
        mLeftLstv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mLeftAdapter.setSelectIndex(position);
                mLeftAdapter.notifyDataSetChanged();
                mLeftCategory = (SPCategory) mLeftAdapter.getItem(position);
                selectLeftCategory(mLeftCategory);
            }
        });
        mRightGdv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SPCategory category = (SPCategory) mRightAdapter.getItem(position);
                if (category.isBlank()) return;
                startupActivity(category.getId());
            }
        });
        refreshBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refreshData();
            }
        });
    }

    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
    }

    @Override
    public void initData() {
        refreshData();
    }

    public void refreshData() {
        mLeftCategorys = SPMobileApplication.getInstance().getTopCategorys();
        refreshTopCategory();
        if (mLeftCategorys == null || mLeftCategorys.size() < 1) {
            SPCategoryRequest.getCategory(0, new SPSuccessListener() {
                @Override
                public void onRespone(String msg, Object response) {
                    if (response != null) {
                        mLeftCategorys = (List<SPCategory>) response;
                        SPMobileApplication.getInstance().setTopCategorys(mLeftCategorys);
                        refreshTopCategory();
                        emptypView.setVisibility(View.GONE);
                    }
                }
            }, new SPFailuredListener() {
                @Override
                public void onRespone(String msg, int errorCode) {
                    emptypView.setVisibility(View.VISIBLE);
                    showFailedToast(msg);
                }
            });
        }
    }

    private void refreshTopCategory() {
        if (SPCommonUtils.verifyArray(mLeftCategorys)) {
            mLeftAdapter.setSelectIndex(0);
            mLeftAdapter.setData(mLeftCategorys);
            mLeftAdapter.notifyDataSetChanged();
            mLeftCategory = mLeftCategorys.get(0);
            selectLeftCategory(mLeftCategory);
            emptypView.setVisibility(View.GONE);
        } else {
            emptypView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 左边分类点击
     */
    public void selectLeftCategory(SPCategory category) {
        if (category == null) return;
        mLeftCategory = category;
        if (mRightCategorysCache != null && mRightCategorysCache.get(mLeftCategory.getId()) != null) {       //先从缓存获取
            List<SPCategory> cacheCategorys = mRightCategorysCache.get(mLeftCategory.getId());
            mRightAdapter.setData(cacheCategorys);
            mRightAdapter.notifyDataSetChanged();
            return;
        }
        SPCategoryRequest.goodsSecAndThirdCategoryList(category.getId(), new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                if (response != null) {
                    mRightCategorys = (List<SPCategory>) response;
                    thirdCategoryDateChange(mRightCategorys);
                }
            }
        }, new SPFailuredListener() {
            @Override
            public void onRespone(String msg, int errorCode) {
                showFailedToast(msg);
            }
        });
    }

    public void thirdCategoryDateChange(List<SPCategory> thirdCategory) {
        mRightCategorys = thirdCategory;
        if (mRightCategorys != null) {
            mRightAdapter.setData(mRightCategorys);
            mRightCategorysCache.put(mLeftCategory.getId(), mRightCategorys);      //以categoryId为key,mRightCategorys为值,缓存每一个大分类的子分类
            mRightAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh_btn:
                refreshData();
                break;
            case R.id.scan_imgv:      //扫描
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.msg_imgv:      //消息
                if (!checkLogin()) return;
                getActivity().startActivity(new Intent(getActivity(), SPMessageCenterActivity_.class));
                break;
            case R.id.search_key_edtv:      //分类,搜索关键词
                Intent intent2 = new Intent(getActivity(), SPSearchCommonActivity_.class);
                getActivity().startActivity(intent2);
                break;
        }
    }

    boolean checkLogin() {
        if (!SPMobileApplication.getInstance().isLogined()) {
            showToastUnLogin();
            toLoginPage();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SCANNIN_GREQUEST_CODE:
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result");
                    if (result.startsWith(SPMobileConstants.BASE_HOST)) {
                        String[] str = result.split("&");
                        for (String s : str) {
                            if (s.startsWith("id")) {
                                String id = s.substring(3, s.length());
                                Intent intent = new Intent(getActivity(), SPProductDetailActivity_.class);
                                intent.putExtra("goodsID", id);
                                startActivity(intent);
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onAdClick(String cat_id) {
        if (SPStringUtils.isEmpty(cat_id))
            startupActivity(mLeftCategory.getId());
        else
            startupActivity(Integer.valueOf(cat_id));
    }

    public void startupActivity(int cat_id) {
        Intent intent = new Intent(getActivity(), SPProductListActivity.class);
        intent.putExtra("category_id", cat_id);
        getActivity().startActivity(intent);
    }

}
