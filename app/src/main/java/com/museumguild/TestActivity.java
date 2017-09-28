package com.museumguild;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.museumguild.bean.BwgInfo;
import com.museumguild.bean.LineSpot;
import com.museumguild.bean.MyInfo;
import com.museumguild.http.ResultCallback;
import com.museumguild.ls.widgets.map.MapWidget;
import com.museumguild.ls.widgets.map.events.MapTouchedEvent;
import com.museumguild.ls.widgets.map.events.ObjectTouchEvent;
import com.museumguild.ls.widgets.map.interfaces.MapEventsListener;
import com.museumguild.ls.widgets.map.interfaces.OnMapTouchListener;
import com.museumguild.manage.LoginManager;
import com.museumguild.manage.ServerApiManager;
import com.museumguild.utils.Log;
import com.museumguild.utils.Util;
import com.museumguild.view.model.MapObjectContainer;
import com.museumguild.view.model.MapObjectModel;
import com.museumguild.view.view.CircleBitmapDisplayer;
import com.museumguild.view.view.TextPopup;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.okhttp.Request;

import java.util.ArrayList;


/**
 * Created by hasee on 2017/9/17.
 */

public class TestActivity extends Activity implements View.OnClickListener,OnMapTouchListener,MapEventsListener{
    private String mHeadPic;
    private String bwgid = "52a03da75cf1c83d015cf1c8d2080000";//此处给定值，测试结果
    private ImageView testresult;
    private FrameLayout rootLayout;
    private ImageView big;
    private ImageView small;
    private MapWidget mapWidget;
    private static final int LAYER_TYPE_ONE = 1;
    private static final int LAYER_TYPE_TWO = 2;
    private MapObjectContainer modelsTypeOne = new MapObjectContainer();
    private TextPopup mapObjectInfoPopup;

    private int screenWidth;
    private int screenHeight;
    private Display display;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testlayout);
//        testresult = (ImageView)findViewById(R.id.resultid);
        big = (ImageView)findViewById(R.id.iv_zoom_big);
        small = (ImageView)findViewById(R.id.iv_zoom_small);
//        testresult.se
        testresult.setBackgroundResource(R.drawable.adult);
        rootLayout = (FrameLayout)findViewById(R.id.rootLayout);
//        mapWidget = new MapWidget();
        rootLayout.addView(testresult);

        display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        big.setOnClickListener(this);
        small.setOnClickListener(this);
//        big.setOnClickListener(this);
//        initMap(savedInstanceState, bwgid);
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .build();
//        ImageLoader.getInstance().init(config);


//        showMyInfo();
    }
    private void showMyInfo(){
        ServerApiManager.getIns().getbwg(
                bwgid,
                new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        boolean isSuccess = Util.parseResultIsRequstOk(response);
                        if (isSuccess) {
                            BwgInfo bwgInfo = new Gson().fromJson(response,BwgInfo.class);
                            if (!TextUtils.isEmpty(bwgInfo.thumbnail)){
                                mHeadPic = Util.dealWithServerPicPath(bwgInfo.thumbnail);
                                Log.m("",mHeadPic);

//                                ImageLoader.getInstance().displayImage(mHeadPic,testresult);
                                DisplayImageOptions options = new DisplayImageOptions.Builder()
//                                        .cacheInMemory(true)
//                                        .cacheOnDisk(true)
                                        .displayer(new CircleBitmapDisplayer())
                                        .build();
                          ImageLoader.getInstance().displayImage(mHeadPic,testresult,options);

                            }
                        }
                    }
                });
    }
    private void initMap(Bundle savedInstanceState, String mapName) {
        initMapAndJumpToSpot(savedInstanceState,mapName,null);
    }
    private void initMapAndJumpToSpot(Bundle savedInstanceState, String mapName, LineSpot lineSpot) {
//        // In order to display the map on the screen you will need
//        // to initialize widget and place it into layout.
//        if (TextUtils.isEmpty(mapName)) {
//            return;
//        }
//        int initialZoomLevel ;
//        if(screenHeight > 1900){
//            initialZoomLevel = 12;
//        }else {
//            initialZoomLevel = 11;
//        }
//        if(mapName.equals(MAP_LI_SHI_WEN_WU_CEN_LIE)){
//            mapId = MAP_ID_1;
//            rootMapFolder = MAP_LSWW_FOLDER;
////            toolbarName = getString(R.string.map_lsww);
//        }else if(mapName.equals(MAP_GU_DAI_FU_SHI)){
//            mapId = MAP_ID_2;
//            rootMapFolder = MAP_GDFS_FOLDER;
////            toolbarName = getString(R.string.map_gdfs);
//            initialZoomLevel ++;
//        }else if(mapName.equals(MAP_GU_DAI_GAN_SHI)){
//            mapId = MAP_ID_3;
//            rootMapFolder = MAP_GDGS_FOLDER;
////            toolbarName = getString(R.string.map_gdgs);
//            initialZoomLevel ++;
//        }else if(mapName.equals(MAP_MING_ZU_FENG_QING)){
//            mapId = MAP_ID_4;
//            rootMapFolder = MAP_MZFQ_FOLDER;
////            toolbarName = getString(R.string.map_mzfq);
//        }else if(mapName.equals(MAP_SHU_HUA_LIN)) {
//            mapId = MAP_ID_5;
//            rootMapFolder = MAP_SHL_FLODER;
////            toolbarName = getString(R.string.map_shl);
//            initialZoomLevel ++;
//        }else if(mapName.equals(MAP_WEN_WU_LIN)){
//            mapId = MAP_ID_6;
//            rootMapFolder = MAP_WWL_FOLDER;
////            toolbarName = getString(R.string.map_wwl);
//            initialZoomLevel ++;
//        }
//        Log.m(TAG , "initialZoomLevel:"+initialZoomLevel);
//        mapWidget = new MapWidget(savedInstanceState, this, rootMapFolder, initialZoomLevel);
//        mapWidget.setId(mapId);
//
//        FrameLayout rootView = (FrameLayout) findViewById(R.id.rootLayout);
//        rootView.removeAllViews();
//        rootView.addView(mapWidget);
//
//        mapWidget.getConfig().setFlingEnabled(true);
//        mapWidget.getConfig().setPinchZoomEnabled(true);
//        mapWidget.setMaxZoomLevel(13);
//        mapWidget.setMinZoomLevel(10);
//        mapWidget.setUseSoftwareZoom(true);
//        mapWidget.setZoomButtonsVisible(false);
//        mapWidget.setBackgroundColor(Color.WHITE);
//        mapWidget.createLayer(LAYER_TYPE_ONE);
//        mapWidget.createLayer(LAYER_TYPE_TWO);
//        // In order to receive MapObject touch events we need to set listener
//        // In order to receive pre and post zoom events we need to set MapEventsListener
//        mapWidget.setOnMapTouchListener(this);
//        mapWidget.addMapEventsListener(this);
//        //加载地图上所有的热点
////        initAllMapHotSpots(mapName , lineSpot);
//        nowRoom = mapName;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_zoom_big:
                mapWidget.zoomIn();
                break;
            case R.id.iv_zoom_small:
                mapWidget.zoomOut();
                break;
        }
    }

    @Override
    public void onTouch(MapWidget v, MapTouchedEvent event) {
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
            }

        } else {
            if (mapObjectInfoPopup != null) {
                mapObjectInfoPopup.hide();
            }
        }
    }

    @Override
    public void onPreZoomIn() {
        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }
    }

    @Override
    public void onPostZoomIn() {

    }

    @Override
    public void onPreZoomOut() {
        if (mapObjectInfoPopup != null) {
            mapObjectInfoPopup.hide();
        }
    }

    @Override
    public void onPostZoomOut() {

    }




}
