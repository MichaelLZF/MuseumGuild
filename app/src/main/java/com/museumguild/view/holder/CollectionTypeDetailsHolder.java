package com.museumguild.view.holder;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.museumguild.entity.collection.Collection;
import com.museumguild.entity.collection.CollectionAttr;
import com.museumguild.manage.ServerApiManager;
import com.museumguild.utils.Log;
import com.museumguild.utils.Util;
import com.museumguild.utils.share.PopShare;
import com.museumguild.view.adapter.AbstractAdapter;
import com.museumguild.view.adapter.AbstractViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.museumguild.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/8/13.
 */
public class CollectionTypeDetailsHolder extends BaseHolder implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{
    private Gallery mGallery;
    private GallaryAdapter mGallaryAdapter;

    private TextView tvDes;

    private TextView tvStartTime;
    private TextView tvEndTime;
    private SeekBar sbSeekBar;

    private LinearLayout lyAudioPlay;
    private ImageView ivPlayList;
    private ImageView ivPlayPre;
    private ImageView ivPlayStatus;
    private ImageView ivPlayNext;
    private ImageView ivPlayAudio;
    private ImageView ivPlayAudioTimes;
    private LinearLayout ivPlaySwitchAudioLan;

    private Collection mCollection;
    private List<CollectionAttr> picAttrs;
    private CollectionAttr curAudioAttr;
    private List<CollectionAttr> audioAttrs;


    public MediaPlayer mediaPlayer;
    private Timer mTimer= null;

    private static final String TAG = "CollectionTypeDetailsHolder";
    private PopShare popShare;

    public CollectionTypeDetailsHolder(View rootView, Object value) {
        super(rootView, value);

        picAttrs = new ArrayList<CollectionAttr>();
        audioAttrs = new ArrayList<CollectionAttr>();
        if(null != mCollection)
        {
            List<CollectionAttr> attrs = mCollection.getAttlist();
            if(null != attrs && attrs.size() > 0)
            {
                for(CollectionAttr attr : attrs)
                {
                    if(attr.isPicType())
                    {
                        picAttrs.add(attr);
                    }

                    if(attr.isAudioType())
                    {
                        audioAttrs.add(attr);
                    }
                }
            }

            tvDes.setText(mCollection.getJianjie());
        }
        mGallaryAdapter.notifyData(picAttrs);
        lyAudioPlay.setVisibility(audioAttrs.size() > 0? View.VISIBLE : View.GONE);
        if(audioAttrs.size() > 0)
        {
            playAudio(audioAttrs.get(0));
        }
    }

    @Override
    protected void initData(Object value) {

    }

    @Override
    protected void initComp(Object value) {
        mCollection = (Collection)value;
        mGallery = (Gallery)findViewById(R.id.details_gallery_gl);
        tvDes = (TextView)findViewById(R.id.collection_type_des_tv);

        tvStartTime = (TextView)findViewById(R.id.details_starttime_tv);
        tvEndTime = (TextView)findViewById(R.id.details_endtime_tv);
        sbSeekBar = (SeekBar) findViewById(R.id.details_seek_sb);
        sbSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());

        lyAudioPlay = (LinearLayout)findViewById(R.id.audio_play_ly);
        ivPlayList = (ImageView)findViewById(R.id.details_play_list_iv);
        ivPlayList.setOnClickListener(this);
        ivPlayPre = (ImageView)findViewById(R.id.details_play_pre_iv);
        ivPlayPre.setOnClickListener(this);
        ivPlayStatus = (ImageView)findViewById(R.id.details_play_status_iv);
        ivPlayStatus.setOnClickListener(this);
        ivPlayNext = (ImageView)findViewById(R.id.details_play_next_iv);
        ivPlayNext.setOnClickListener(this);
        ivPlayAudio = (ImageView)findViewById(R.id.details_play_audio_iv);
        ivPlayAudio.setOnClickListener(this);
        ivPlayAudioTimes = (ImageView)findViewById(R.id.details_play_times_iv);
        ivPlayAudioTimes.setOnClickListener(this);
        ivPlaySwitchAudioLan = (LinearLayout)findViewById(R.id.details_play_audio_lang_switch_iv);
        ivPlaySwitchAudioLan.setOnClickListener(this);
        mGallaryAdapter = new GallaryAdapter();
        mGallery.setAdapter(mGallaryAdapter);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.details_play_list_iv:
                break;
            case R.id.details_play_pre_iv:
                break;
            case R.id.details_play_status_iv:
                if(null == mediaPlayer)
                {
                    return;
                }
                if(mediaPlayer.isPlaying())
                {
                    ivPlayStatus.setSelected(true);
                    mediaPlayer.pause();
                }
                else
                {
                    ivPlayStatus.setSelected(false);
                    mediaPlayer.start();
                }
                break;
            case R.id.details_play_next_iv:
                break;
            case R.id.details_play_audio_iv:
                ivPlayAudio.setSelected(!ivPlayAudio.isSelected());
                if(ivPlayAudio.isSelected())
                {
                    ivPlayAudio.getBackground().setAlpha(100);
                    CloseVolume();
                }
                else
                {
                    ivPlayAudio.getBackground().setAlpha(255);
                    OpenVolume();
                }
                break;
            case R.id.txtShareSinaWB:
//                Util.shareSinaWeiboByActivity(getContext(), this);
                break;
            case R.id.txtShareQQ:
                Log.m(TAG,"share qq onclick");
                Util.shareQQ(getContext(),"藏品", mCollection.getName(), ServerApiManager.getCollectionShareUrl(mCollection.id),
                        null != picAttrs.get(0) ? Util.dealWithServerPicPath(picAttrs.get(0).getUrl()):null);
                break;
            case R.id.txtShareWX:
                Log.m(TAG,"share wx onclick");
                Util.shareWX(getContext(),"藏品", mCollection.getName(), ServerApiManager.getCollectionShareUrl(mCollection.id),
                        null != picAttrs.get(0) ? Util.dealWithServerPicPath(picAttrs.get(0).getUrl()):null);
                break;
            case R.id.txtShareFriend:
                Log.m(TAG,"share wx friend quan onclick");
                //朋友圈将title显示在内容的地方，title传空，
                Util.shareWXFriend(getContext(),"藏品", mCollection.getName(), ServerApiManager.getCollectionShareUrl(mCollection.id),
                        null != picAttrs.get(0) ? Util.dealWithServerPicPath(picAttrs.get(0).getUrl()):null);
                break;
            case R.id.txtShareCopy:
               // Util.shareCopy(getContext());
                break;
            case R.id.details_play_times_iv:
                switchPlayAudioTimes();
                break;
            case R.id.details_play_audio_lang_switch_iv:
                audioLanSwitch();
                break;

        }
    }

    public void shareBtnOnclick(){
        if (popShare == null) {
            popShare = new PopShare(getContext());
        }
        popShare.showAtLocation(rootView, this);
    }

    private class GallaryAdapter extends AbstractAdapter
    {
        public GallaryAdapter() {
        }

        @Override
        protected View createView(Context context) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_details_gallery_layout, null);
        }

        @Override
        protected AbstractViewHolder createViewHolder(View view) {
            return new GallaryViewHolder(view);
        }

        private class GallaryViewHolder extends AbstractViewHolder
        {
            private ImageView ivPic;

            public GallaryViewHolder(View view) {
                super(view);
            }

            @Override
            public void initView(View view) {
                ivPic = (ImageView)view.findViewById(R.id.pic_iv);
            }

            @Override
            public void initValue(int positon) {
                String url = picAttrs.get(positon).getUrl();
                ImageLoader.getInstance().displayImage(Util.dealWithServerPicPath(url), ivPic,
                        getRectOptions(R.drawable.collection_type_bg));
            }
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        post(new Runnable() {
            @Override
            public void run() {
                tvEndTime.setText(getTimeStr(mp.getDuration()));
            }
        });
        mp.start();
        ivPlayStatus.setSelected(false);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(ivPlayAudioTimes.isSelected())
        {
            mp.start();
        }
        else
        {
            ivPlayStatus.setSelected(true);
        }
    }

    private String getTimeStr(int seconds)
    {
        int secs = seconds / 1000;
        int s = secs % 60;

        int mins = secs / 60;
        int m = mins % 60;

        int h = mins / 60;

        String ss = s < 10 ? ("0"+s) : s +"";
        String mm = m < 10 ? ("0" + m) : m + "";
        String hh = h < 10 ? ("0" + h) : h + "";
        if(h <= 0)
        {
            return mm + ":" + ss;
        }
        else
        {
            return hh + ":" + mm + ":" + ss;
        }
    }

    Handler handleProgress = new Handler() {
                public void handleMessage(Message msg) {
                    if (mediaPlayer == null) {
                        return;
                    }
                       int position = mediaPlayer.getCurrentPosition();
                        int duration = mediaPlayer.getDuration();

                        if (duration > 0) {
                            tvStartTime.setText(getTimeStr(position));
                                long pos = sbSeekBar.getMax() * position / duration;
                                sbSeekBar.setProgress((int) pos);
                            }
                    };
            };


    private PopupWindow popupWindow;
    private void audioLanSwitch()
    {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.collection_type_switch_lan_layout,null);
         popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setWidth(getContext().getResources().getDimensionPixelSize(R.dimen.collection_type_details_popwidth));
        popupWindow.setHeight(getContext().getResources().getDimensionPixelSize(R.dimen.collection_type_details_popheight) * audioAttrs.size() + audioAttrs.size());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getContext().getResources().getDrawable(
                R.color.white));
        ListView audioLanLv = (ListView)contentView.findViewById(R.id.audio_lang_switch_lv);
        audioLanLv.setFooterDividersEnabled(false);
        AudioLanAdapter audioLanAdapter = new AudioLanAdapter();
        if(audioAttrs.size() > 0)
        {
            audioLanLv.setAdapter(audioLanAdapter);
            audioLanAdapter.notifyData(audioAttrs);
        }
        View popRl = findViewById(R.id.pop_rl1);
        View popIV = findViewById(R.id.pop_iv);
        int [] a = new int[2];
        popIV.getLocationOnScreen(a);
        popupWindow.showAsDropDown(popRl, a[0]-popupWindow.getWidth() / 2, -(popRl.getHeight() + popupWindow.getHeight()));
    }

    private void switchAudio(CollectionAttr audioAttr)
    {
        if(audioAttr == curAudioAttr)
        {
            return;
        }
        stop();
        playAudio(audioAttr);
    }

    private void switchPlayAudioTimes()
    {
        ivPlayAudioTimes.setSelected(!ivPlayAudioTimes.isSelected());
    }

    private void playAudio(CollectionAttr audioAttr)
    {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            String sAudioUrl = audioAttr.getUrl();
            curAudioAttr = audioAttr;
            mediaPlayer.setDataSource(Util.dealWithServerPicPath(sAudioUrl));
            mediaPlayer.prepareAsync();//prepare之后自动播放
            mTimer=new Timer();
            TimerTask mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if(mediaPlayer==null)
                        return;
                    if (mediaPlayer.isPlaying() && sbSeekBar.isPressed() == false) {
                        handleProgress.sendEmptyMessage(0);
                    }
                }
            };
            mTimer.schedule(mTimerTask, 0, 1000);
        } catch (Exception e) {
                e.printStackTrace();
            }

    }

    private int nLeftVolume;
    private int nRightVolume;
    public void CloseVolume(){
        AudioManager audioManager=(AudioManager)getContext().getSystemService(Service.AUDIO_SERVICE);
        nLeftVolume = nRightVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(0, 0);
    }


    public void OpenVolume(){
        AudioManager audioManager=(AudioManager)getContext().getSystemService(Service.AUDIO_SERVICE);
        mediaPlayer.setVolume(1, 1);
    }

    public void pause()
    {
        mediaPlayer.pause();
    }

    private boolean isPaused = false;
    public void onPause()
    {
        if(null != mediaPlayer && mediaPlayer.isPlaying())
        {
            isPaused = true;
            mediaPlayer.pause();
        }
    }
    public void onResume()
    {
        if(null != mediaPlayer && isPaused)
        {
            isPaused = false;
            mediaPlayer.start();
        }
    }

    public void stop()
    {
        if (mediaPlayer != null) {
            mTimer.cancel();
            mTimer = null;
           mediaPlayer.stop();
           mediaPlayer.release();
           mediaPlayer = null;
        }

        sbSeekBar.setProgress(0);
        tvStartTime.setText("00:00");
        tvEndTime.setText("00:00");
    }



    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            if (null != mediaPlayer) {
                this.progress = progress * mediaPlayer.getDuration()
                        / seekBar.getMax();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            if (null != mediaPlayer) {
                mediaPlayer.seekTo(progress);
            }
        }
    }

    private class AudioLanAdapter extends  AbstractAdapter
    {
        @Override
        protected AbstractViewHolder createViewHolder(View view) {
            return new AudioLanViewHolder(view);
        }

        @Override
        protected View createView(Context context) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_collection_anudio_lan_layout,null);
        }

        private class AudioLanViewHolder extends  AbstractViewHolder
        {
            private TextView tvContent;
            private ImageView ivContent;
            AudioLanViewHolder(View view)
            {
                super(view);
            }

            @Override
            public void initValue(final int positon) {
                String str = audioAttrs.get(positon).getName();
                if(null != str && !"".equals(str))
                {
                    String[] str1 = str.split("-");
                    if(str1.length >= 2)
                    {
                        tvContent.setText(str1[1]);
                    }
                    else
                    {
                        tvContent.setText(str);
                    }
                }
                tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(popupWindow != null)
                        popupWindow.dismiss();
                        switchAudio(audioAttrs.get(positon));
//                        for(int i = 0; i < audioAttrs.size(); i++)
//                        {
//                            if(audioAttrs.get(i) == curAudioAttr)
//                            {
//                                tvContent.setSelected(true);
//                            }
//                            else
//                            {
//                                tvContent.setSelected(false);
//                            }
//                        }
                    }
                });

                tvContent.setSelected(curAudioAttr == audioAttrs.get(positon) ? true : false);
                if(positon == audioAttrs.size() - 1)
                {
                    ivContent.setVisibility(View.GONE);
                }
                else
                {
                    ivContent.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void initView(View view) {
                tvContent = (TextView)view.findViewById(R.id.contentTv);
                ivContent = (ImageView)view.findViewById(R.id.contentiv);
            }
        }
    }
}


