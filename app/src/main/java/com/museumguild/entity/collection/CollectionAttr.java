package com.museumguild.entity.collection;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/13.
 */
public class CollectionAttr implements Serializable {
    private static final int PIC_TYPE = 1;
    private static final int AUDIO_TYPE = 2;
    private String name;
    private String url;
    private String thumbnail;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPicType()
    {
        return this.type == PIC_TYPE;
    }

    public boolean isAudioType()
    {
        return this.type == AUDIO_TYPE;
    }

    @Override
    public String toString() {
        return "CollectionAttr{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", type=" + type +
                '}';
    }
}
