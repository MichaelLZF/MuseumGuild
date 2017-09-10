package com.museumguild.view.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.museumguild.utils.BitmapUtil;
import com.museumguild.utils.Constant;
import com.museumguild.utils.Log;
import com.museumguild.utils.Util;
import com.museumguild.R;

public class TakePhotoActivity extends BaseActivity {
    private static final int REQUEST_PICK_IMAGE = 1;
    private Uri cameraPhotoUri;
    private ImageView iv_photo;
    private String cameraPath;
    private View tv_choose_this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraPhotoUri = Util.takePhoto(this);
    }

    @Override
    protected int initContentViewId() {
        return R.layout.activity_take_photo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK && requestCode == Constant.SELECT_PIC_BY_TACK_PHOTO){
            Log.m(TAG, "back from camera data:"+data);
            cameraPath = Util.getPhotoPath(data , cameraPhotoUri, this);
            if( null != cameraPath)
            {
                iv_photo.setImageBitmap(BitmapUtil.getImage(cameraPath));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initView() {
        iv_photo = (ImageView)findViewById(R.id.iv_photo);
        findViewById(R.id.tv_re_take).setOnClickListener(this);
        tv_choose_this = findViewById(R.id.tv_choose_this);
        tv_choose_this.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null == cameraPhotoUri){
            tv_choose_this.setVisibility(View.GONE);
        }else{
            tv_choose_this.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())  {
            case R.id.tv_re_take:
                cameraPhotoUri = Util.takePhoto(this);
                break;
        }
    }

    @Override
    protected void initData() {

    }
}
