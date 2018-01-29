package com.tpshop.mall.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tpshop.mall.R;
import com.tpshop.mall.model.distribute.SPTeamModel;
import com.tpshop.mall.utils.SPUtils;

import java.util.List;

/**
 * Created by zw on 2017/6/7
 */
public class MyTeamAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SPTeamModel> teamModels;

    public MyTeamAdaper(Context context, List<SPTeamModel> teamModels) {
        this.mContext = context;
        this.teamModels = teamModels;
    }

    public void updateData(List<SPTeamModel> teamModels) {
        this.teamModels = teamModels;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.distribution_team_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewholder = (ViewHolder) holder;
        SPTeamModel teamModel = teamModels.get(position);
        viewholder.teamName.setText(teamModel.getNickName());
        viewholder.teamTime.setText("加入时间：" + SPUtils.getTimeFormPhpTime(teamModel.getRegTime()));
        viewholder.teamDistribute.setText("+" + teamModel.getDistributMoney() + "元");
        Uri uri = SPUtils.getImageUri(mContext, SPUtils.getImageUrl(teamModel.getHeadPic()));
        if (uri != null)
            viewholder.teamImg.setImageURI(uri);
        else
            viewholder.teamImg.setImageURI(new Uri.Builder().scheme("res").path(String.valueOf(R.drawable.person_default_head)).build());
    }

    @Override
    public int getItemCount() {
        if (this.teamModels == null) return 0;
        return this.teamModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView teamName;
        TextView teamTime;
        TextView teamDistribute;
        SimpleDraweeView teamImg;

        public ViewHolder(View itemView) {
            super(itemView);
            teamImg = (SimpleDraweeView) itemView.findViewById(R.id.teamImg);
            teamName = (TextView) itemView.findViewById(R.id.teamName);
            teamTime = (TextView) itemView.findViewById(R.id.teamTime);
            teamDistribute = (TextView) itemView.findViewById(R.id.teamDistribute);
        }
    }

}
