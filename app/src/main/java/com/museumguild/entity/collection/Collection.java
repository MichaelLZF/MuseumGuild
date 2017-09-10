package com.museumguild.entity.collection;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class Collection implements Serializable {
    public String id;
    public String name;
    public String jianjie;
    public String room;
    public List<CollectionAttr> attlist;
    public int android_x = 0;
    public int android_y = 0;
    public int ibeacon_x = 0;
    public int ibeacon_y = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    public List<CollectionAttr> getAttlist() {
        return attlist;
    }

    public void setAttlist(List<CollectionAttr> attlist) {
        this.attlist = attlist;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", jianjie='" + jianjie + '\'' +
                ", room='" + room + '\'' +
                ", attlist=" + attlist +
                ", android_x=" + android_x +
                ", android_y=" + android_y +
                ", ibeacon_x=" + ibeacon_x +
                ", ibeacon_y=" + ibeacon_y +
                '}';
    }
}
