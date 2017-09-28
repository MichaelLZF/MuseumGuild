package com.museumguild.view.fragment;

/**
 * Created by hasee on 2017/8/17.
 */
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.museumguild.R;
import com.museumguild.TestActivity;
import com.museumguild.view.activities.find.FindCollectionDetailsActivity;

/**
 * Created by hasee on 2017/8/17.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private ImageView testbwg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);
        testbwg = (ImageView)view.findViewById(R.id.bwgtestid);
        testbwg.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bwgtestid:
                Intent in = new Intent();
                in.setClass(HomeFragment.this.getActivity(), TestActivity.class);
                startActivity(in);
                break;
        }
    }
}