package com.tpshop.mall.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by lenovo on 2017/4/29
 */
public class RandomCode {

    private static final char[] CHARS = {            //随机数数组
            '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
            'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    private String code;
    private static RandomCode bmpCode;
    private Random random = new Random();
    private int padding_left, padding_top;
    private static final int DEFAULT_FONT_SIZE = 25;                                      //默认字体大小
    private static final int DEFAULT_CODE_LENGTH = 4;                                     //验证码默认随机数的个数
    private static final int DEFAULT_LINE_NUMBER = 5;                                     //默认线条的条数
    private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
    private static final int DEFAULT_WIDTH = 100, DEFAULT_HEIGHT = 40;                    //验证码的默认宽高
    private static final int BASE_PADDING_LEFT = 10, RANGE_PADDING_LEFT = 15,             //padding值
            BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 20;

    public static RandomCode getInstance() {
        if (bmpCode == null) bmpCode = new RandomCode();
        return bmpCode;
    }

    //验证码图片
    public Bitmap createBitmap(String mycode) {
        padding_left = 0;
        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bp);
        code = mycode;
        c.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(DEFAULT_FONT_SIZE);
        for (int i = 0; i < code.length(); i++) {          //画验证码
            randomTextStyle(paint);
            randomPadding();
            c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
        }
        for (int i = 0; i < DEFAULT_LINE_NUMBER; i++)      //画线条
            drawLine(c, paint);
        c.save(Canvas.ALL_SAVE_FLAG);                      //保存
        c.restore();
        return bp;
    }

    public String getCode() {
        return code;
    }

    //生成验证码
    private String createCode() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < DEFAULT_CODE_LENGTH; i++)
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        return buffer.toString();
    }

    //画干扰线
    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    //生成随机颜色
    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    //随机生成文字样式,颜色,粗细,倾斜度
    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean());         //true为粗体,false为非粗体
        float skewX = random.nextInt(11) / 10;
        skewX = random.nextBoolean() ? skewX : -skewX;
        paint.setTextSkewX(skewX);                           //float类型参数,负数表示右斜,整数左斜
    }

    //随机生成padding值
    private void randomPadding() {
        padding_left += BASE_PADDING_LEFT + random.nextInt(RANGE_PADDING_LEFT);
        padding_top = BASE_PADDING_TOP + random.nextInt(RANGE_PADDING_TOP);
    }

}

