package com.museumguild.view.activities.find;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.museumguild.bean.LineSpot;
import com.museumguild.entity.collection.Collection;
import com.museumguild.entity.collection.find.LockInform;
import com.museumguild.http.ResultCallback;
import com.museumguild.ibeacon.iBeaconClass;
import com.museumguild.ibeacon.iBeaconClass.iBeacon;
import com.museumguild.ls.widgets.map.MapWidget;
import com.museumguild.ls.widgets.map.events.MapTouchedEvent;
import com.museumguild.ls.widgets.map.events.ObjectTouchEvent;
import com.museumguild.ls.widgets.map.interfaces.Layer;
import com.museumguild.ls.widgets.map.interfaces.MapEventsListener;
import com.museumguild.ls.widgets.map.interfaces.OnMapTouchListener;
import com.museumguild.ls.widgets.map.model.MapObject;
import com.museumguild.ls.widgets.map.utils.PivotFactory;
import com.museumguild.ls.widgets.map.utils.PivotFactory.PivotPosition;
import com.museumguild.manage.ServerApiManager;
import com.museumguild.utils.FileUtil;
import com.museumguild.utils.Util;
import com.museumguild.view.activities.BaseActivity;


import com.museumguild.utils.Log;

import com.museumguild.view.holder.find.FindFailDialog;
import com.museumguild.view.holder.find.FindFunctionDialog;
import com.museumguild.view.holder.find.FindSuccessDialog;
import com.museumguild.view.model.MapObjectContainer;
import com.museumguild.view.model.MapObjectModel;
import com.museumguild.view.view.TextPopup;
import com.museumguild.R;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FindCollectionDetailsActivity extends BaseActivity implements OnMapTouchListener, MapEventsListener, FindSuccessDialog.OnConfirmClickedListener {
    public static final int REQUEST_CODE_ROUTE = 0x1;
    private static final String MAP_LI_SHI_WEN_WU_CEN_LIE = "西域历史的记忆";// 新疆历史文物陈列
    private static final String MAP_LSWW_FOLDER = "map_lswwcl";
    private static final String MAP_GU_DAI_FU_SHI = "新疆古代服饰的记忆";// 古代服饰的记忆展厅
    private static final String MAP_GDFS_FOLDER = "map_gdfs";
    private static final String MAP_GU_DAI_GAN_SHI = "古尸";// 新疆古代干尸陈列
    private static final String MAP_GDGS_FOLDER = "map_gdgs";
    private static final String MAP_MING_ZU_FENG_QING = "民族风情展厅";
    private static final String MAP_MZFQ_FOLDER = "map_mzfq";
    private static final String MAP_SHU_HUA_LIN = "书画临展厅";
    private static final String MAP_SHL_FLODER = "map_shuhua_lzt";
    private static final String MAP_WEN_WU_LIN = "文物临展厅";
    private static final String MAP_WWL_FOLDER = "map_wenwu_lzt";
    private static final String CHOOSED_ID = "999";
    private ArrayList<Collection> mapWenWuSpotsList = new ArrayList<Collection>();
    private ArrayList<Collection> mapFuShiSpotsList = new ArrayList<Collection>();
    private ArrayList<Collection> mapGuShiSpotsList = new ArrayList<Collection>();

    private MapWidget mapWidget;
    public static final int MAP_ID_1 = 1;
    public static final int MAP_ID_2 = 2;
    public static final int MAP_ID_3 = 3;
    public static final int MAP_ID_4 = 4;
    public static final int MAP_ID_5 = 5;
    public static final int MAP_ID_6 = 6;
    private static final int LAYER_TYPE_ONE = 1;
    private static final int LAYER_TYPE_TWO = 2;
    private static final int LAYER_TYPE_THREE = 3;
    private static final int MODEL_ID_BIXUGE = 4;
    private static final int MODE_ID_XIOAGUANGHAN = 5;
    private static final int MODE_ID_HUAQIAODAMEN = 6;
    private MapObjectContainer modelsTypeOne = new MapObjectContainer();
    private MapObjectContainer modelsTypeTwo;
    private MapObjectContainer modelsTypeThree;
    private TextPopup mapObjectInfoPopup;
    private int pinHeight;
    private int nextObjectId;
    private int nextModelId;
    private int arrowHeight;

    private Toolbar toolbar;

    private PopupWindow mPopWindow = null;

    private String nowRoom = MAP_LI_SHI_WEN_WU_CEN_LIE;
    private boolean isFirst;

    //蓝牙定位使用
    private boolean mScanning;
    private BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD = 5000;
    Runnable runnable;

    private Collection collection;
    private Collection found;
    private int position;

    //当前手机api版本
    private int currentApiVersion = Build.VERSION.SDK_INT;
    private Display display;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        Log.m(TAG, "device display Width = " + screenWidth);
        Log.m(TAG, "device display Height = " + screenHeight);
        //去掉logo
        removeMapLogo();

        //开启蓝牙
        initBluetooth();
        mapObjectInfoPopup = new TextPopup(this,(FrameLayout) findViewById(R.id.rootLayout));
        isFirst = true;
        initMap(savedInstanceState, nowRoom);
        startSearch();

    }


    @Override
    public void onConfirmClicked() {
        finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private  void startSearch() {
        if (currentApiVersion < 18) {
            Toast.makeText(this, R.string.api_level_is_too_low, Toast.LENGTH_SHORT).show();
        }
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "对不起，该功能需要蓝牙的支持", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        runnable = new Runnable() {
            @Override
            public void run() {
                iBeacon beacon = getFirstBeacon();
                Message msg = new Message();
                if (beacon != null && beacon.rssi > -86) {
                    Collection col = getCollectionByBeacon(beacon);
                    if (col != null && col.name.equals(collection.name))
                        msg.what = 3;          //成功找到文物
                    else if (col != null){
                        msg.what = 2;         //错误的文物
                        found = col;
                    }
                    mHandler.sendMessage(msg);
                } else {
                    msg.what = 4;          //失败
                    mHandler.sendMessage(msg);
                }
                mLeDevices.clear();      //清空ibeacon，重新扫描
                mHandler.postDelayed(this, 3000);
            }
        };
        mHandler.postDelayed(runnable, 3000);
    }

    private Collection getCollectionByBeacon (iBeacon ibeacon) {
        Collection coll = null;
        //boolean flag = false;
        if (collection.room.equals(MAP_LI_SHI_WEN_WU_CEN_LIE)) {
            for (int i = 0; i < mapWenWuSpotsList.size(); i++) {
                Collection temp = mapWenWuSpotsList.get(i);
                if (temp.ibeacon_x == ibeacon.major && temp.ibeacon_y == ibeacon.minor) {
                    coll = temp;
                    break;
                }
            }
        }
        if (collection.room.equals(MAP_GU_DAI_FU_SHI)) {
            for (int i = 0; i < mapFuShiSpotsList.size(); i++) {
                Collection temp = mapFuShiSpotsList.get(i);
                if (temp.ibeacon_x == ibeacon.major && temp.ibeacon_y == ibeacon.minor) {
                    //flag = true;
                    coll = temp;
                    break;
                }
            }
        }
        if (collection.room.equals(MAP_GU_DAI_GAN_SHI)) {
            for (int i = 0; i < mapGuShiSpotsList.size(); i++) {
                Collection temp = mapGuShiSpotsList.get(i);
                if (temp.ibeacon_x == ibeacon.major && temp.ibeacon_y == ibeacon.minor) {
                    //flag = true;
                    coll = temp;
                    break;
                }
            }
        }
        return coll;
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*;*/
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
        if (mBluetoothAdapter != null)
            mBluetoothAdapter.stopLeScan(mLeScanCallback);

    }


    //保存对象到本地
    private void saveObject(Set<String> set, String filename) {
        LockInform lockInform = new LockInform(set);
        boolean flag = FileUtil.saveObject(this, filename, lockInform);
        /*if (flag) {
            show("save success!");
        } else {
            show("save fail!");
        }*/
    }

    //获取本地对象
    private  Set<String> getObject(String filename) {
        Object obj = FileUtil.getObject(this, filename);
        LockInform lock = null;
        if (obj instanceof LockInform) {
            lock = (LockInform) obj;

            // show("" + map.get("wqx"));
        }
        if (lock != null) {
            //show("get success, " + lock);
            return lock.getLocked();
        } else {
            //show("get null");
            return (Set<String>) null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected int initContentViewId() {
        return R.layout.activity_find_collection_details;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        collection = (Collection)getIntent().getSerializableExtra("collection");
        position = getIntent().getIntExtra("positoin", -1);
        toolbar = (Toolbar) findViewById(R.id.toolbar_normal);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((TextView) findViewById(R.id.toolbar_title)).setText(collection.room);


        initListeners();
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

    private void initListeners() {
        //右侧控件
        //findViewById(R.id.iv_message).setOnClickListener(this);
        findViewById(R.id.iv_zoom_big).setOnClickListener(this);
        findViewById(R.id.iv_zoom_small).setOnClickListener(this);
        findViewById(R.id.iv_locate).setOnClickListener(this);

    }

    @Override
    protected void initData() {

        // Toast.makeText(this, collection.name + " " + collection.room , Toast.LENGTH_SHORT).show();
//        if (collection.room.equals(MAP_LI_SHI_WEN_WU_CEN_LIE)) {
//            nowRoom = MAP_LI_SHI_WEN_WU_CEN_LIE;
//        }
//        if (collection.room.equals(MAP_GU_DAI_FU_SHI)) {
//            nowRoom = MAP_GU_DAI_FU_SHI;
//        }
//        if (collection.room.equals(MAP_GU_DAI_GAN_SHI)) {
//            nowRoom = MAP_GU_DAI_GAN_SHI;
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_zoom_big://地图放大
                mapWidget.zoomIn();
                break;
            case R.id.iv_zoom_small://地图缩小
                mapWidget.zoomOut();
                break;
            case R.id.iv_locate://定位到入口
                //TODO 538 738
                //mapWidget.zoomIn();
                //mapWidget.scrollMapTo(new Point(538,738));
                if (currentApiVersion < 18) {
                    Toast.makeText(this, R.string.api_level_is_too_low, Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
                    return;
                }
                //scanLeDevice(true);
                //Toast.makeText(this, R.string.wait_for_locate, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
            //"637", "321", "651", "340"
			/*mapWidget.zoomIn();
			mapWidget.scrollMapTo(new Point((289+466)/2,(367+486)/2));*/
        }
    }

//    private void showHotSpotInMap(LineSpot value) {
//        //加载地图
//        if (!TextUtils.isEmpty(value.room)) {
//            if(!nowRoom.equals(value.room)){
//                initMapAndJumpToSpot(null, value.room ,value);
//            }else{
//                initChoosedSpotModels(CHOOSED_ID,value);
//            }
//        }
//    }

    private void initMapAndJumpToSpot(Bundle savedInstanceState, String mapName, LineSpot lineSpot) {
        // In order to display the map on the screen you will need
        // to initialize widget and place it into layout.
        String toolbarName = MAP_LI_SHI_WEN_WU_CEN_LIE;
        if (TextUtils.isEmpty(mapName)) {
            return;
        }
        int mapId = MAP_ID_1;
        String rootMapFolder = MAP_LSWW_FOLDER;
        int initialZoomLevel ;
        if(screenHeight > 1900){
            initialZoomLevel = 12;
        }else {
            initialZoomLevel = 11;
        }
        if(mapName.equals(MAP_LI_SHI_WEN_WU_CEN_LIE)){
            mapId = MAP_ID_1;
            rootMapFolder = MAP_LSWW_FOLDER;
            toolbarName = getString(R.string.map_lsww);
        }else if(mapName.equals(MAP_GU_DAI_FU_SHI)){
            mapId = MAP_ID_2;
            rootMapFolder = MAP_GDFS_FOLDER;
            toolbarName = getString(R.string.map_gdfs);
            initialZoomLevel ++;
        }else if(mapName.equals(MAP_GU_DAI_GAN_SHI)){
            mapId = MAP_ID_3;
            rootMapFolder = MAP_GDGS_FOLDER;
            toolbarName = getString(R.string.map_gdgs);
            initialZoomLevel ++;
        }else if(mapName.equals(MAP_MING_ZU_FENG_QING)){
            mapId = MAP_ID_4;
            rootMapFolder = MAP_MZFQ_FOLDER;
            toolbarName = getString(R.string.map_mzfq);
        }else if(mapName.equals(MAP_SHU_HUA_LIN)) {
            mapId = MAP_ID_5;
            rootMapFolder = MAP_SHL_FLODER;
            toolbarName = getString(R.string.map_shl);
            initialZoomLevel ++;
        }else if(mapName.equals(MAP_WEN_WU_LIN)){
            mapId = MAP_ID_6;
            rootMapFolder = MAP_WWL_FOLDER;
            toolbarName = getString(R.string.map_wwl);
            initialZoomLevel ++;
        }
        Log.m(TAG , "initialZoomLevel:"+initialZoomLevel);
        mapWidget = new MapWidget(savedInstanceState, this, rootMapFolder, initialZoomLevel);
//        String path = "";
//        mapWidget = new MapWidget(savedInstanceState, this, "slt", initialZoomLevel);
        mapWidget.setId(mapId);

        FrameLayout rootView = (FrameLayout) findViewById(R.id.rootLayout);
        rootView.removeAllViews();
        rootView.addView(mapWidget);

        mapWidget.getConfig().setFlingEnabled(true);
        mapWidget.getConfig().setPinchZoomEnabled(true);
        mapWidget.setMaxZoomLevel(13);
        mapWidget.setMinZoomLevel(10);
        mapWidget.setUseSoftwareZoom(true);
        mapWidget.setZoomButtonsVisible(false);
        mapWidget.setBackgroundColor(Color.WHITE);
        mapWidget.createLayer(LAYER_TYPE_ONE);
        mapWidget.createLayer(LAYER_TYPE_TWO);
        // In order to receive MapObject touch events we need to set listener
        // In order to receive pre and post zoom events we need to set MapEventsListener
        mapWidget.setOnMapTouchListener(this);
        mapWidget.addMapEventsListener(this);
        //加载地图上所有的热点
//        initAllMapHotSpots(mapName , lineSpot);
        nowRoom = mapName;
    }

    private void initMap(Bundle savedInstanceState, String mapName) {
        initMapAndJumpToSpot(savedInstanceState,mapName,null);
    }

    private void initAllMapHotSpots(String mapName, LineSpot lineSpot) {
        if (!isFirst && mapName == null) {
            isFirst = false ;
            return;
        }
        //初始化文物展厅所有热点
        if (mapName.equals(MAP_LI_SHI_WEN_WU_CEN_LIE) ) {
            if( mapWenWuSpotsList.size() > 0){
                initLayerModels(mapWenWuSpotsList);
                initChoosedSpotModels(CHOOSED_ID, lineSpot);
            }else{
                getCollectionByMapName(mapName, lineSpot);
            }
        }else if(mapName.equals(MAP_GU_DAI_FU_SHI)){
            if( mapFuShiSpotsList.size() > 0){
                initLayerModels(mapFuShiSpotsList);
                initChoosedSpotModels(CHOOSED_ID, lineSpot);
            }else{
                getCollectionByMapName(mapName, lineSpot);
            }
        }else if(mapName.equals(MAP_GU_DAI_GAN_SHI)){
            if( mapGuShiSpotsList.size() > 0){
                initLayerModels(mapGuShiSpotsList);
                initChoosedSpotModels(CHOOSED_ID, lineSpot);
            }else{
                getCollectionByMapName(mapName, lineSpot);
            }
        }else {
            initChoosedSpotModels(CHOOSED_ID,lineSpot);
        }

    }

    private void getCollectionByMapName(final String mapName, final LineSpot lineSpot) {
        ServerApiManager.getIns().getListCollection("", "", "", "","200", "1", new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.e(TAG, "getCollectionByMapName onError");
            }

            @Override
            public void onResponse(String response) {
                boolean isSuccess = Util.parseResultIsRequstOk(response);
                if(isSuccess){
                    try {
                        JSONObject jo = new JSONObject(response);
                        JSONArray ja = jo.getJSONArray("collectionlist");
                        mapWenWuSpotsList.clear();
                        mapFuShiSpotsList.clear();
                        mapGuShiSpotsList.clear();
                        for (int i = 0; i < ja.length(); i++) {
                            Collection collection = new Gson().fromJson(ja.getString(i),Collection.class);
                            if(!TextUtils.isEmpty(collection.room)){
                                if(collection.room.equals(MAP_LI_SHI_WEN_WU_CEN_LIE)){
                                    mapWenWuSpotsList.add(collection);
                                }else if(collection.room.equals(MAP_GU_DAI_FU_SHI)){
                                    mapFuShiSpotsList.add(collection);
                                }else if(collection.room.equals(MAP_GU_DAI_GAN_SHI)){
                                    mapGuShiSpotsList.add(collection);
                                }
                            }
                        }
                        if(mapName.equals(MAP_LI_SHI_WEN_WU_CEN_LIE)){
                            initLayerModels(mapWenWuSpotsList);
                            initChoosedSpotModels(CHOOSED_ID, lineSpot);
                        }else if(mapName.equals(MAP_GU_DAI_FU_SHI)){
                            initLayerModels(mapFuShiSpotsList);
                            initChoosedSpotModels(CHOOSED_ID, lineSpot);
                        }else if(mapName.equals(MAP_GU_DAI_GAN_SHI)){
                            initLayerModels(mapGuShiSpotsList);
                            initChoosedSpotModels(CHOOSED_ID, lineSpot);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    //初始化热点
    private void initChoosedSpotModels(final String objectId, final LineSpot lineSpot) {
        if(null == lineSpot ||TextUtils.isEmpty(lineSpot.line_android_x) || TextUtils.isEmpty(lineSpot.line_android_y)){
            return;
        }
        final int centerX = Integer.valueOf(lineSpot.line_android_x);
        final int centerY = Integer.valueOf(lineSpot.line_android_y);
        final MapObjectModel objectModel = new MapObjectModel(objectId, centerX, centerY, lineSpot.title, R.drawable.ic_hotspot, 0);
        Layer layer1 = mapWidget.getLayerById(LAYER_TYPE_ONE);
        addNotScalableMapObject(objectModel, layer1, CHOOSED_ID);
        modelsTypeOne.addObject(objectModel);
        if (mapWidget.getZoomLevel() < 12){
            mapWidget.zoomIn();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapWidget.scrollMapTo(new Point(centerX, centerY));
                showLocationsPopup(objectModel,500);
            }
        }, 1000);
    }

    private void initLayerModels(List<Collection> collections){
        modelsTypeOne.removeAll();
        for (int i = 0; i <collections.size(); i++) {
            Collection collection = collections.get(i);
            if ( collection.android_x != 0 && collection.android_y != 0) {
                MapObjectModel objectModel = new MapObjectModel(collection.id, collection.android_x, collection.android_y, collection, R.drawable.ic_hotspot, 0);
                Layer layer1 = mapWidget.getLayerById(LAYER_TYPE_ONE);
                addNotScalableMapObject(objectModel, layer1 ,collection.id);
                modelsTypeOne.addObject(objectModel);
            }
        }
    }

    private void addNotScalableMapObject(MapObjectModel objectModel, Layer layer, String objectId) {
        if (objectModel.getScala() == 0) {
            addNotScalableMapObject(objectModel.getX(), objectModel.getY(), layer, objectId, false);
        } else if (objectModel.getScala() == 1) {
            addNotScalableMapObject(objectModel.getX(), objectModel.getY(), layer, objectId, true);
        }
    }

    private void addNotScalableMapObject(int x, int y, Layer layer, String objectId, boolean isScalable) {
        // Getting the drawable of the map object
        Drawable drawable = getResources().getDrawable(R.drawable.ic_hotspot);
        pinHeight = drawable.getIntrinsicHeight();

        // Creating the map object
        MapObject object1 = new MapObject(objectId,
                drawable, new Point(x, y), PivotFactory.createPivotPoint(
                drawable, PivotPosition.PIVOT_BOTTOM_CENTER), true, isScalable);
        // Adding object to layer
        layer.addMapObject(object1);
    }

    private void showLocationsPopup(int x, int y, String text ,final Collection collection1) {
        FrameLayout mapLayout = (FrameLayout) findViewById(R.id.rootLayout);

        if (mapObjectInfoPopup != null && mapObjectInfoPopup.isVisible()) {
            mapObjectInfoPopup.hide();
        }

//		((TextPopup) mapObjectInfoPopup)
//				.setIcon((BitmapDrawable) getResources().getDrawable(
//						R.drawable.map_popup_arrow));
        ((TextPopup) mapObjectInfoPopup).setText(text);

        mapObjectInfoPopup.setOnClickListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mapObjectInfoPopup != null) {
                        if (collection1.name.equals(collection.name)) {
                            //解锁当前文物
                            Set<String> set = getObject("object.dat");
                            if (set == null) {
                                set = new HashSet<String>();
                            }
                            //Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
                            set.add(collection.name);
                            //Toast.makeText(this, map.size() + "", Toast.LENGTH_SHORT).show();
                            saveObject(set, "object.dat");


                            FindSuccessDialog dialog = new FindSuccessDialog(FindCollectionDetailsActivity.this, collection1.name, collection1.jianjie);
                            dialog.setOnConfirmListener(FindCollectionDetailsActivity.this);
                            dialog.show();
                        } else {
                            FindFailDialog dialog = new FindFailDialog(FindCollectionDetailsActivity.this, collection1.name, collection1.jianjie);
                            dialog.show();
                        }
                    }
                }

                return false;
            }
        });

        ((TextPopup) mapObjectInfoPopup).show(mapLayout, x, y);
    }

    /***
     * Transforms coordinate in map coordinate system to screen coordinate
     * system
     *
     * @param mapCoord - X in map coordinate in pixels.
     * @return X coordinate in screen coordinates. You can use this value to
     * display any object on the screen.
     */
    private int xToScreenCoords(int mapCoord) {
        return (int) (mapCoord * mapWidget.getScale() - mapWidget.getScrollX());
    }

    private int yToScreenCoords(int mapCoord) {
        return (int) (mapCoord * mapWidget.getScale() - mapWidget.getScrollY());
    }

    @Override
    public void onPostZoomIn() {
        // this scrollMapTo x,y
        //mapWidget.scrollMapTo(new Point(centerX,centerY));
    }

    @Override
    public void onPostZoomOut() {
    }

    @Override
    public void onPreZoomIn() {
        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }
    }

    @Override
    public void onPreZoomOut() {
        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }
    }

    @Override
    public void onTouch(MapWidget v, MapTouchedEvent event) {
        // Get touched object events from the MapTouchEvent
        ArrayList<ObjectTouchEvent> touchedObjs = event.getTouchedObjectIds();

        if (touchedObjs.size() > 0) {
            ObjectTouchEvent objectTouchEvent = event.getTouchedObjectIds().get(0);
            long layerId = objectTouchEvent.getLayerId();
            String objectId = (String) objectTouchEvent.getObjectId();
            if(objectId == null ){
                return;
            }
            MapObjectModel objectModel = modelsTypeOne.getObjectById(objectId);

            if (objectModel != null) {
                //showLocationsPopup(objectModel,0);
            } else {
                // This is a case when we want to show popup where the user has
                // touched.
                //showLocationsPopup(xInScreenCoords, yInScreenCoords,"Shows where user touched", null);
            }

        } else {
            if (mapObjectInfoPopup != null) {
                mapObjectInfoPopup.hide();
            }
        }
    }

    private void showLocationsPopup(final MapObjectModel objectModel, int delayTime) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float density = getResources().getDisplayMetrics().density;
                int imgHeight = (int) (pinHeight / density / 2);
                int x = xToScreenCoords(objectModel.getX());
                int y = yToScreenCoords(objectModel.getY()) - imgHeight;
                showLocationsPopup(x, y, objectModel.getCaption(),objectModel.getCollection());
            }
        },delayTime);

    }

    private void removeMapLogo() {
        Class<?> c = null;
        try {
            c = Class.forName("com.museumguild.ls.widgets.map.utils.Resources");
            Object obj = c.newInstance();
            Field field = c.getDeclaredField("LOGO");
            field.setAccessible(true);
            field.set(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //蓝牙消息处理
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:     //定位成功
                    /*iBeacon ibeacon = mLeDevices.get(0);
                    if (ibeacon != null) {
						/*string.append("first beacon").append("").append(" ").append("major = ").append(beacon.major)
								.append("minor = ").append(beacon.minor).append("\n");
                        boolean flag = false;
                        Collection collection = new Collection();
                        for (int i = 0; i < mapWenWuSpotsList.size(); i++) {
                            Collection temp = mapWenWuSpotsList.get(i);
                            if (temp.android_x == ibeacon.major && temp.android_y == ibeacon.minor) {
                                flag = true;
                                collection = temp;
                                break;
                            }
                        }
                        if (flag == false) {
                            for (int i = 0; i < mapFuShiSpotsList.size(); i++) {
                                Collection temp = mapFuShiSpotsList.get(i);
                                if (temp.android_x == ibeacon.major && temp.android_y == ibeacon.minor) {
                                    flag = true;
                                    collection = temp;
                                    break;
                                }
                            }
                        }
                        if (flag == false) {
                            for (int i = 0; i < mapGuShiSpotsList.size(); i++) {
                                Collection temp = mapGuShiSpotsList.get(i);
                                if (temp.android_x == ibeacon.major && temp.android_y == ibeacon.minor) {
                                    flag = true;
                                    collection = temp;
                                    break;
                                }
                            }
                        }

                        if (flag == false) {
                            Toast.makeText(getApplicationContext(), R.string.can_not_find_device, Toast.LENGTH_SHORT).show();
                            break;
                        }

                        Intent intent = new Intent(FindCollectionDetailsActivity.this, CollectionTypeDetailsActivity.class);
                        intent.putExtra("collection", collection);
                        startActivity(intent);


                    } else {
                        Toast.makeText(getApplicationContext(), R.string.can_not_find_device, Toast.LENGTH_SHORT).show();
                    }*/

                    //Toast.makeText(getApplicationContext(), "定位成功", Toast.LENGTH_SHORT).show();
                    break;
                case 1:   //定位失败
                    //text.setText(R.string.can_not_find_device);
                    Toast.makeText(getApplicationContext(), R.string.can_not_find_device, Toast.LENGTH_SHORT).show();
                    break;
                case 2:

                    float density = getResources().getDisplayMetrics().density;
                    int imgHeight = (int) (pinHeight / density / 2);
                    int x = xToScreenCoords(found.android_x);
                    int y = yToScreenCoords(found.android_y) - imgHeight;
                    showLocationsPopup(x, y, found.name,found);
                    break;
                case 3:
                    density = getResources().getDisplayMetrics().density;
                    imgHeight = (int) (pinHeight / density / 2);
                    x = xToScreenCoords(collection.android_x);
                    y = yToScreenCoords(collection.android_y) - imgHeight;
                    showLocationsPopup(x, y, collection.name,collection);
                    //Toast.makeText(getApplicationContext(),"成功", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    //Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initLeScanCallback()
    {
        if (currentApiVersion < 18) {   //判断当前Api是否大于18才能开启蓝牙
            return;
        }
        if(null == mLeScanCallback)
        {
            mLeScanCallback =
                    new BluetoothAdapter.LeScanCallback() {

                        @Override
                        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                            final iBeacon ibeacon = iBeaconClass.fromScanData(device,rssi,scanRecord);
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

    //获取最近的信号
    public iBeacon getFirstBeacon() {
        if (mLeDevices.size() != 0) {
            return mLeDevices.get(0);
        } else {
            return null;
        }
    }
}

