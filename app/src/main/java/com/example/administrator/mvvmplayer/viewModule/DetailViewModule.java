package com.example.administrator.mvvmplayer.viewModule;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.adapter.PopAdapter;
import com.example.administrator.mvvmplayer.bean.Mp3Info;
import com.example.administrator.mvvmplayer.bean.MusicBean;
import com.example.administrator.mvvmplayer.databinding.ActivityMusicBinding;
import com.example.administrator.mvvmplayer.service.MusicService;
import com.example.administrator.mvvmplayer.view.LyricView;
import com.example.administrator.mvvmplayer.view.PlayListPop;
import com.example.administrator.mvvmplayer.view.activity.MusicDetailActivity;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import okhttp3.Call;
import okhttp3.Response;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.viewModule
 *  @文件名:   Music1ViewModule
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/29 23:14
 *  @描述：    TODO
 */
public class DetailViewModule implements View.OnClickListener, LyricView.PlayListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener {
    private static final String TAG = "Music1ViewModule";
    private ActivityMusicBinding mBinding;
    private MusicDetailActivity mDetailActivity;
    private RecyclerView mMusic1Recy;
    private List<Mp3Info> mMp3Infos = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private ImageView mDetailFavor;
    private ImageView mDetailLast;
    private ImageView mDetailPlay;
    private ImageView mDetailNext;
    private ImageView mDetailMore;
    private ImageView mDetailBack;
    private ImageView mDetailShare;
    private LyricView mDetailLrc;
    
    private MusicService.AudioBinder mAudioBinder;
    //TODO
    public static Mp3Info audioItem;
    private int duration;
    private static final int MSG_UPDATE_PROGRESS = 0;
    private static final int MSG_PLAY_LYRIC = 1;
    private String mMp3path;
    private SeekBar mDetailSeekbar;
    private ImageView mDetailMode;
    private TextView mDetailName;
    private TextView mDetailArtist;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_UPDATE_PROGRESS:
                    startUpdateProgress();
                    break;
                case MSG_PLAY_LYRIC:
                    startPlayLyric();
                    break;
            }

        }
    };
    private String mTitle;


    public DetailViewModule(ActivityMusicBinding binding, MusicDetailActivity detailActivity) {
        this.mBinding = binding;
        this.mDetailActivity = detailActivity;

        initView();
        initData();
        initListener();
    }

    /*
    接受方法
     */
    @Subscribe
    public void onEventMainThread(Mp3Info audioItem){
        Toast.makeText(mDetailActivity,audioItem.getTitle(),Toast.LENGTH_SHORT).show();
        this.audioItem = audioItem;
        mTitle = audioItem.getTitle();
        //更新播放状态图标
        updatePlayStateBtn();
        //更新歌曲名和歌手名
        mDetailName.setText(audioItem.getTitle());
        mDetailArtist.setText(audioItem.getArtist());
        //获取总时长
        duration = mAudioBinder.getDuration();
        //设置进度条最大值
        mDetailSeekbar.setMax(duration);
        //更新播放进度
        startUpdateProgress();
        //更新模式图标
        updatePlayModeBtn();
        //设置歌词文件
        Log.e(TAG, "onEventMainThread: "+audioItem.getTitle() );
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, audioItem.getTitle() + ".lrc");
        if (!file.exists()){
            getMusicFile();
        }else{
            mDetailLrc.setLyricFile(audioItem.getTitle());
            //歌词播放
            startPlayLyric();
        }

    }

    private void startPlayLyric() {
        //获取当前progress
        int progress = mAudioBinder.getProgress();
        //设置到歌词中进行播放
        mDetailLrc.playLyric(progress,duration);
        //定时获取进度
        handler.sendEmptyMessage(MSG_PLAY_LYRIC);
    }

    private void startUpdateProgress() {
        //获取当前进度
        int progress = mAudioBinder.getProgress();
        //设置进度
        updateProgress(progress);
        //定时获取
        handler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS,500);
    }

    private void updateProgress(int progress) {
        //更新进度
        mDetailSeekbar.setProgress(progress);
    }


    public void initData() {
        //注册eventbus
        EventBus.getDefault().register(this);
        Intent intent1 = new Intent(mDetailActivity.getIntent());
        intent1.setClass(mDetailActivity.getBaseContext(),MusicService.class);
        AudioConnection connection = new AudioConnection();
        mDetailActivity.bindService(intent1,connection, Service.BIND_AUTO_CREATE);
        mDetailActivity.startService(intent1);
//        getMusicFile();
    }

    private void getMusicFile() {
        //读取歌词歌曲文件
        Intent intent = mDetailActivity.getIntent();
        mMp3path = intent.getStringExtra("path");
        final String path = intent.getStringExtra("lrc");
        String url = "http://geci.me/api/lyric/"+mTitle;
        try {
            URL url1 = new URL(url);
            Log.e(TAG, "initData: "+url1.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        OkGo.get(url)     // 请求方式和请求url
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        // s 即为所需要的结果
                        String result = s.toString();
                        Gson gson = new Gson();
                        MusicBean musicBean = gson.fromJson(result, MusicBean.class);
//        没有歌词信息
                        Log.e(TAG, "onSuccess: " +DetailViewModule.audioItem.getTitle());
                        if (!musicBean.getResult().toString().equals("[]")){
                            final String lrc = musicBean.getResult().get(0).getLrc();
                            OkGo.get(lrc)//
                                    .tag(this)//
                                    .execute(new FileCallback(DetailViewModule.audioItem.getTitle()+".lrc") {  //文件下载时，需要指定下载的文件目录和文件名
                                        @Override
                                        public void onSuccess(File file, Call call, Response response) {
                                            Log.e(TAG, "onSuccessaaaaaaaaaaa: "+lrc+".."+file.getName() );
                                            // file 即为文件数据，文件保存在指定目录
//                                            ArrayList<LyricBean> lyricBeen = LyricParser.parseLyric(file);
                                            mDetailLrc.setLyricFile(file.getName());
                                            //歌词播放
                                            startPlayLyric();
                                        }


                                        @Override
                                        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                            //这里回调下载进度(该回调在主线程,可以直接更新ui)
                                        }

                                        @Override
                                        public void onError(Call call, Response response, Exception e) {
                                            super.onError(call, response, e);
                                            e.printStackTrace();
                                            Log.e(TAG, "onError1: "+e.getMessage() );
                                        }
                                    });
                        }else{
                            mDetailLrc.setLyricFile(DetailViewModule.audioItem.getTitle());
                            //歌词播放
                            startPlayLyric();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e(TAG, "onError2: "+e.getMessage() );
                    }
                });
    }

    private void initListener() {
        mDetailBack.setOnClickListener(this);
        mDetailShare.setOnClickListener(this);
        mDetailFavor.setOnClickListener(this);
        mDetailLast.setOnClickListener(this);
        mDetailNext.setOnClickListener(this);
        mDetailPlay.setOnClickListener(this);
        mDetailMore.setOnClickListener(this);
        mDetailLrc.setPlayListener(this);
        mDetailMode.setOnClickListener(this);
        mDetailSeekbar.setOnSeekBarChangeListener(this);
    }

    private void initView() {
        mDetailName = mBinding.detailName;
        mDetailArtist = mBinding.detailArtist;
        mDetailMode = mBinding.detailMode;
        mDetailBack = mBinding.detailBack;
        mDetailShare = mBinding.detailShare;
        mDetailFavor = mBinding.detailFavor;
        mDetailLast = mBinding.detailLast;
        mDetailPlay = mBinding.detailPlay;
        mDetailNext = mBinding.detailNext;
        mDetailMore = mBinding.detailMore;
        mDetailLrc = mBinding.detailLrc;
        mDetailSeekbar = mBinding.detailSeekbar;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (!b)return;
        //跳转到指定位置播放
        mAudioBinder.seekTo(i);
        //更新进度显示
        updateProgress(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.detail_play:
                updatePlayState();
                break;
            case R.id.detail_mode:
                Log.e(TAG, "onClick: "+"click ");
                //切换播放模式
                updatePlayMode();
                break;
            case R.id.detail_last:
                mAudioBinder.playPre();
                break;
            case R.id.detail_next:
                mAudioBinder.playNext();
                break;
            case R.id.detail_more:
                showPlayList();
                break;

        }
 }

    private void showPlayList() {
        //显示播放列表
        PopAdapter adapter = new PopAdapter();
        adapter.updatePlayList(mAudioBinder.getPlayList());
        PlayListPop pop = new PlayListPop(mDetailActivity.getBaseContext(),adapter,this);
        pop.showAtLocation(mDetailName, Gravity.CENTER,0,0);
    }

    private void updatePlayMode() {
        //获取当前播放模式 全部循环-》单曲循环-》随机循环
        int play_mode = mAudioBinder.getPlayMode();
        Log.e(TAG, "updatePlayMode: "+play_mode );
        //切换
        switch (play_mode){
            case MusicService.MODE_ALL:
                mAudioBinder.setPlayMode(MusicService.MODE_SINGLE);
                break;
            case MusicService.MODE_SINGLE:
                mAudioBinder.setPlayMode(MusicService.MODE_RANDOM);
                break;
            case  MusicService.MODE_RANDOM:
                mAudioBinder.setPlayMode(MusicService.MODE_ALL);
                break;
        }
        //改变图标
        updatePlayModeBtn();
    }

    private void updatePlayModeBtn() {
        //获取当前播放模式 全部循环-》单曲循环-》随机循环
        int play_mode = mAudioBinder.getPlayMode();
        //切换图标
        switch (play_mode){
            case MusicService.MODE_ALL:
                mDetailMode.setImageResource(R.drawable.playmode_single);
                break;
            case MusicService.MODE_SINGLE:
                mDetailMode.setImageResource(R.drawable.playmode_random);
                break;
            case  MusicService.MODE_RANDOM:
                mDetailMode.setImageResource(R.drawable.playmode_all);
                break;
        }
    }

    /*
    切换播放状态
     */
    private void updatePlayState() {
        //获取状态
        boolean isPlaying = mAudioBinder.isPlaying();
        //根据当前状态切换
        if (isPlaying){
            //暂停
            mAudioBinder.pause();
        }else{
            mAudioBinder.start();
        }
        //切换状态图标
        updatePlayStateBtn();
    }

    private void updatePlayStateBtn() {
        //获取当前播放状态
        boolean playing = mAudioBinder.isPlaying();
        if (playing){
            mDetailPlay.setImageResource(R.drawable.stop);
            //开始更新
            handler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS,500);
        }else{
            mDetailPlay.setImageResource(R.drawable.play);
            //停止更新进度
            handler.removeMessages(MSG_UPDATE_PROGRESS);
        }
        //修改图标
    }

    @Override
    public void onPlay(int progress) {
        mAudioBinder.seekTo(progress);
        updateProgress(progress);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mAudioBinder.playPosition(i);
    }


    public class AudioConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mAudioBinder = (MusicService.AudioBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }





}
