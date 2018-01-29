package com.tpshop.mall.common;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.tpshop.mall.R;
import com.tpshop.mall.utils.SPDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qianxiaoai on 2016/7/7
 */
public class PermissionUtils {

    public static final int CODE_CAMERA = 4;
    public static final int CODE_CALL_PHONE = 3;
    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_GET_ACCOUNTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_MULTI_PERMISSION = 100;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 8;

    private static final String[] requestPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_LOGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SET_DEBUG_APP,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_APN_SETTINGS};

    public interface PermissionGrant {
        void onPermissionGranted(int requestCode);
    }

    /**
     * Requests permission.
     */
    public static void requestPermission(final Activity activity, final int requestCode, PermissionGrant permissionGrant) {
        if (activity == null) return;
        if (requestCode < 0 || requestCode >= requestPermissions.length) return;
        final String requestPermission = requestPermissions[requestCode];
        //如果是6.0以下的手机,ActivityCompat.checkSelfPermission()会始终等于PERMISSION_GRANTED
        //如果用户关闭了你申请的权限,ActivityCompat.checkSelfPermission(),会导致程序崩溃
        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
        } catch (RuntimeException e) {
            e.printStackTrace();
            SPDialogUtils.showToast(activity, "please open this permission");
            return;
        }
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission))
                shouldShowRationale(activity, requestCode, requestPermission);
            else
                ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);
        } else {
            SPDialogUtils.showToast(activity, "opened:" + requestPermissions[requestCode]);
            permissionGrant.onPermissionGranted(requestCode);
        }
    }

    private static void requestMultiResult(Activity activity, String[] permissions, int[] grantResults, PermissionGrant permissionGrant) {
        if (activity == null) return;
        Map<String, Integer> perms = new HashMap<>();
        ArrayList<String> notGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            perms.put(permissions[i], grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                notGranted.add(permissions[i]);
        }
        if (notGranted.size() == 0) {
            SPDialogUtils.showToast(activity, "all permission success" + notGranted);
            permissionGrant.onPermissionGranted(CODE_MULTI_PERMISSION);
        } else {
            openSettingActivity(activity, "those permission need granted!");
        }
    }

    /**
     * 一次申请多个权限
     */
    public static void requestMultiPermissions(final Activity activity, PermissionGrant grant) {
        final List<String> permissionsList = getNoGrantedPermission(activity, false);
        final List<String> shouldRationalePermissionsList = getNoGrantedPermission(activity, true);
        if (permissionsList == null || shouldRationalePermissionsList == null) return;
        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), CODE_MULTI_PERMISSION);
        } else if (shouldRationalePermissionsList.size() > 0) {
            showMessageOKCancel(activity, "should open those permission", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(activity, shouldRationalePermissionsList
                            .toArray(new String[shouldRationalePermissionsList.size()]), CODE_MULTI_PERMISSION);
                }
            });
        } else {
            grant.onPermissionGranted(CODE_MULTI_PERMISSION);
        }
    }

    private static void shouldShowRationale(final Activity activity, final int requestCode, final String requestPermission) {
        String[] permissionsHint = activity.getResources().getStringArray(R.array.permissions);
        showMessageOKCancel(activity, "Rationale: " + permissionsHint[requestCode], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);
            }
        });
    }

    private static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", null).create().show();
    }

    public static void requestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                                @NonNull int[] grantResults, PermissionGrant permissionGrant) {
        if (activity == null) return;
        if (requestCode == CODE_MULTI_PERMISSION) {
            requestMultiResult(activity, permissions, grantResults, permissionGrant);
            return;
        }
        if (requestCode < 0 || requestCode >= requestPermissions.length) {
            SPDialogUtils.showToast(activity, "illegal requestCode:" + requestCode);
            return;
        }
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionGrant.onPermissionGranted(requestCode);
        } else {
            String[] permissionsHint = activity.getResources().getStringArray(R.array.permissions);
            openSettingActivity(activity, "Result" + permissionsHint[requestCode]);
        }
    }

    private static void openSettingActivity(final Activity activity, String message) {
        showMessageOKCancel(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
    }

    private static ArrayList<String> getNoGrantedPermission(Activity activity, boolean isShouldRationale) {
        ArrayList<String> permissions = new ArrayList<>();
        for (String requestPermission : requestPermissions) {
            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
            } catch (RuntimeException e) {
                e.printStackTrace();
                SPDialogUtils.showToast(activity, "please open those permission");
                return null;
            }
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                    if (isShouldRationale)
                        permissions.add(requestPermission);
                } else {
                    if (!isShouldRationale)
                        permissions.add(requestPermission);
                }
            }
        }
        return permissions;
    }

}
