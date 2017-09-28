package com.museumguild.view.holder.find;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.museumguild.R;

/**
 * Created by WZTCM on 2016/12/16.
 */
public class FindFunctionDialog extends Dialog {
    //类似于自定义View，必须实现一个非默认的构造方法
    public FindFunctionDialog(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置不显示对话框标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置对话框显示哪个布局文件
        setContentView(R.layout.find_function_layout);
        //对话框也可以通过资源id找到布局文件中的组件，从而设置点击侦听
        LinearLayout ly = (LinearLayout) findViewById(R.id.ic_find_function_dialog);
        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}