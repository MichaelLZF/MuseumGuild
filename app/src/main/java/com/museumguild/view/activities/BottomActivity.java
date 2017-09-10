package com.museumguild.view.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.museumguild.R;
import com.museumguild.library.zxing.android.CaptureActivity;
import com.museumguild.manage.LoginManager;
import com.museumguild.utils.Constant;
import com.museumguild.view.fragment.HomeFragment;
import com.museumguild.view.fragment.PersonalFragment;
import com.museumguild.view.fragment.PersonalFragmentOK;
import com.museumguild.view.fragment.ScanFragment;

/**
 * Created by hasee on 2017/8/17.
 */

public class BottomActivity extends Activity implements View.OnClickListener{
    private LinearLayout home;
    private LinearLayout scanSearch;
    private LinearLayout personal;

    private HomeFragment homeFragment;
    private PersonalFragment personalFragment;
    private ScanFragment scanFragment;
    private PersonalFragmentOK personalFragmentOK;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bottom_fragment);

        // 初始化控件和声明事件
        home = (LinearLayout) findViewById(R.id.homelayout);
        scanSearch = (LinearLayout)findViewById(R.id.scansearchlayout);
        personal = (LinearLayout) findViewById(R.id.personallayout);
        home.setOnClickListener(this);
        scanSearch.setOnClickListener(this);
        personal.setOnClickListener(this);

        // 设置默认的Fragment
        setDefaultFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constant.REQUEST_SCAN_CODE && resultCode==RESULT_OK){
            if(data!=null){
                FragmentManager fm = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm.beginTransaction();
                if (scanFragment == null)
                    {
                        scanFragment = new ScanFragment();
                    }
                transaction.replace(R.id.id_content, scanFragment);
                transaction.commit();
            }
        }
    }

    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homeFragment = new HomeFragment();
        transaction.replace(R.id.id_content, homeFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v)
    {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId())
        {
            case R.id.homelayout:
                if (homeFragment == null)
                {
                    homeFragment = new HomeFragment();
                }
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.id_content, homeFragment);
                break;
            case R.id.scansearchlayout:
                Intent intent = new Intent();
                intent.setClass(BottomActivity.this, CaptureActivity.class);
                startActivityForResult(intent, Constant.REQUEST_SCAN_CODE);
//                if (scanFragment == null)
//                {
//                    scanFragment = new ScanFragment();
//                }
//                transaction.replace(R.id.id_content, scanFragment);
                break;
            case R.id.personallayout:
                if(LoginManager.getIns().isLogin()){
                    if (personalFragmentOK == null)
                    {
                        personalFragmentOK = new PersonalFragmentOK();
                    }
                    transaction.replace(R.id.id_content, personalFragmentOK);
                }else{
                    if (personalFragment == null)
                    {
                        personalFragment = new PersonalFragment();
                    }
                    transaction.replace(R.id.id_content, personalFragment);
                }

                break;
        }
        // transaction.addToBackStack();
        // 事务提交
        transaction.commit();
    }
}
