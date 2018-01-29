package com.tpshop.mall.model;

import java.io.Serializable;

public class SPAppData implements SPModel, Serializable {

    private int isNew;                     //是否有新版本,0无,1有
    private String url;                    //新版本链接
    private String log;                    //app更新日志

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "isNew", "new",
                "url", "url",
                "log", "log",
        };
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

}
