package com.tpshop.mall.model;

/**
 * @author liuhao
 */
public class SPCommentNum implements SPModel {

    private String noCommentNum;                         //没有评价数
    private String hadCommentNum;                        //已经评价数
    private String serveCommentNum;                      //服务评价数

    public String getHadCommentNum() {
        return hadCommentNum;
    }

    public void setHadCommentNum(String hadCommentNum) {
        this.hadCommentNum = hadCommentNum;
    }

    public String getNoCommentNum() {
        return noCommentNum;
    }

    public void setNoCommentNum(String noCommentNum) {
        this.noCommentNum = noCommentNum;
    }

    public String getServeCommentNum() {
        return serveCommentNum;
    }

    public void setServeCommentNum(String serveCommentNum) {
        this.serveCommentNum = serveCommentNum;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "hadCommentNum", "had",
                "noCommentNum", "no",
                "serveCommentNum", "serve"
        };
    }

}
