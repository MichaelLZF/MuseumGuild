package com.museumguild.view.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.museumguild.R;
import com.museumguild.manage.LoginManager;
import com.museumguild.utils.Util;
import com.museumguild.view.activities.LoginActivity;
import com.museumguild.view.activities.SettingActivity;

/**
 * Created by hasee on 2017/8/17.
 */

public class PersonalFragment extends Fragment{
    private Button loginRegister;
    private ImageView headpic;
    private DrawerLayout mDrawerLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment,container,false);
        loginRegister = (Button)view.findViewById(R.id.login_register);
        headpic = (ImageView)view.findViewById(R.id.head_pic);
//        if(LoginManager.getIns().isLogin()){
//            loginRegister.setText("已登录");
//        }
//        else{
            loginRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(PersonalFragment.this.getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
//        }
        headpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginManager.getIns().isLogin()){
//                    Intent intent = new Intent(PersonalFragment.this.getActivity(),SettingActivity.class);
//                    intent.putExtra("photo", true);
//                    startActivity(intent);
                    startActivity(new Intent(PersonalFragment.this.getActivity(), SettingActivity.class));
//                    clickDrawer();
                }else{
                    Util.toast("请登录");
                    startActivity(new Intent(PersonalFragment.this.getActivity(),LoginActivity.class));
                }
            }
        });

        return view;
    }
//    private void clickDrawer() {
//        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mDrawerLayout.closeDrawers();
//        } else {
//            mDrawerLayout.openDrawer(GravityCompat.START);
//        }
//    }

}