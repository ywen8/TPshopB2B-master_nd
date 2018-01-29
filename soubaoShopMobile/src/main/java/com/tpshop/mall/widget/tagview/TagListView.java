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
 * Description:带箭头的自定义view
 *
 * @version V1.0
 */
package com.tpshop.mall.widget.tagview;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;

import com.soubao.tpshop.utils.SPCommonUtils;
import com.tpshop.mall.R;
import com.tpshop.mall.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kince
 */
public class TagListView extends FlowLayout implements OnClickListener {

    private Tag mSelectTag;                            //选择的Tag
    private Context mContext;
    private int mTtagViewWidth;
    public int mTtagCountOfRow;                        //每行显示tag数量
    private boolean mIsDeleteMode;
    private float mTagListViewHeight;                  //taglistview高度
    private int mTagViewTextColorResId;
    private int mTagViewBackgroundResId;
    private OnTagClickListener mOnTagClickListener;
    private final List<Tag> mTags = new ArrayList<>();
    private OnTagCheckedChangedListener mOnTagCheckedChangedListener;

    public TagListView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public TagListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        init();
    }

    public TagListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        mContext = context;
        init();
    }

    @Override
    public void onClick(View v) {
        if ((v instanceof TagView)) {
            TagView checkTagView = (TagView) v;
            Tag localTag = (Tag) checkTagView.getTag();
            mSelectTag = localTag;
            checkTagView.setSelected(true);
            if (this.mOnTagClickListener != null)
                this.mOnTagClickListener.onTagClick(checkTagView, localTag);
        }
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View childrenView = getChildAt(i);
            if (childrenView instanceof TagView) {
                TagView childrenTagView = (TagView) childrenView;
                if (((Tag) childrenTagView.getTag()).getId() != mSelectTag.getId())
                    childrenTagView.setSelected(false);
            }
        }
    }

    private void init() {
        mTtagCountOfRow = 3;
    }

    private void inflateTagView(final Tag t, boolean b) {
        TagView tagView = (TagView) View.inflate(getContext(), R.layout.tag, null);
        String text = t.getTitle();
        tagView.setText(text);
        tagView.setTag(t);
        float tagMinWidth = mContext.getResources().getDimension(R.dimen.tagMinWidth);
        float marginHorizontal = mContext.getResources().getDimension(R.dimen.margin_space);    //水平间距
        float tagHeight = mContext.getResources().getDimension(R.dimen.tagHeight);
        float textSize = mContext.getResources().getDimension(R.dimen.textSizeNormal);
        Paint paint = new Paint();
        float textWidth = SPUtils.getCharacterWidth(paint, text, textSize);
        float measureTextWidth = marginHorizontal * 2 + textWidth;
        measureTextWidth = Math.max(measureTextWidth, tagMinWidth);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(Float.valueOf(measureTextWidth).intValue(),
                Float.valueOf(tagHeight).intValue());
        tagView.setLayoutParams(layoutParams);           //重新设置宽高
        if (this.mTtagViewWidth <= 0)
            mTtagViewWidth = Float.valueOf(tagMinWidth).intValue();
        if (mTagViewTextColorResId <= 0) {
            int c = getResources().getColor(R.color.color_font_gray);
            tagView.setTextColor(c);
        }
        if (mTagViewBackgroundResId <= 0) {
            mTagViewBackgroundResId = R.drawable.tag_button_bg_unchecked;
            tagView.setBackgroundResource(mTagViewBackgroundResId);
        }
        tagView.setSelected(t.isChecked());
        tagView.setCheckEnable(b);
        if (mIsDeleteMode) {
            int k = (int) TypedValue.applyDimension(1, 5.0F, getContext().getResources().getDisplayMetrics());
            tagView.setPadding(tagView.getPaddingLeft(), tagView.getPaddingTop(), k, tagView.getPaddingBottom());
        }
        if (t.getBackgroundResId() > 0)
            tagView.setBackgroundResource(t.getBackgroundResId());
        if ((t.getLeftDrawableResId() > 0) || (t.getRightDrawableResId() > 0))
            tagView.setCompoundDrawablesWithIntrinsicBounds(t.getLeftDrawableResId(), 0, t.getRightDrawableResId(), 0);
        tagView.setOnClickListener(this);
        tagView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(
                    CompoundButton paramAnonymousCompoundButton,
                    boolean paramAnonymousBoolean) {
                t.setChecked(paramAnonymousBoolean);
                if (TagListView.this.mOnTagCheckedChangedListener != null)
                    TagListView.this.mOnTagCheckedChangedListener.onTagCheckedChanged((TagView) paramAnonymousCompoundButton, t);
            }
        });
        addView(tagView);
    }

    public void addTag(int i, String s) {
        addTag(i, s, false);
    }

    public void addTag(int i, String s, boolean b) {
        addTag(new Tag(i, s), b);
    }

    public void addTag(Tag tag) {
        addTag(tag, false);
    }

    public void addTag(Tag tag, boolean b) {
        mTags.add(tag);
        inflateTagView(tag, b);
    }

    public void addTags(List<Tag> lists) {
        addTags(lists, false);
    }

    public void addTags(List<Tag> lists, boolean b) {
        for (int i = 0; i < lists.size(); i++)
            addTag(lists.get(i), b);
    }

    public List<Tag> getTags() {
        return mTags;
    }

    public float getTagListViewHeight() {
        return mTagListViewHeight;
    }

    public View getViewByTag(Tag tag) {
        return findViewWithTag(tag);
    }

    public void removeTag(Tag tag) {
        mTags.remove(tag);
        removeView(getViewByTag(tag));
    }

    public void setDeleteMode(boolean b) {
        mIsDeleteMode = b;
    }

    public void setOnTagCheckedChangedListener(OnTagCheckedChangedListener onTagCheckedChangedListener) {
        mOnTagCheckedChangedListener = onTagCheckedChangedListener;
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
    }

    public void setTagViewBackgroundRes(int res) {
        mTagViewBackgroundResId = res;
    }

    public void setTagViewTextColorRes(int res) {
        mTagViewTextColorResId = res;
    }

    public void setTags(List<? extends Tag> lists) {
        setTags(lists, false);
    }

    public void setTags(List<? extends Tag> lists, boolean b) {
        removeAllViews();
        mTags.clear();
        int count = lists.size();
        for (int i = 0; i < count; i++)
            addTag(lists.get(i), b);
        int itemHeight = SPCommonUtils.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tag_height));                     //每一个item高度
        int verticalSpacing = SPCommonUtils.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tag_vertical_spacing));                 //垂直间隙
        int listviewPaddingTop = SPCommonUtils.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tag_listview_padding_top));              //taglistview距离顶部间隙
        int listviewBottomTop = SPCommonUtils.dip2px(mContext, mContext.getResources().getDimension(R.dimen.tag_common_margin));              //taglistview距离底部间隙
        int rows = Double.valueOf(Math.ceil(count / 3.0)).intValue();
        mTagListViewHeight = itemHeight * rows + (rows - 1) * verticalSpacing + listviewPaddingTop + listviewBottomTop;        //计算taglistview高度
    }

    public interface OnTagCheckedChangedListener {
        void onTagCheckedChanged(TagView tagView, Tag tag);
    }

    public interface OnTagClickListener {
        void onTagClick(TagView tagView, Tag tag);
    }

    public void setTtagViewWidth(int tagWidth) {
        this.mTtagViewWidth = tagWidth;
    }

}
