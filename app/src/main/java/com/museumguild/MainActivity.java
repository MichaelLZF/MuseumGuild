package com.museumguild;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.museumguild.view.activities.BottomActivity;

public class MainActivity extends AppCompatActivity{

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.main);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
                sp.edit().putBoolean("is_user_guide_showed",true);
                Intent in = new Intent();
                in.setClass(MainActivity.this, BottomActivity.class);
                startActivity(in);
                finish();
            }
        });
    }
}
