package com.museumguild.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.museumguild.R;
import com.museumguild.library.zxing.android.CaptureActivity;

/**
 * Created by hasee on 2017/9/19.
 */

public class PayActivity extends Activity implements View.OnClickListener{
    private EditText money;
    private Button paybutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypage);
        money = (EditText)findViewById(R.id.money);
        paybutton = (Button)findViewById(R.id.paybutton);
        paybutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paybutton:
                if(!money.getText().toString().isEmpty()){
                    finish();
                    Intent in = new Intent();
                    in.setClass(PayActivity.this, CaptureActivity.class);
                    startActivity(in);
                    //支付判断;
                }
        }
    }
}
