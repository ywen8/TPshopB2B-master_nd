package com.tpshop.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.tpshop.mall.R;

import java.util.ArrayList;
import java.util.List;

public class SPStarView extends RelativeLayout implements View.OnClickListener {

    List<Button> starList;
    private int rank = -1;             //星星数量
    private Button star1Btn;
    private Button star2Btn;
    private Button star3Btn;
    private Button star4Btn;
    private Button star5Btn;
    int mNormalStarResid, mCheckStarResid;
    private boolean isResponseClickListener;

    public SPStarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        starList = new ArrayList<>();
        View parent = LayoutInflater.from(context).inflate(R.layout.star_view, this, true);
        star1Btn = (Button) parent.findViewById(R.id.star1_btn);
        star1Btn.setOnClickListener(this);
        starList.add(star1Btn);
        star2Btn = (Button) parent.findViewById(R.id.star2_btn);
        star2Btn.setOnClickListener(this);
        starList.add(star2Btn);
        star3Btn = (Button) parent.findViewById(R.id.star3_btn);
        star3Btn.setOnClickListener(this);
        starList.add(star3Btn);
        star4Btn = (Button) parent.findViewById(R.id.star4_btn);
        star4Btn.setOnClickListener(this);
        starList.add(star4Btn);
        star5Btn = (Button) parent.findViewById(R.id.star5_btn);
        star5Btn.setOnClickListener(this);
        starList.add(star5Btn);
        isResponseClickListener = true;
    }

    public void setStarSize(int size) {
        star1Btn.setWidth(size);
        star1Btn.setHeight(size - size / 3);
        star2Btn.setWidth(size);
        star2Btn.setHeight(size - size / 3);
        star3Btn.setWidth(size);
        star3Btn.setHeight(size - size / 3);
        star4Btn.setWidth(size);
        star4Btn.setHeight(size - size / 3);
        star5Btn.setWidth(size);
        star5Btn.setHeight(size - size / 3);
    }

    @Override
    public void onClick(View v) {
        if (!isResponseClickListener) return;
        switch (v.getId()) {
            case R.id.star1_btn:
                rank = 0;
                break;
            case R.id.star2_btn:
                rank = 1;
                break;
            case R.id.star3_btn:
                rank = 2;
                break;
            case R.id.star4_btn:
                rank = 3;
                break;
            case R.id.star5_btn:
                rank = 4;
                break;
        }
        checkStart(rank);
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int num) {
        rank = num;
        checkStart(rank);
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
            Button starBtn = starList.get(i);
            if (i <= rank)
                starBtn.setBackgroundResource(R.drawable.icon_start_checked_normal);
            else
                starBtn.setBackgroundResource(R.drawable.icon_start_uncheck_normal);
        }
    }

    public void setIsResponseClickListener(boolean isResponser) {
        this.isResponseClickListener = isResponser;
    }

}
