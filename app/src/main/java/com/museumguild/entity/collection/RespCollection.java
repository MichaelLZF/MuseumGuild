package com.museumguild.entity.collection;

import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class RespCollection {
    private int code;
    private String msg;
    private PageParam pageParm;
    private List<Collection> collectionlist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PageParam getPageParm() {
        return pageParm;
    }

    public void setPageParm(PageParam pageParm) {
        this.pageParm = pageParm;
    }

    public List<Collection> getCollectionlist() {
        return collectionlist;
    }

    public void setCollectionlist(List<Collection> collectionlist) {
        this.collectionlist = collectionlist;
    }

    @Override
    public String toString() {
        return "RespCollection{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", pageParm=" + pageParm +
                ", collectionlist=" + collectionlist +
                '}';
    }
}
