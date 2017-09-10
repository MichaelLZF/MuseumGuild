package com.museumguild.view.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.museumguild.R;

/**
 * Created by hasee on 2017/8/17.
 */

public class ScanFragment extends Fragment implements View.OnClickListener {
    private Button languageButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scan_success,container,false);
        languageButton = (Button)view.findViewById(R.id.language);
        languageButton.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
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
    }


}
