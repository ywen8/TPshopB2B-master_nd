package com.tpshop.mall.model.distribute;

import com.tpshop.mall.model.SPModel;

import java.io.Serializable;

/**
 * Created by zw on 2017/6/13
 */
public class SPDistributeType implements SPModel, Serializable {

    private int typeId;                    //类型id
    private int parentId;                  //父类型id
    private int typeLevel;                 //第几级类型
    private String typeName;               //类型名称

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setTypeLevel(int typeLevel) {
        this.typeLevel = typeLevel;
    }

    public int getTypeLevel() {
        return typeLevel;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "typeId", "id",
                "typeName", "name",
                "parentId", "parent_id",
                "typeLevel", "level",
        };
    }

}
