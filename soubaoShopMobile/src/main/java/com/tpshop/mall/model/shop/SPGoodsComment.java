package com.tpshop.mall.model.shop;

import com.tpshop.mall.model.SPModel;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by admin on 2016/6/20
 */
public class SPGoodsComment implements SPModel {

    private String goodsID;                    //商品ID
    private String content;                    //内容
    private String addTime;                    //评论时间
    private String headUrl;                    //用户头像
    private String username;                   //用户名称
    private String nickname;                   //用户昵称
    private String specName;                   //规格名称
    private String commentID;                  //评论ID
    private String goodsRank;
    private String serviceRank;
    private String deliverRank;
    private List<String> images;               //评论图片
    private JSONArray imageArray;

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "commentID", "comment_id",
                "goodsID", "goods_id",
                "addTime", "add_time",
                "goodsRank", "goods_rank",
                "serviceRank", "service_rank",
                "deliverRank", "deliver_rank",
                "imageArray", "img",
                "specName", "spec_key_name",
                "nickname", "nickname",
                "headUrl", "head_pic",
        };
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getGoodsRank() {
        return goodsRank;
    }

    public void setGoodsRank(String goodsRank) {
        this.goodsRank = goodsRank;
    }

    public String getServiceRank() {
        return serviceRank;
    }

    public void setServiceRank(String serviceRank) {
        this.serviceRank = serviceRank;
    }

    public String getDeliverRank() {
        return deliverRank;
    }

    public void setDeliverRank(String deliverRank) {
        this.deliverRank = deliverRank;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public JSONArray getImageArray() {
        return imageArray;
    }

    public void setImageArray(JSONArray imageArray) {
        this.imageArray = imageArray;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

}
