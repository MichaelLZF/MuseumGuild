package com.museumguild.bean;

/**
 * Created by hasee on 2017/8/18.
 */

public class UserInfo {
    public String token;
    public String userid;//用户id
    public String username;//用户名
    public String pic;//头像
    public String sex;//性别

    public UserInfo(String token, String userid, String username, String pic, String sex) {
        this.token = token;
        this.userid = userid;
        this.username = username;
        this.pic = pic;
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
