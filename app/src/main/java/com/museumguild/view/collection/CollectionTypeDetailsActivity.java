package com.museumguild.view.collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.museumguild.R;
import com.museumguild.entity.collection.Collection;
import com.museumguild.view.activities.BaseContentActivity;
import com.museumguild.view.holder.CollectionTypeDetailsHolder;

/**
 * Created by Administrator on 2016/8/13.
 */
public class CollectionTypeDetailsActivity extends BaseContentActivity {
    private TextView tvTitle;
    private CollectionTypeDetailsHolder mDetailsHolder;
    private ImageView ivShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.collection_type_details_layout, null);
        Collection collection = (Collection)getIntent().getSerializableExtra("collection");
        mDetailsHolder = new CollectionTypeDetailsHolder(view, collection);
        setChildContentView(view);
        if(null != tvTitle)
        {
            tvTitle.setText(collection.getName());
        }
    }

    @Override
    protected void initTitle(LinearLayout lyLeft, TextView tvLeft, TextView tvTitle, ImageView ivRight, TextView tvRight) {
        lyLeft.setVisibility(View.VISIBLE);
        this.tvTitle = tvTitle;
        ivShare = ivRight;
        ivShare.setVisibility(View.VISIBLE);
        ivShare.setImageResource(R.drawable.ic_share);
    }

    @Override
    protected void right() {
        super.right();
        if(null != mDetailsHolder){
            mDetailsHolder.shareBtnOnclick();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != mDetailsHolder)
        {
            mDetailsHolder.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mDetailsHolder)
        {
            mDetailsHolder.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null != mDetailsHolder)
        {
            mDetailsHolder.onResume();
        }
    }
}
