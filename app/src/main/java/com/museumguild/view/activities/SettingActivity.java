package com.museumguild.view.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.museumguild.bean.MyInfo;
import com.museumguild.http.ResultCallback;
import com.museumguild.manage.LoginManager;
import com.museumguild.manage.ServerApiManager;
import com.museumguild.manage.UploadFileManager;
import com.museumguild.utils.Constant;
import com.museumguild.utils.Log;
import com.museumguild.utils.Util;
import com.museumguild.view.view.CircleBitmapDisplayer;
import com.museumguild.view.view.CustomAlertDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.museumguild.R;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingActivity extends BaseActivity {

    private Spinner sp_setting_sex;
    private ImageView iv_setting_head;
    private PopupWindow popWindow;
    private Uri cameraPhotoUri;
    private String mSex = "男";
    private String resultSex;
    private String mHeadPic;
    private String resultPic;
    private List<String> serverPaths;
    private EditText nickname;
    private EditText birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_normal);
        toolbar.setNavigationIcon(R.drawable.selector_capture_back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView)findViewById(R.id.toolbar_title)).setText("设置");

        findViewById(R.id.tv_login_out).setOnClickListener(this);
        iv_setting_head = (ImageView) findViewById(R.id.iv_setting_head);
        iv_setting_head.setOnClickListener(this);
        sp_setting_sex = (Spinner) findViewById(R.id.sp_setting_sex);
        String[] sexs = {"男","女"};
        ArrayAdapter adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_setting_sex.setAdapter(adapter);
        sp_setting_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_setting_sex.setSelection(position);
                resultSex = position == 0 ? "男" : "女";
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        findViewById(R.id.ly_clear_cache).setOnClickListener(this);
        nickname = (EditText)findViewById(R.id.nickname);
        birthday = (EditText)findViewById(R.id.birthday);
        birthday.setInputType(InputType.TYPE_NULL);
        birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePickerDialog();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_login_out:
                LoginManager.getIns().logoutAndBackToLoginPage(this);
                finish();
                break;
            case R.id.iv_setting_head:
                showPopupWindow(v);
                break;
            case R.id.layout_take_photo://点击拍照
                cameraPhotoUri = Util.takePhoto(this);
                popWindow.dismiss();
                break;
            case R.id.layout_choose_photo://点击选择照片
                Util.pickPhoto(this);
                popWindow.dismiss();
                break;
            case R.id.ly_clear_cache://清理缓存
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Util.clearAllCache(SettingActivity.this);
                        //恢复解锁的文物
//                        Set<String> set = new HashSet<String>();
//                        LockInform lockInform = new LockInform(set);
//                        boolean flag = FileUtil.saveObject(SettingActivity.this, "object.dat", lockInform);
                        dialog.dismiss();

                    }
                });
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setTitle("确定要清理缓存吗?");
                dialog.show();
                break;
            case R.id.birthday:
                showDatePickerDialog();
                break;
        }
    }
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(SettingActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                birthday.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showPopupWindow(View v) {
        //显示popup Window
        if(popWindow!=null && popWindow.isShowing()){
            lightOff();
            popWindow.dismiss();
        }
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_report_main, null);
        popWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT,true);
        popupView.findViewById(R.id.layout_take_photo).setOnClickListener(this);
        popupView.findViewById(R.id.layout_choose_photo).setOnClickListener(this);
        popWindow.setOutsideTouchable(false);
        //设置PopupWindow的弹出和消失效果
        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        lightOff();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.m(TAG, "onActivityResult");
        if (resultCode == RESULT_OK)
        {
            switch (requestCode ) {
                case Constant.SELECT_PIC_BY_PICK_PHOTO:
                    Log.m(TAG, "back from choose photo page");
                    resultPic = Util.doPhoto( data ,this);
                    break;
                case Constant.SELECT_PIC_BY_TACK_PHOTO:
                    Log.m(TAG, "back from camera ");
                    resultPic = Util.getPhotoPath(data , cameraPhotoUri, this);
                default:
                    break;
            }
            if(!TextUtils.isEmpty(resultPic))
            {
            ImageLoader.getInstance().displayImage(resultPic,iv_setting_head, Util.getRoundImageOption());
            //iv_setting_head.setImageBitmap(BitmapFactory.decodeFile(resultPic));
            }
        }
    }


    @Override
    protected void initData() {
        showMyInfo();
    }

    private void showMyInfo(){
        showProgress();
        ServerApiManager.getIns().show_myinfo(new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                hideProgress();
            }

            @Override
            public void onResponse(String response) {
                hideProgress();
                boolean isSuccess = Util.parseResultIsRequstOk(response);
                if (isSuccess) {
                    MyInfo myInfo = new Gson().fromJson(response, MyInfo.class);
                    nickname.setText(myInfo.nickname);
                    birthday.setText(myInfo.birthday);
                    boolean isMale = true;
                    if (!TextUtils.isEmpty(myInfo.sex) && myInfo.sex.equals("女")) {
                        isMale = false;
                    }
                    if(!TextUtils.isEmpty(myInfo.pic)){
                        mHeadPic = Util.dealWithServerPicPath(myInfo.pic);
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .cacheInMemory(true)
                                .cacheOnDisk(true)
                                .displayer(new CircleBitmapDisplayer())
                                .build();
                        ImageLoader.getInstance().displayImage(mHeadPic,iv_setting_head,options);
                    }
                    resultSex = mSex = isMale ? "男" : "女";
                    sp_setting_sex.setSelection(isMale?0:1);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showSaveDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showSaveDialog();
    }

    private void showSaveDialog() {
        if( !TextUtils.isEmpty(resultPic) || (!TextUtils.isEmpty(resultSex) && !resultSex.endsWith(mSex))){
            final CustomAlertDialog dialog = new CustomAlertDialog(this,this);
            dialog.buildDialog("是否保存个人设置?","");
            dialog.setLeftButton("否");
            dialog.setRightButton("是");
            dialog.setOnButtonClickListener(new CustomAlertDialog.OnButtonClickListener() {
                @Override
                public void OnButtonClick(int clickID) {
                    dialog.dismiss();
                    if(clickID== CustomAlertDialog.OnButtonClickListener.RIGHT_CLICK){
                        showProgress();
                        //首先上传图片,再保存
                        new Thread(){
                            @Override
                            public void run() {
                                if(!TextUtils.isEmpty(resultPic)){
                                    ArrayList<String> uploadDataList = new ArrayList<String>();
                                    uploadDataList.add(resultPic);
                                    serverPaths = UploadFileManager.getInstance().uploadImg(uploadDataList);
                                }
                                if(null != serverPaths && serverPaths.size()>0){
                                    mHeadPic = serverPaths.get(0);
                                }
                                ServerApiManager.getIns().set_myinfo(nickname.getText().toString(), mHeadPic, resultSex, birthday.getText().toString(), new ResultCallback<String>() {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        hideProgress();
                                        SettingActivity.super.onBackPressed();
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        hideProgress();
                                        SettingActivity.super.onBackPressed();
                                    }
                                });
                            }
                        }.start();
                    }else if(clickID== CustomAlertDialog.OnButtonClickListener.LEFT_CLICK){
                        SettingActivity.super.onBackPressed();
                    }
                }
            });
            dialog.show();
        }else{
            super.onBackPressed();
        }
    }

}
