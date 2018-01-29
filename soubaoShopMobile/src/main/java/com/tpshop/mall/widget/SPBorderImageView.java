package com.tpshop.mall.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tpshop.mall.R;

public class SPBorderImageView extends ImageView {

    private int color;
    private boolean borderTopShow;
    private boolean borderLeftShow;
    private boolean borderRightShow;
    private boolean borderBottomShow;

    public SPBorderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BorderImageView, 0, 0);
        color = typeArray.getColor(R.styleable.BorderImageView_borderColor, context.getResources().getColor(R.color.separator_line));
        borderLeftShow = typeArray.getBoolean(R.styleable.BorderImageView_borderLeftShow, false);
        borderRightShow = typeArray.getBoolean(R.styleable.BorderImageView_borderRightShow, false);
        borderTopShow = typeArray.getBoolean(R.styleable.BorderImageView_borderTopShow, false);
        borderBottomShow = typeArray.getBoolean(R.styleable.BorderImageView_borderBottomShow, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rec = canvas.getClipBounds();
        if (borderBottomShow) rec.bottom--;
        if (borderRightShow) rec.right--;
        if (borderTopShow) rec.top--;
        if (borderLeftShow) rec.left--;
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rec, paint);
    }

}
