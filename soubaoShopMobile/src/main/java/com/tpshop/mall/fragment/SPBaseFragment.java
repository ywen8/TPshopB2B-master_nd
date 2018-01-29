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
 * Date: @date 2015年10月20日 下午7:13:14
 * Description:
 *
 * @version V1.0
 */
package com.tpshop.mall.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.activity.common.SPIViewController;
import com.tpshop.mall.activity.person.user.SPLoginActivity;
import com.tpshop.mall.activity.person.user.SPLoginActivity_;
import com.tpshop.mall.utils.SPConfirmDialog;
import com.tpshop.mall.utils.SPDialogUtils;
import com.tpshop.mall.utils.SPLoadingSmallDialog;
import com.tpshop.mall.widget.CustomToast;

import org.json.JSONObject;

public abstract class SPBaseFragment extends Fragment implements SPIViewController {

    public View view;
    public JSONObject mDataJson;
    private boolean hasLoaded = false;                   //标记已加载完成,保证懒加载只能加载一次
    private boolean isCreated = false;                   //标记Fragment是否已经onCreate
    private boolean isVisibleToUser = false;             //界面对于用户是否可见
    private SPLoadingSmallDialog mLoadingSmallDialog;

    public void init(View view) {
        initSubView(view);
        initEvent();
        initData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = true;         //关键步骤
        lazyLoad(this.view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;          //关键步骤
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoad(view, null);
    }

    /**
     * 懒加载方法,获取数据什么的放到这边来使用,在切换到这个界面时才进行网络请求
     */
    private void lazyLoad(View view, Bundle savedInstanceState) {
        if (!isVisibleToUser || hasLoaded || !isCreated)            //如果该界面不对用户显示、已经加载、fragment还没有创建,三种情况任意一种,不获取数据
            return;
        lazyInit(view, savedInstanceState);
        hasLoaded = true;         //关键步骤,确保数据只加载一次
    }

    /**
     * 子类必须实现的方法,这个方法里面的操作都是需要懒加载的
     */
    public abstract void lazyInit(View view, Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCreated = false;
        hasLoaded = false;
    }

    public void showToast(String msg) {
        SPDialogUtils.showToast(getActivity(), msg);
    }

    public void showToastUnLogin() {
        showToast(getString(R.string.toast_person_unlogin));
    }

    public void toLoginPage() {
        toLoginPage(null);
    }

    public void toLoginPage(String from) {
        Intent loginIntent = new Intent(getActivity(), SPLoginActivity_.class);
        if (!SPStringUtils.isEmpty(from)) loginIntent.putExtra(SPLoginActivity.KEY_FROM, from);
        startActivity(loginIntent);
    }

    public void showSuccessToast(String txt) {
        CustomToast.getToast().ToastShow(getContext(), txt, R.drawable.success);
    }

    public void showFailedToast(String txt) {
        CustomToast.getToast().ToastShow(getContext(), txt, R.drawable.fail);
    }

    public void showLoadingSmallToast() {
        mLoadingSmallDialog = new SPLoadingSmallDialog(getActivity());
        mLoadingSmallDialog.setCanceledOnTouchOutside(false);
        mLoadingSmallDialog.show();
    }

    public void hideLoadingSmallToast() {
        if (mLoadingSmallDialog != null) mLoadingSmallDialog.dismiss();
    }

    public void showConfirmDialog(String message, String title, String okText, final SPConfirmDialog.ConfirmDialogListener confirmDialogListener,
                                  final int actionType) {
        SPConfirmDialog.Builder builder = new SPConfirmDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (confirmDialogListener != null)
                    confirmDialogListener.clickOk(actionType);          //设置你的操作事项
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void showConfirmDialog(String message, String title, final SPConfirmDialog.ConfirmDialogListener confirmDialogListener,
                                  final int actionType) {
        showConfirmDialog(message, title, getResources().getString(R.string.ok), confirmDialogListener, actionType);
    }

    /**
     * 初始化子类视图
     */
    public abstract void initSubView(View view);

    public abstract void initEvent();

    public abstract void initData();

    @Override
    public void gotoLoginPage() {
        toLoginPage();
    }

    @Override
    public void gotoLoginPage(String from) {
        toLoginPage(from);
    }

}
