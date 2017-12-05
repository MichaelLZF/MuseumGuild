package com.museumguild;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.museumguild.ibeacon.iBeaconClass.iBeacon;
import com.museumguild.ibeacon.iBeaconClass;

import com.museumguild.manage.LoginManager;
import com.museumguild.utils.Constant;
import com.museumguild.utils.Log;
import com.museumguild.utils.PrefenceUtil;
import com.museumguild.view.fragment.HomeFragment;
import com.museumguild.view.fragment.MyFragmentPageAdapter;
import com.museumguild.view.fragment.PayFragment;
import com.museumguild.view.fragment.PersonalFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by hasee on 2017/8/17.
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {
    private String username;
    private String password;
    private boolean isAutologin;
    private View content_ly;
    public final String TAG = getClass().getSimpleName();

    private LinearLayout home;
    private LinearLayout scanSearch;
    private LinearLayout personal;
    private ImageView homeimage;
    private TextView hometext;
    private ImageView scanimage;
    private TextView scantext;
    private ImageView personlimage;
    private TextView personltext;


    //蓝牙定位使用
    private boolean mScanning;
    private BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD = 3000;
    //当前手机API版本
    private int currentApiVersion = Build.VERSION.SDK_INT;



    //实现滑动切换fragment
    private ViewPager viewPager;
    //作为指示标签的按钮
    private ImageView cursor;
    //标志指示标签的横坐标
    float cursorX = 0;
    //所有按钮的宽度的数组
    private int[] widthArgs;
    //所有标题按钮的数组
    private LinearLayout[] linArgs;
    private  ArrayList<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.bottom_fragment);
        setContentView(R.layout.activity_main2);

        // 初始化控件和声明事件
        viewPager = (ViewPager)findViewById(R.id.myviewpager);
        home = (LinearLayout) findViewById(R.id.homelayout);
        scanSearch = (LinearLayout) findViewById(R.id.scansearchlayout);
        personal = (LinearLayout) findViewById(R.id.personallayout);

        homeimage = (ImageView)findViewById(R.id.homeimage);
        hometext = (TextView)findViewById(R.id.hometext);
        scanimage = (ImageView)findViewById(R.id.scanimage);
        scantext = (TextView)findViewById(R.id.scantext);
        personlimage = (ImageView)findViewById(R.id.personalimage);
        personltext = (TextView)findViewById(R.id.personaltext);

        //初始化layout数组
        linArgs = new LinearLayout[]{
                home,scanSearch,personal
        };
        //指示标签设置为绿色
        cursor = (ImageView)this.findViewById(R.id.cursor_btn);
        cursor.setBackgroundColor(Color.GREEN);
        fragments = new ArrayList<Fragment>();
        fragments.add(new HomeFragment());
        fragments.add(new PayFragment());
        fragments.add(new PersonalFragment());

        MyFragmentPageAdapter adapter =
                new MyFragmentPageAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        home.setOnClickListener(this);
        scanSearch.setOnClickListener(this);
        personal.setOnClickListener(this);
        //开启蓝牙
        initBluetooth();
        // 设置默认的Fragment
        setDefaultFragment();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_SCAN_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                viewPager.setCurrentItem(1);
                cursorAnim(1);
            }
        }
    }

    private void setDefaultFragment() {
        viewPager.setCurrentItem(1);
        cursorAnim(1);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.homelayout:
                viewPager.setCurrentItem(0);
                cursorAnim(0);
                break;
            case R.id.scansearchlayout:
                viewPager.setCurrentItem(1);
                cursorAnim(1);
                break;
            case R.id.personallayout:
                viewPager.setCurrentItem(2);
                cursorAnim(2);
                break;
        }
    }

    protected void initData() {
        username = PrefenceUtil.readString("username", LoginManager.DEFAULT_USERNAME);
        password = PrefenceUtil.readString("password", "1");
        if (!username.equals(LoginManager.DEFAULT_USERNAME)) {
            Log.m(TAG, "this is auto login...");
            isAutologin = true;
        } else {
            isAutologin = false;
        }
        if (isAutologin) {
            Log.m(TAG, "background Login start");
            LoginManager.getIns().backgroundLogin();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initBluetooth() {
        if (currentApiVersion < 18) {   //判断当前Api是否大于18才能开启蓝牙
            return;
        }
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        initLeScanCallback();
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            //finish();
            return;
        }
        //开启蓝牙
        mBluetoothAdapter.enable();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initLeScanCallback() {
        if (currentApiVersion < 18) {   //判断当前Api是否大于18才能开启蓝牙
            return;
        }
        if (null == mLeScanCallback) {
            mLeScanCallback =
                    new BluetoothAdapter.LeScanCallback() {

                        @Override
                        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                            final iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi, scanRecord);
                            addDevice(ibeacon);
                            Collections.sort(mLeDevices, new Comparator<iBeacon>() {
                                @Override
                                public int compare(iBeacon h1, iBeacon h2) {
                                    return h2.rssi - h1.rssi;
                                }
                            });
                        }
                    };
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = null;

    private ArrayList<iBeacon> mLeDevices = new ArrayList<iBeacon>();

    private void addDevice(iBeaconClass.iBeacon device) { //添加iBeacon信息
        if (device == null) {
            Log.m("DeviceScanActivity ", "device==null ");
            return;
        }

        for (int i = 0; i < mLeDevices.size(); i++) {
            String btAddress = mLeDevices.get(i).bluetoothAddress;
            if (btAddress.equals(device.bluetoothAddress)) {
                mLeDevices.add(i + 1, device);
                mLeDevices.remove(i);
                return;
            }
        }
        mLeDevices.add(device);

    }
    //实现滑动效果

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if(widthArgs == null){
            widthArgs = new int[]{
                    home.getWidth(),
                    scanSearch.getWidth(),
                    personal.getWidth()
            };
        }
        resetButtonColor(-1);
        resetButtonColor(i);
        cursorAnim(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
    //指示器的跳转，传入当前所处的页面的下标
    public void cursorAnim(int curItem){
        //每次调用，就将指示器的横坐标设置为0，即开始的位置
        cursorX = 0;
        //再根据当前的curItem来设置指示器的宽度
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)cursor.getLayoutParams();
        //减去边距*2，以对齐标题栏文字
        lp.width = widthArgs[curItem]-linArgs[0].getPaddingLeft()*2;
        cursor.setLayoutParams(lp);
        //循环获取当前页之前的所有页面的宽度
        for(int i=0; i<curItem; i++){
            cursorX = cursorX + linArgs[i].getWidth();
        }
        //再加上当前页面的左边距，即为指示器当前应处的位置
        cursor.setX(cursorX+linArgs[curItem].getPaddingLeft());
    }
    //重置所有按钮的颜色

    public void resetButtonColor(int i) {
        if(i == -1){
            homeimage.setBackgroundResource(R.drawable.home);
            scanimage.setBackgroundResource(R.drawable.scan_search);
            personlimage.setBackgroundResource(R.drawable.person_outline);
            hometext.setTextColor(Color.BLACK);
            scantext.setTextColor(Color.BLACK);
            personltext.setTextColor(Color.BLACK);
        }
        if(i==0){
            homeimage.setBackgroundResource(R.drawable.homechoose);
            hometext.setTextColor(getResources().getColor(R.color.backcolor));
        }
        if(i == 1){
            scanimage.setBackgroundResource(R.drawable.scan_searchchoose);
            scantext.setTextColor(getResources().getColor(R.color.backcolor));
        }
        if(i==2){
            personlimage.setBackgroundResource(R.drawable.person);
            personltext.setTextColor(getResources().getColor(R.color.backcolor));
        }


    }
}
