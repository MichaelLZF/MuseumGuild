package com.museumguild.entity.collection.find;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by WZTCM on 2016/12/7.
 */
public class LockInform implements Serializable {
    private HashMap<String,Boolean> IsLocked;

    private ArrayList<String> list;

    private Set<String> Locked;

    public LockInform() {

    }

    public LockInform(HashMap<String, Boolean> isLocked) {
        this.IsLocked = isLocked;
    }

    public LockInform(Set<String> Locked) {
        this.Locked = Locked;
    }

    public LockInform(ArrayList<String> Locked) {
        this.list = list;
    }

    public void setIsLocked(HashMap<String, Boolean> isLocked) {
        this.IsLocked = isLocked;
    }

    public HashMap<String, Boolean> getIsLocked( ) {
        return IsLocked;
    }

    public void setLocked(Set<String> Locked) {
        this.Locked = Locked;
    }

    public ArrayList<String> getList( ) {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public Set<String> getLocked( ) {
        return Locked;
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder("");
        for(String key : IsLocked.keySet()){
            build.append(key).append("  ").append(IsLocked.get(key)).append('\'');
        }
        return build.toString();
    }
}
