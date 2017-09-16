package com.museumguild.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.museumguild.MainActivity;
import com.museumguild.R;
import com.museumguild.http.ResultCallback;
import com.museumguild.manage.LoginManager;
import com.museumguild.manage.ServerApiManager;
import com.museumguild.utils.Log;
import com.museumguild.utils.PrefenceUtil;
import com.museumguild.utils.Util;
import com.squareup.okhttp.Request;

/**
 * Created by hasee on 2017/8/18.
 */

public class LoginActivity extends BaseActivity {
    private EditText et_username;
    private EditText et_password;
    private boolean isLogOut;
    private View tv_goto_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initContentViewId() {
        return R.layout.login;
    }

    @Override
    protected void initView() {

        findViewById(R.id.login).setOnClickListener(this);
        et_username = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);
        tv_goto_register = findViewById(R.id.register);
        tv_goto_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.login) {
            if (checkInput()) {
                showProgress();
                final String username = et_username.getText().toString();
                final String password = Util.transferToMD5(et_password.getText().toString());
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
                                Log.m(TAG, "onResponse:" + response);
                                boolean isSuccess = Util.parseResultIsRequstOk(response);
                                if (isSuccess) {
                                    Util.toast("登录成功");
                                    PrefenceUtil.write("username", username);
                                    PrefenceUtil.write("password", password);
                                    LoginManager.getIns().setUserInfoAndToken(response);
                                    finish();
                                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                } else {
                                    Util.toast(Util.parseResultFailMsg(response));
                                }
                            }
                        });
            }
        } else if (v.getId() == R.id.register) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    public boolean checkInput() {
        if (TextUtils.isEmpty(et_username.getText())) {
            Util.toast("用户名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(et_password.getText())) {
            Util.toast("密码不能为空");
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void initData() {
        isLogOut = getIntent().getBooleanExtra("isLogOut", false);
    }
}

