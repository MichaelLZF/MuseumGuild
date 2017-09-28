package com.museumguild.view.holder.find;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.museumguild.R;

/**
 * Created by WZTCM on 2016/12/12.
 */
public class FindSuccessDialog extends Dialog {
    private String title;
    private String jianjie;
    private Context context;

    public OnConfirmClickedListener listener;
    public FindSuccessDialog(Context context, String title, String jj) {
        super(context);
        this.title = title;
        this.jianjie = jj;
        this.context = context;
    }

    public interface OnConfirmClickedListener {
        public void onConfirmClicked();
    }

    public void setOnConfirmListener(OnConfirmClickedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置不显示对话框标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置对话框显示哪个布局文件
        setContentView(R.layout.find_success_dialog);
        //对话框也可以通过资源id找到布局文件中的组件，从而设置点击侦听

        TextView tv1 = (TextView) findViewById(R.id.find_success_title);
        tv1.setText(title);

        TextView tv2 = (TextView) findViewById(R.id.find_success_jianjie);
        tv2.setText(jianjie);

        Button bt = (Button) findViewById(R.id.find_success_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onConfirmClicked();
            }
        });
    }
}
