package com.museumguild.view.fragment;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.museumguild.R;
import com.museumguild.TestActivity;
import com.museumguild.library.zxing.android.CaptureActivity;
import com.museumguild.manage.LoginManager;
import com.museumguild.utils.Util;
import com.museumguild.view.activities.PayActivity;

/**
 * Created by hasee on 2017/8/17.
 */

public class PayFragment extends Fragment implements View.OnClickListener {
    private Button languageButton;
    private Button payButton;
    private RadioButton adultRadioButton;
    private RadioButton childRadioButton;
    private CheckBox notifycheckBox;
    private TextView notifyItems;

    private ImageView test;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main,container,false);
        languageButton = (Button)view.findViewById(R.id.language);
        payButton = (Button)view.findViewById(R.id.paybutton);
        adultRadioButton = (RadioButton)view.findViewById(R.id.adult);
        childRadioButton = (RadioButton)view.findViewById(R.id.child);
        notifycheckBox = (CheckBox)view.findViewById(R.id.notifycheck);
        notifyItems = (TextView)view.findViewById(R.id.notifyitems);

test = (ImageView)view.findViewById(R.id.testid);
        test.setOnClickListener(this);

        languageButton.setOnClickListener(this);
        payButton.setOnClickListener(this);
        notifyItems.setOnClickListener(this);
        initView();
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.language:
                PopupMenu popup = new PopupMenu(this.getActivity(),v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.language,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Chinese:
                                languageButton.setBackgroundResource(R.drawable.chinesechange);
                                break;
                            case R.id.English:
                                languageButton.setBackgroundResource(R.drawable.englishchange);
                                break;
                            case R.id.Uyghurtili:
                                languageButton.setBackgroundResource(R.drawable.weiyuchange);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
                break;
            case R.id.paybutton://支付成功时跳转到扫描二维码页面（先到支付页面）
                if(payCheck()){
                    Intent intent = new Intent();
//                    intent.setClass(PayFragment.this.getActivity(), CaptureActivity.class);
                    intent.setClass(PayFragment.this.getActivity(), PayActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.notifyitems:
                showNotifyitems();
                break;
//             //test
//            case R.id.testid:
//                Intent intent = new Intent();
//                intent.setClass(PayFragment.this.getActivity(), TestActivity.class);
//                startActivity(intent);
//                break;
        }

    }


    //Paycheck
    private boolean payCheck(){
        if(!LoginManager.getIns().isLogin()){
            Util.toast("请先登录");
            return false;
        }
        else if(!notifycheckBox.isChecked()){
            Util.toast("请先阅读《说明条款》");
            return false;
        }
        return true;
    }

    protected void initView() {
        //RadioButton
        Drawable adult = getResources().getDrawable(R.drawable.adult);
        adult.setBounds(0, 0, 120, 120);
        adultRadioButton.setCompoundDrawables(null, adult, null, null);

        Drawable child = getResources().getDrawable(R.drawable.child);
        child.setBounds(0, 0, 120, 120);
        childRadioButton.setCompoundDrawables(null, child, null, null);
    }
    private void showNotifyitems(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity())
                .setTitle("说明条款")
                .setMessage(R.string.notifydetails);
        builder.setPositiveButton("我同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifycheckBox.setChecked(true);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


}
