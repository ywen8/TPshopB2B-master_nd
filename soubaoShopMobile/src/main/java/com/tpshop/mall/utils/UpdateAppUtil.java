package com.tpshop.mall.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zw on 2017/6/3
 */
public class UpdateAppUtil {

    private Context context;

    public void downLoadApk(Context context, final String url) {
        this.context = context;
        final ProgressDialog pd;
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(url, pd);
                    installApk(file);             //安装APK
                    pd.dismiss();                 //结束掉进度条对话框
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {          //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            pd.setMax(conn.getContentLength());         //获取到文件的大小
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "tpshop.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                pd.setProgress(total);         //获取当前下载量
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    //安装apk
    private void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
