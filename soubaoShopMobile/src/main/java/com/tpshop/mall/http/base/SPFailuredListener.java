package com.tpshop.mall.http.base;

import com.tpshop.mall.R;
import com.tpshop.mall.SPMainActivity;
import com.tpshop.mall.activity.common.SPIViewController;
import com.tpshop.mall.utils.SPUtils;

/**
 * 首先会验证token是否过期/失效,如果过期/失效会进入登录页面登录
 */
public abstract class SPFailuredListener {

    private SPIViewController viewController;

    public SPFailuredListener() {
    }

    public SPFailuredListener(SPIViewController pViewController) {
        viewController = pViewController;
    }

    public SPIViewController getViewController() {
        return viewController;
    }

    /**
     * 预处理
     */
    public void handleResponse(String msg, int errorCode) {
        boolean isNeedLogin = SPUtils.isTokenExpire(errorCode);
        if (isNeedLogin) {         //去登录
            if (viewController != null) {
                viewController.gotoLoginPage("other");
                String loginTimeOut = SPMainActivity.getmInstance().getString(R.string.login_time_out);
                onRespone(loginTimeOut, errorCode);
            } else {
                onRespone(msg, errorCode);
            }
        } else {
            onRespone(msg, errorCode);
        }
    }

    public abstract void onRespone(String msg, int errorCode);

}
