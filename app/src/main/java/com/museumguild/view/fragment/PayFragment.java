package com.museumguild.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.museumguild.R;
import com.museumguild.library.zxing.android.CaptureActivity;

/**
 * Created by hasee on 2017/8/17.
 */

public class PayFragment extends Fragment implements View.OnClickListener {
    private Button languageButton;
    private Button payButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main,container,false);
        languageButton = (Button)view.findViewById(R.id.language);
        payButton = (Button)view.findViewById(R.id.paybutton);
        languageButton.setOnClickListener(this);
        payButton.setOnClickListener(this);
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
            case R.id.paybutton://支付成功时跳转到扫描二维码页面
                Intent intent = new Intent();
                intent.setClass(PayFragment.this.getActivity(), CaptureActivity.class);
                startActivity(intent);
        }

    }


}
