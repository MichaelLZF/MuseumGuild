package com.museumguild.entity.collection;

/**
 * Created by Administrator on 2016/8/13.
 */
public class CollectionType {
    private String room;//  8/21修改 后台 colletion_type修改为了 room
    private String name;

    public String getCollection_type() {
        if(null != room && !"".equals(room))
        {
            return room;
        }
        return name;
    }



    @Override
    public String toString() {
        return "CollectionType{" +
                "collection_type='" + room + '\'' +
                '}';
    }
}
