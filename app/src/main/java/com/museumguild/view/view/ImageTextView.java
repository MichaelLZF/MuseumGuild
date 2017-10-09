package com.museumguild.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.museumguild.R;

/**
 * Created by hasee on 2017/9/29.
 */

public class ImageTextView extends RelativeLayout {

    private static int IMAGEVIEW_ID = 0x123456;
    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int resourceId = -1;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyImageView);
        ImageView iv = new ImageView(context);
        TextView tv = new TextView(context);
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MyImageView_Text:
                    resourceId = typedArray.getResourceId(
                            R.styleable.MyImageView_Text, 0);
                    tv.setText(resourceId > 0 ? typedArray.getResources().getText(
                            resourceId) : typedArray
                            .getString(R.styleable.MyImageView_Text));
                    break;
                case R.styleable.MyImageView_Src:
                    resourceId = typedArray.getResourceId(
                            R.styleable.MyImageView_Src, 0);
                    iv.setImageResource(resourceId > 0 ?resourceId:R.drawable.ic_launcher);
                    break;
            }
        }
//        this.setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutParams imageset = new LayoutParams(
                300,
                300
        );
        iv.setId(IMAGEVIEW_ID);
        imageset.setMargins(0,40,0,0);
        addView(iv,imageset);


        LayoutParams textset = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        textset.setMargins(0,30,0,0);
        textset.addRule(BELOW,IMAGEVIEW_ID);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(tv,textset);
        typedArray.recycle();
    }

}
