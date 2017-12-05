package com.museumguild.view.fragment;

/**
 * Created by hasee on 2017/8/17.
 */
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.museumguild.R;
import com.museumguild.arvideo.ARActivity;
import com.museumguild.view.view.ImageTextView;

/**
 * Created by hasee on 2017/8/17.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private ImageTextView testbwg;//测试页面
    private SearchView search;
//    private LinearLayout bwg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);
        search = (SearchView)view.findViewById(R.id.search);
        search.setIconifiedByDefault(false);
        testbwg = (ImageTextView) view.findViewById(R.id.bwgimage1);
        testbwg.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bwgimage1:
                Intent in = new Intent();
                in.setClass(HomeFragment.this.getActivity(), ARActivity.class);
                startActivity(in);
                break;
        }
    }
}