package com.museumguild;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.museumguild.manage.LoginManager;
import com.museumguild.utils.Constant;
import com.museumguild.view.fragment.HomeFragment;
import com.museumguild.view.fragment.PayFragment;
import com.museumguild.view.fragment.PersonalFragment;

/**
 * Created by hasee on 2017/8/17.
 */

public class MainActivity extends Activity implements View.OnClickListener{
    private LinearLayout home;
    private LinearLayout scanSearch;
    private LinearLayout personal;

    private HomeFragment homeFragment;
    private PayFragment payFragment;
    private PersonalFragment personalFragment;

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
                if (payFragment == null)
                {
                    payFragment = new PayFragment();
                }
                transaction.replace(R.id.id_content, payFragment);
                transaction.commit();
            }
        }
    }

    private void setDefaultFragment()
    {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        payFragment = new PayFragment();
        transaction.replace(R.id.id_content, payFragment);
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
                transaction.replace(R.id.id_content, homeFragment);
                break;
            case R.id.scansearchlayout:
                if (payFragment == null)
                {
                    payFragment = new PayFragment();
                }
                transaction.replace(R.id.id_content, payFragment);
                break;
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, CaptureActivity.class);
//                startActivityForResult(intent, Constant.REQUEST_SCAN_CODE);
//                break;
            case R.id.personallayout:
//                if(LoginManager.getIns().isLogin()){
                    if (personalFragment == null)
                    {
                        personalFragment = new PersonalFragment();
                    }
                    transaction.replace(R.id.id_content, personalFragment);
//                }
                break;
        }
        // transaction.addToBackStack();
        // 事务提交
        transaction.commit();
    }
}
