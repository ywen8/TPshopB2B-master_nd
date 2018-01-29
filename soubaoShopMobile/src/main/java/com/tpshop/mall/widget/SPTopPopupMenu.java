/*
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年11月14日 下午8:17:18
 * Description: 顶部弹出菜单
 *
 * @version V1.0
 */
package com.tpshop.mall.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tpshop.mall.R;
import com.tpshop.mall.adapter.SPTopPopupMenuAdapter;

import java.util.ArrayList;

/**
 * Created by admin on 2016/8/17
 */
public class SPTopPopupMenu extends PopupWindow {

    private int myType;                              //1:左上,2:右上,3:顶部中间
    private int myWidth;
    private ListView myLv;
    private View myMenuView;
    private Context myContext;
    private LinearLayout popupLL;
    private boolean myIsDirty = true;                //判断是否需要添加或更新列表子类项
    private ArrayList<String> myItems;
    private OnItemClickListener myOnItemClickListener;

    public SPTopPopupMenu(Context context, int width, OnItemClickListener onItemClickListener, ArrayList<String> items, int type) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myMenuView = inflater.inflate(R.layout.top_popup, null);
        this.myContext = context;
        this.myItems = items;
        this.myOnItemClickListener = onItemClickListener;
        this.myType = type;
        this.myWidth = width;
        initWidget();
        setPopup();
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        myLv = (ListView) myMenuView.findViewById(R.id.popup_lv);
        popupLL = (LinearLayout) myMenuView.findViewById(R.id.popup_layout);
        myLv.setOnItemClickListener(myOnItemClickListener);
        if (myType == 1) {
            android.widget.RelativeLayout.LayoutParams lpPopup = (android.widget.RelativeLayout.LayoutParams) popupLL.getLayoutParams();
            lpPopup.width = (int) (myWidth * 1.0 / 4);
            lpPopup.setMargins(0, 0, (int) (myWidth * 3.0 / 4), 0);
            popupLL.setLayoutParams(lpPopup);
        }
    }

    /**
     * 设置popup的样式
     */
    private void setPopup() {
        this.setContentView(myMenuView);                                                            //设置AccessoryPopup的view
        this.setWidth((int) (myWidth * 1.0 / 4));                                                   //设置AccessoryPopup弹出窗体的宽度
        int menuHeight = Float.valueOf(myContext.getResources().getDimension(R.dimen.height_row)).intValue() * this.myItems.size();                     //设置AccessoryPopup弹出窗体的高度
        this.setHeight(menuHeight);
        this.setFocusable(true);                                                                    //设置AccessoryPopup弹出窗体可点击
        if (myType == 1) {
            this.setAnimationStyle(R.style.AnimTopLeft);
        } else if (myType == 2) {
            this.setAnimationStyle(R.style.AnimTopRight);
        } else {
            this.setAnimationStyle(R.style.AnimTopMiddle);
        }
        ColorDrawable dw = new ColorDrawable(0x33000000);                                           //实例化一个ColorDrawable颜色为半透明
        popupLL.setBackgroundDrawable(dw);                                                          //设置SelectPicPopupWindow弹出窗体的背景
        myMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = popupLL.getBottom();
                int left = popupLL.getLeft();
                int right = popupLL.getRight();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height || x < left || x > right)
                        dismiss();
                }
                return true;
            }
        });
    }

    /**
     * 显示弹窗界面
     */
    public void show(View view) {
        if (myIsDirty) {
            myIsDirty = false;
            SPTopPopupMenuAdapter adapter = new SPTopPopupMenuAdapter(myContext, myItems, myType);
            myLv.setAdapter(adapter);
        }
        showAsDropDown(view, 0, 0);
    }

}
