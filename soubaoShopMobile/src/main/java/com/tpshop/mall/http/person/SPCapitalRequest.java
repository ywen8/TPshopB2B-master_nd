package com.tpshop.mall.http.person;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPStringUtils;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPMobileHttptRequest;
import com.tpshop.mall.http.base.SPSuccessListener;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lenovo on 2017/5/16
 */
public class SPCapitalRequest {

    /**
     * 获取图形验证码
     *
     * @param successListener
     * @param failuredListener
     * @URL index.php?m=Api&c=User&a=verify
     */
    public static void getVerifyCodeSuccess(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "verify");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        String code = response.getString(SPMobileConstants.Response.RESULT);
                        successListener.onRespone(msg, code);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 提款申请
     *
     * @param successListener
     * @param failuredListener
     * @URL index.php?m=api&c=User&a=withdrawals
     */
    public static void postWithdraw(String account_bank, String account_name, String bank_name, String money, String verify_code,
                                    final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "withdrawals");
        RequestParams params = new RequestParams();
        if (!SPStringUtils.isEmpty(account_bank))
            params.put("account_bank", account_bank);
        if (!SPStringUtils.isEmpty(account_name))
            params.put("account_name", account_name);
        if (!SPStringUtils.isEmpty(bank_name))
            params.put("bank_name", bank_name);
        if (!SPStringUtils.isEmpty(money))
            params.put("money", money);
        if (!SPStringUtils.isEmpty(verify_code))
            params.put("verify_code", verify_code);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    if (status > 0) {
                        successListener.onRespone(msg, msg);
                    } else {
                        failuredListener.handleResponse(msg, status);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

}
