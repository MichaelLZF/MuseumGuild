package com.museumguild;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.museumguild.bean.MyInfo;
import com.museumguild.http.ResultCallback;
import com.museumguild.manage.LoginManager;
import com.museumguild.manage.ServerApiManager;
import com.museumguild.utils.Util;
import com.museumguild.view.view.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;


/**
 * Created by hasee on 2017/9/17.
 */

public class TestActivity extends Activity {
    private String mHeadPic;
    private ImageView headpic;
    private TextView loginusername;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testlayout);
        headpic = (ImageView)findViewById(R.id.head_pic);
        loginusername = (TextView)findViewById(R.id.loginusername);
        if (LoginManager.getIns().isLogin()){
            showMyInfo();
        }
    }

    private void showMyInfo(){
        ServerApiManager.getIns().show_myinfo(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                boolean isSuccess = Util.parseResultIsRequstOk(response);
                if (isSuccess) {
                    MyInfo myInfo = new Gson().fromJson(response, MyInfo.class);
                    loginusername.setText(myInfo.username);
//                    boolean isMale = true;
//                    if (!TextUtils.isEmpty(myInfo.sex) && myInfo.sex.equals("女")) {
//                        isMale = false;
//                    }
                    if(!TextUtils.isEmpty(myInfo.pic)){
                        mHeadPic = Util.dealWithServerPicPath(myInfo.pic);
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .cacheInMemory(true)
                                .cacheOnDisk(true)
                                .displayer(new CircleBitmapDisplayer())
                                .build();
                        ImageLoader.getInstance().displayImage(mHeadPic,headpic,options);
                    }
                }
            }
        });
    }


}
