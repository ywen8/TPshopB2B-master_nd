/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tpshop.mall.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.tpshop.mall.R;
import com.tpshop.mall.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

public final class ViewfinderView extends View {

    boolean isFirst;
    private Paint paint;                                                     //画笔对象的引用
    private int slideTop;                                                    //中间滑动线的最顶端位置
    private int ScreenRate;                                                  //四个绿色边角对应的长度
    private Bitmap resultBitmap;                                             //将扫描的二维码拍下来,这里没有这个功能,暂时不考虑
    private final int maskColor;
    private static float density;                                            //手机的屏幕密度
    private final int resultColor;
    private final int resultPointColor;
    private static final int OPAQUE = 0xFF;
    private static final int TEXT_SIZE = 14;                                 //字体大小
    private static final int CORNER_WIDTH = 5;                               //四个绿色边角对应的宽度
    private static final int SPEEN_DISTANCE = 5;                             //中间那条线每次刷新移动的距离
    private static final int TEXT_PADDING_TOP_O = 35;                        //字体距离扫描框上面的距离
    private static final int TEXT_PADDING_TOP_T = 20;                        //字体距离扫描框上面的距离
    private static final long ANIMATION_DELAY = 10L;                         //刷新界面的时间
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        ScreenRate = (int) (15 * density);           //将像素转换成dp
        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        possibleResultPoints = new HashSet<>(5);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();             //中间的扫描框,你要修改扫描框的大小,去CameraManager里面修改
        if (frame == null) return;
        if (!isFirst) {           //初始化中间线滑动的最上边和最下边
            isFirst = true;
            slideTop = frame.top;
        }
        int width = canvas.getWidth();          //获取屏幕的宽和高
        int height = canvas.getHeight();
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        //画出扫描框外面的阴影部分,共四个部分,扫描框的上面到屏幕上面,扫描框的下面到屏幕下面
        //扫描框的左边面到屏幕左边,扫描框的右边到屏幕右边
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);
        if (resultBitmap != null) {
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {          //画扫描框边上的角,总共8个部分
            //画扫描框上面的字
            paint.setColor(Color.WHITE);
            paint.setTextSize(TEXT_SIZE * density);
            paint.setAlpha(0xDD);
            String text1 = getResources().getString(R.string.scan_text1);
            float textWidth1 = paint.measureText(text1);
            canvas.drawText(text1, (width - textWidth1) / 2, (frame.top - (float) TEXT_PADDING_TOP_O * density), paint);
            String text2 = getResources().getString(R.string.scan_text2);
            float textWidth2 = paint.measureText(text2);
            canvas.drawText(text2, (width - textWidth2) / 2, (frame.top - (float) TEXT_PADDING_TOP_T * density), paint);
            paint.setColor(Color.GREEN);
            canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate, frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top + ScreenRate, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right, frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top + ScreenRate, paint);
            canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left + ScreenRate, frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - ScreenRate, frame.left + CORNER_WIDTH, frame.bottom, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH, frame.right, frame.bottom, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate, frame.right, frame.bottom, paint);
            //绘制中间的线,每次刷新界面,中间的线往下移动SPEEN_DISTANCE
            slideTop += SPEEN_DISTANCE;
            if (slideTop >= frame.bottom) slideTop = frame.top;
            Rect lineRect = new Rect();
            lineRect.left = frame.left;
            lineRect.right = frame.right;
            lineRect.top = slideTop;
            lineRect.bottom = slideTop + 18;
            canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(R.drawable.qrcode_scan_line))).getBitmap(), null, lineRect, paint);
            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible)
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast)
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
            }
            //只刷新扫描框的内容,其他地方不刷新
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
