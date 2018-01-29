package com.tpshop.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tpshop.mall.R;

import java.util.ArrayList;
import java.util.List;

public class SPStarSmallView extends RelativeLayout {

    List<ImageView> starList;
    int mNormalStarResid, mCheckStarResid;

    public SPStarSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        starList = new ArrayList<>();
        View parent = LayoutInflater.from(context).inflate(R.layout.star_small_view, this, true);
        ImageView star1Imgv = (ImageView) parent.findViewById(R.id.star1_imgv);
        starList.add(star1Imgv);
        ImageView star2Imgv = (ImageView) parent.findViewById(R.id.star2_imgv);
        starList.add(star2Imgv);
        ImageView star3Imgv = (ImageView) parent.findViewById(R.id.star3_imgv);
        starList.add(star3Imgv);
        ImageView star4Imgv = (ImageView) parent.findViewById(R.id.star4_imgv);
        starList.add(star4Imgv);
        ImageView star5Imgv = (ImageView) parent.findViewById(R.id.star5_imgv);
        starList.add(star5Imgv);
    }

    public void setStarImage(int normalStarResid, int checkStarResid) {
        this.mNormalStarResid = normalStarResid;
        this.mCheckStarResid = checkStarResid;
    }

    /**
     * 选中star的索引
     */
    public void checkStart(int rank) {
        for (int i = 0; i < 5; i++) {
            ImageView starImgv = starList.get(i);
            if (i <= rank)
                starImgv.setImageResource(R.drawable.icon_start_checked_normal);
            else
                starImgv.setImageResource(R.drawable.icon_start_uncheck_normal);
        }
    }

}
