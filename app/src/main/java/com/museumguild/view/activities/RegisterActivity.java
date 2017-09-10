package com.museumguild.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.museumguild.R;
import com.museumguild.http.ResultCallback;
import com.museumguild.manage.LoginManager;
import com.museumguild.manage.ServerApiManager;
import com.museumguild.utils.Log;
import com.museumguild.utils.PrefenceUtil;
import com.museumguild.utils.Util;
import com.museumguild.view.fragment.ScanFragment;
import com.squareup.okhttp.Request;

/**
 * Created by hasee on 2017/8/18.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private ScanFragment scanFragment;
    private EditText reusername;
    private EditText repassword;
    private EditText passwordconf;
    private EditText email;
    private EditText phone;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.register);
    }

    @Override
    protected int initContentViewId() {
        return R.layout.register;
    }

    @Override
    protected void initView() {
        reusername = (EditText)findViewById(R.id.username);
        repassword = (EditText)findViewById(R.id.password);
        passwordconf = (EditText)findViewById(R.id.repassword);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        registerButton = (Button)findViewById(R.id.register);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        if(v.getId()==R.id.register){
//            if(checkInput()){
//                Toast.makeText(this, "dengluchengg", Toast.LENGTH_SHORT).show();
//            }
//        }
        super.onClick(v);
        if(v.getId() == R.id.register){
//            Util.toast("进入注册页面");
            if(checkInput()){
                showProgress();
                if(repassword.getText().toString().equals(passwordconf.getText().toString())){
                    ServerApiManager.getIns().register(
                            reusername.getText().toString(),
                            email.getText().toString(),
                            Util.transferToMD5(repassword.getText().toString()),
                            new ResultCallback<String>() {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Util.toast("注册失败");
                                    hideProgress();
                                }

                                @Override
                                public void onResponse(String response) {
                                    Log.m(TAG,"onResponse:"+response);
                                    boolean isSuccess = Util.parseResultIsRequstOk(response);
                                    if(isSuccess){
                                        Util.toast("注册成功");
                                        // 继续登录,进入首页
                                        login();
                                    }else{
                                        hideProgress();
                                        Util.toast(Util.parseResultFailMsg(response));
                                    }
                                }
                            });
                }else{
                    Util.toast("两次密码输入不相同");
//                    Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    private void login() {
        final String username = reusername.getText().toString();
        final String password = Util.transferToMD5(repassword.getText().toString());
        ServerApiManager.getIns().login(
                username,
                password,
                new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Util.toast("登录失败");
                        hideProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        hideProgress();
                        Log.m(TAG,"onResponse:"+response);
                        boolean isSuccess = Util.parseResultIsRequstOk(response);
                        if(isSuccess){
                            Util.toast("登录成功");
                            PrefenceUtil.write("username", username);
                            PrefenceUtil.write("password", password);
                            LoginManager.getIns().setUserInfoAndToken(response);
//                            FragmentManager fm = getFragmentManager();
//                            // 开启Fragment事务
//                            FragmentTransaction transaction = fm.beginTransaction();
//                            if (scanFragment == null)
//                            {
//                                scanFragment = new ScanFragment();
//                            }
//                            transaction.replace(R.id.id_content, scanFragment);
//                            transaction.commit();
                            RegisterActivity.this.startActivity(new Intent(RegisterActivity.this,BottomActivity.class));
                        }else{
                            Util.toast(Util.parseResultFailMsg(response));
                        }
                        finish();
                    }
                });
    }

    public boolean checkInput(){
        if(TextUtils.isEmpty(reusername.getText())){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(repassword.getText())){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(passwordconf.getText())){
            Util.toast("请确认密码");
            return false;
        }
        if(TextUtils.isEmpty(email.getText())){
            Toast.makeText(this, "注册邮箱不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(phone.getText())){
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void initData() {

    }
}
