package com.museumguild.view.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.museumguild.R;

/**
 * Created by Administrator on 2016/6/14.
 */
public abstract class BaseContentActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvTitle;
    private LinearLayout lyLeft;
    private TextView tvLeft;
    private TextView tvRight;
    private ImageView ivRight;
    private FrameLayout flContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_normal);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tvTitle = (TextView) findViewById(R.id.title_center_tv);
        lyLeft = (LinearLayout) findViewById(R.id.title_left_ly);
        tvLeft = (TextView) findViewById(R.id.title_left_tv);
        ivRight = (ImageView) findViewById(R.id.title_right_iv);
        tvRight = (TextView) findViewById(R.id.title_right_tv);
        lyLeft.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        flContent = (FrameLayout) findViewById(R.id.content_fl);
        initTitle(lyLeft, tvLeft, tvTitle, ivRight, tvRight);

    }

    protected void setChildContentView(int layoutId)
    {
        View view = LayoutInflater.from(this).inflate(layoutId, null);
        setChildContentView(view);
    }

    protected void setChildContentView(View view)
    {
        FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        flContent.addView(view, fl);
    }

    protected abstract void initTitle(LinearLayout lyLeft, TextView tvLeft,
                                      TextView tvTitle, ImageView ivRight, TextView tvRight);

    protected void left()
    {
        finish();
    }

    protected void right()
    {

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_left_tv:
            case R.id.title_left_ly:
                left();
                break;
            case R.id.title_right_tv:
            case R.id.title_right_iv:
                right();
                break;
        }
    }

    @Override
    protected int initContentViewId() {
        return R.layout.title_content_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
