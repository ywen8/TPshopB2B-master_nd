package com.tpshop.mall.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpshop.mall.R;
import com.tpshop.mall.utils.SPUtils;

/**
 * Created by zw on 2017/6/3
 */
public class UpdateDialog extends Dialog {

    private UpdateDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private String message;
        private Context context;
        private Button cancelBtn;
        private Button downloadBtn;

        public Builder(Context context) {
            this.context = context;
        }

        public UpdateDialog.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public UpdateDialog.Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public UpdateDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final UpdateDialog dialog = new UpdateDialog(context, R.style.dialog);
            View layout = inflater.inflate(R.layout.update_view, null);
            View view = layout.findViewById(R.id.dialog_ll);
            int width = SPUtils.getWindowWidth(context);
            int height = SPUtils.getWindowheight(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new Double(width * 0.65).intValue(),
                    new Double(height * 0.5).intValue());
            view.setLayoutParams(params);
            cancelBtn = (Button) layout.findViewById(R.id.cancelBtn);
            downloadBtn = (Button) layout.findViewById(R.id.downloadBtn);
            if (message != null) ((TextView) layout.findViewById(R.id.updateMsg)).setText(message);
            dialog.setContentView(layout);
            return dialog;
        }

        public void setCancelOnClickListener(View.OnClickListener listener) {
            cancelBtn.setOnClickListener(listener);
        }

        public void setDownloadOnClickListener(View.OnClickListener listener) {
            downloadBtn.setOnClickListener(listener);
        }
    }

}
