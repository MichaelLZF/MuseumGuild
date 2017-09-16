package com.museumguild.view.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.museumguild.R;
import com.museumguild.bean.MyInfo;
import com.museumguild.http.ResultCallback;
import com.museumguild.manage.LoginManager;
import com.museumguild.manage.ServerApiManager;
import com.museumguild.utils.Util;
import com.museumguild.view.activities.LoginActivity;
import com.museumguild.view.activities.SettingActivity;
import com.museumguild.view.view.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

/**
 * Created by hasee on 2017/8/17.
 */

public class PersonalFragment extends Fragment implements View.OnClickListener{
    private String mHeadPic;
    private ImageView headpic;
//    private DrawerLayout mDrawerLayout;
    private Button loginoutButton;
    private TextView loginusername;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment, container, false);
        loginoutButton = (Button) view.findViewById(R.id.loginout);
        headpic = (ImageView) view.findViewById(R.id.head_pic);
        headpic.setOnClickListener(this);

        loginoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginManager.getIns().isLogin()){
                    LoginManager.getIns().logoutAndBackToLoginPage(PersonalFragment.this.getActivity());
                }else{
                    Util.toast("未登录，请您先登录");
                }

            }
        });
//        loginRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(PersonalFragment.this.getActivity(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//        listView = (ListView)view.findViewById(R.id.mylistview);
//        listView.addView();
        loginusername = (TextView) view.findViewById(R.id.loginusername);
        loginusername.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.head_pic||v.getId()==R.id.loginusername)
                if (LoginManager.getIns().isLogin()) {
                    startActivity(new Intent(PersonalFragment.this.getActivity(), SettingActivity.class));
                } else {
                    Util.toast("请登录");
                    startActivity(new Intent(PersonalFragment.this.getActivity(), LoginActivity.class));
                }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//    private void clickDrawer() {
//        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mDrawerLayout.closeDrawers();
//        } else {
//            mDrawerLayout.openDrawer(GravityCompat.START);
//        }
//    }

}