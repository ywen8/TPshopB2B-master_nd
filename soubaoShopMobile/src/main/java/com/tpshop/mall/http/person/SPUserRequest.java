package com.tpshop.mall.http.person;

import android.support.annotation.NonNull;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.soubao.tpshop.utils.SPJsonUtil;
import com.tpshop.mall.common.SPMobileConstants;
import com.tpshop.mall.http.base.SPFailuredListener;
import com.tpshop.mall.http.base.SPMobileHttptRequest;
import com.tpshop.mall.http.base.SPSuccessListener;
import com.tpshop.mall.model.SPAppData;
import com.tpshop.mall.model.SPBrowItem;
import com.tpshop.mall.model.SPBrowingProduct;
import com.tpshop.mall.model.SPCommentData;
import com.tpshop.mall.model.SPCommentNum;
import com.tpshop.mall.model.SPMessageList;
import com.tpshop.mall.model.SPMessageListData;
import com.tpshop.mall.model.SPMessageNotice;
import com.tpshop.mall.model.SPServiceComment;
import com.tpshop.mall.model.person.SPUser;
import com.tpshop.mall.model.shop.SPServiceCommentList;
import com.tpshop.mall.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ben on 2016/7/9
 */
public class SPUserRequest {

    /**
     * 用户登录
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=User&a=login
     */
    public static void doLogin(String phoneNumber, String password, String pushId, final SPSuccessListener successListener,
                               final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "login");
        RequestParams params = new RequestParams();
        try {
            String md5pwd = SPUtils.md5WithAuthCode(password);
            params.put("username", phoneNumber);
            params.put("password", md5pwd);
            params.put("push_id", pushId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        SPUser user = SPJsonUtil.fromJsonToModel(response.getJSONObject(SPMobileConstants.Response.RESULT), SPUser.class);
                        successListener.onRespone(msg, user);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    successListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 第三方登录
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=User&a=thirdLogin
     */
    public static void loginWithThirdPart(String openId, String unionid, String from, String nickname, String headPic, String gender,
                                          String pushId, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "thirdLogin");
        RequestParams params = new RequestParams();
        params.put("openid", openId);
        params.put("unionid", unionid);
        params.put("oauth", from);
        params.put("nickname", nickname);
        params.put("head_pic", headPic);
        params.put("sex", gender);
        params.put("push_id", pushId);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        SPUser user = SPJsonUtil.fromJsonToModel(response.getJSONObject(SPMobileConstants.Response.RESULT), SPUser.class);
                        successListener.onRespone(msg, user);
                    } else {
                        if (!response.isNull(SPMobileConstants.Response.RESULT)) {
                            String result = response.getString(SPMobileConstants.Response.RESULT);
                            if (!result.equals("")) {
                                if (Integer.valueOf(result) == 100)
                                    failuredListener.onRespone(msg, Integer.valueOf(result));
                                else
                                    failuredListener.onRespone(msg, -1);
                            } else {
                                failuredListener.onRespone(msg, -1);
                            }
                        } else {
                            failuredListener.onRespone(msg, -1);
                        }
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 绑定第三方账户
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=User&a=bind_account
     */
    public static void bindAccount(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "bind_account");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        SPUser user = SPJsonUtil.fromJsonToModel(response.getJSONObject(SPMobileConstants.Response.RESULT), SPUser.class);
                        successListener.onRespone(msg, user);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    //注册
    public static void doRegister(String phoneNumber, String password, String code, String pushId, String capache, boolean isBind,
                                  final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "reg");
        RequestParams params = new RequestParams();
        params.put("username", phoneNumber);
        params.put("password", password);
        params.put("password2", password);
        params.put("scene", "1");
        params.put("code", code);
        params.put("push_id", pushId);
        params.put("capache", capache);
        if (isBind) params.put("is_bind", 1);
        SPMobileHttptRequest.post(url, params, getResponseHandler(successListener, failuredListener));
    }

    //发送短信验证码
    public static void sendSmsValidateCode(String phoneNumber, String scene, final SPSuccessListener successListener,
                                           final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Home", "Api", "send_validate_code");
        RequestParams params = new RequestParams();
        params.put("mobile", phoneNumber);
        params.put("scene", scene);
        SPMobileHttptRequest.post(url, params, getResponseHandler(successListener, failuredListener));
    }

    /**
     * 更新用户信息
     *
     * @param successListener
     * @param failuredListener
     * @url index.php?m=Api&c=User&a=updateUserInfo
     */
    public static void updateUserInfo(SPUser user, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "updateUserInfo");
        RequestParams params = new RequestParams();
        params.put("user_id", user.getUserID());
        params.put("nickname", user.getNickName());
        params.put("email", user.getEmail());
        params.put("sex", user.getSex());
        params.put("birthday", user.getBirthday());
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    @NonNull
    private static JsonHttpResponseHandler getResponseHandler(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONObject resultObject = new JSONObject();
                        if (response.has(SPMobileConstants.Response.RESULT))
                            resultObject = response.getJSONObject(SPMobileConstants.Response.RESULT);
                        if (resultObject != null) {
                            SPUser user = SPJsonUtil.fromJsonToModel(resultObject, SPUser.class);
                            successListener.onRespone(msg, user);
                        } else {
                            successListener.onRespone(msg, "success");
                        }
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        };
    }

    /**
     * 找回密码
     *
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     * @url index.php?m=Api&c=User&a=forgetPassword
     */
    public static void forgetPassword(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "forgetPassword");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 退出登录
     *
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     * @url index.php?m=Api&c=User&a=logout
     */
    public static void logout(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "logout");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, status);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 修改密码
     *
     * @param oldPassword      旧密码
     * @param newPassword      新密码
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     * @url index.php?m=Api&c=User&a=password
     */
    public static void modifyPassword(String oldPassword, String newPassword, final SPSuccessListener successListener,
                                      final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "password");
        RequestParams params = new RequestParams();
        try {
            String oldPwd = SPUtils.md5WithAuthCode(oldPassword);
            String newPwd = SPUtils.md5WithAuthCode(newPassword);
            params.put("old_password", oldPwd);
            params.put("new_password", newPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 访问记录
     *
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     * @url index.php?m=Api&c=user&a=visit_log
     */
    public static void getBrowHistory(int page, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "visit_log");
        RequestParams params = new RequestParams();
        params.put("p", page);
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray resultArray = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        if (resultArray != null) {
                            List<SPBrowingProduct> products = SPJsonUtil.fromJsonArrayToList(resultArray, SPBrowingProduct.class);
                            for (SPBrowingProduct product : products) {
                                List<SPBrowItem> items = SPJsonUtil.fromJsonArrayToList(product.getVisitListArray(), SPBrowItem.class);
                                if (items != null) {
                                    product.setVisitList(items);
                                }
                            }
                            successListener.onRespone(msg, products);
                        }
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 清空访问记录
     *
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     * @URL index.php?m=api&c=user&a=clear_visit_log
     */
    public static void clearBrowHistory(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "clear_visit_log");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 评价各种状态数量
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=User&a=comment_num
     */
    public static void getCommentNum(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "comment_num");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONObject object = response.getJSONObject(SPMobileConstants.Response.RESULT);
                        SPCommentNum commentNum = SPJsonUtil.fromJsonToModel(object, SPCommentNum.class);
                        successListener.onRespone(msg, commentNum);
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
     * 某个消息类型的列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=user&a=message
     */
    public static void getMessageList(int category, int page, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "message");
        RequestParams params = new RequestParams();
        params.put("p", page);
        params.put("category", category);
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray resultArray = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        if (resultArray != null) {
                            List<SPMessageList> messageList = SPJsonUtil.fromJsonArrayToList(resultArray, SPMessageList.class);
                            for (SPMessageList message : messageList) {
                                if (message.getMessageDataArray() != null) {
                                    SPMessageListData messageData = SPJsonUtil.fromJsonToModel(message.getMessageDataArray(),
                                            SPMessageListData.class);
                                    if (messageData != null)
                                        message.setMessageData(messageData);
                                }
                            }
                            successListener.onRespone(msg, messageList);
                        }
                    } else {
                        failuredListener.onRespone(msg, -1);
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
     * 消息通知接口
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=user&a=message_notice
     */
    public static void getMessageNotice(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "message_notice");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray resultArray = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        if (resultArray != null) {
                            List<SPMessageNotice> messageList = SPJsonUtil.fromJsonArrayToList(resultArray, SPMessageNotice.class);
                            successListener.onRespone(msg, messageList);
                        }
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
     * 获取消息开关
     *
     * @param successListener
     * @param failuredListener
     * @URL index.php?m=api&c=user&a=message_switch
     */
    public static void getMessageSwitch(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "message_switch");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray jsonarray = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        if (jsonarray != null) {
                            boolean[] isSwitch = new boolean[jsonarray.length()];
                            for (int i = 0; i < jsonarray.length(); i++)
                                isSwitch[i] = (boolean) jsonarray.get(i);
                            successListener.onRespone(msg, isSwitch);
                        }
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
        });
    }

    /**
     * 获取用户更多详细信息
     *
     * @param successListener
     * @param failuredListener
     * @URL index.php?m=Api&c=user&a=userInfo
     */
    public static void getUserInfo(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "userInfo");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONObject object = response.getJSONObject(SPMobileConstants.Response.RESULT);
                        SPUser user = SPJsonUtil.fromJsonToModel(object, SPUser.class);
                        successListener.onRespone(msg, user);
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
        });
    }

    /**
     * 忘记密码(第一步)
     *
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     * @URL index.php?m=Api&c=user&a=forgetPasswordInfo
     */
    public static void forgetPasswordInfo(String account, String checkCode, final SPSuccessListener successListener,
                                          final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "forgetPasswordInfo");
        RequestParams params = new RequestParams();
        params.put("account", account);
        params.put("capache", checkCode);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0 && response.getString(SPMobileConstants.Response.RESULT) != null) {
                        JSONObject object = response.getJSONObject(SPMobileConstants.Response.RESULT);
                        SPUser user = SPJsonUtil.fromJsonToModel(object, SPUser.class);
                        successListener.onRespone(msg, user);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 设置消息开关
     *
     * @param type             0系统消息,1物流通知,2优惠促销,3商品提醒,4我的资产,5商城好店
     * @param val              开关值,1开,0关
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     * @URL index.php?m=api&c=user&a=message_switch
     */
    public static void setMessageSwitch(int type, int val, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "message_switch");
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("val", val);
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 清空消息列表
     *
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     * @url index.php?m=api&c=user&a=clear_message
     */
    public static void cleanMessage(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "clear_message");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 添加服务评价
     *
     * @param successListener  成功回调
     * @param failuredListener 失败回调
     * @url index.php?m=api&c=user&a=add_service_comment
     */
    public static void addserviceComment(RequestParams params, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "add_service_comment");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, -1);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 获取服务评价列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=user&a=service_comment
     */
    public static void getserviceComment(int p, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        RequestParams params = new RequestParams();
        params.put("p", p);
        String url = SPMobileHttptRequest.getRequestUrl("user", "service_comment");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray resultArray = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        List<SPServiceCommentList> serviceCommentList = SPJsonUtil.fromJsonArrayToList(resultArray, SPServiceCommentList.class);
                        for (SPServiceCommentList comment : serviceCommentList) {
                            if (comment.getServiceCommentDateArray() != null) {
                                List<SPServiceComment> serviceData = SPJsonUtil.fromJsonArrayToList((comment.getServiceCommentDateArray()),
                                        SPServiceComment.class);
                                if (serviceData != null)
                                    comment.setServiceCommentDate(serviceData);
                            }
                        }
                        successListener.onRespone(msg, serviceCommentList);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 获取评价列表
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=user&a=comment
     */
    public static void getComment(int p, int status, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        RequestParams params = new RequestParams();
        params.put("p", p);
        params.put("status", status);  //0:未评论,1:已评论
        String url = SPMobileHttptRequest.getRequestUrl("user", "comment");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONArray resultArray = response.getJSONArray(SPMobileConstants.Response.RESULT);
                        List<SPCommentData> serviceData = SPJsonUtil.fromJsonArrayToList(resultArray, SPCommentData.class);
                        successListener.onRespone(msg, serviceData);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 上传头像
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=User&a=upload_headpic
     */
    public static void uploadHeadPic(File headPic, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("User", "upload_headpic");
        RequestParams params = new RequestParams();
        try {
            params.put("head_pic", headPic, "image/jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = response.getString(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, status);
                    }
                } catch (Exception e) {
                    failuredListener.onRespone(e.getMessage(), -1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 获取版本信息
     *
     * @param version          版本号
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=api&c=App&a=latest
     */
    public static void getUpdateInfo(String version, final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        RequestParams params = new RequestParams();
        params.put("version", version);
        String url = SPMobileHttptRequest.getRequestUrl("App", "latest");
        SPMobileHttptRequest.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        JSONObject result = response.getJSONObject(SPMobileConstants.Response.RESULT);
                        SPAppData appData = SPJsonUtil.fromJsonToModel(result, SPAppData.class);
                        successListener.onRespone(msg, appData);
                    } else {
                        failuredListener.onRespone(msg, -1);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 判断token是否失效
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=user&c=user&a=token_status
     */
    public static void getTokenStatus(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("user", "token_status");
        SPMobileHttptRequest.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, status);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 获取入驻商家信息
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Newjoin&a=getApply
     */
    public static void getApply(final SPSuccessListener successListener, final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Newjoin", "getApply");
        SPMobileHttptRequest.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        String merchants = response.getJSONObject(SPMobileConstants.Response.RESULT).getString(SPMobileConstants.Response.STATUS);
                        successListener.onRespone(msg, merchants);
                    } else {
                        failuredListener.onRespone(msg, status);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 入驻商家信息第一步
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Newjoin&a=basicInfo
     */
    public static void setbasicInfo1(final RequestParams params, final SPSuccessListener successListener,
                                     final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Newjoin", "basicInfo");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, status);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 入驻商家信息第二步
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Newjoin&a=storeInfo
     */
    public static void setbasicInfo2(final RequestParams params, final SPSuccessListener successListener,
                                     final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Newjoin", "storeInfo");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, status);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

    /**
     * 入驻商家信息第三步
     *
     * @param successListener  success description
     * @param failuredListener failure description
     * @URL index.php?m=Api&c=Newjoin&a=remark
     */
    public static void setbasicInfo3(final RequestParams params, final SPSuccessListener successListener,
                                     final SPFailuredListener failuredListener) {
        assert (successListener != null);
        assert (failuredListener != null);

        String url = SPMobileHttptRequest.getRequestUrl("Newjoin", "remark");
        SPMobileHttptRequest.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String msg = (String) response.get(SPMobileConstants.Response.MSG);
                    int status = response.getInt(SPMobileConstants.Response.STATUS);
                    if (status > 0) {
                        successListener.onRespone(msg, status);
                    } else {
                        failuredListener.onRespone(msg, status);
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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                failuredListener.onRespone(throwable.getMessage(), statusCode);
            }
        });
    }

}
