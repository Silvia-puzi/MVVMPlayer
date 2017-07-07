package com.example.administrator.mvvmplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.bean.Mp3Info;
import com.example.administrator.mvvmplayer.view.activity.MainActivity;
import com.example.administrator.mvvmplayer.view.activity.MusicDetailActivity;

import java.util.ArrayList;
import java.util.Random;

import de.greenrobot.event.EventBus;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.service
 *  @文件名:   MusicService
 *  @创建者:   Administrator
 *  @创建时间:  2017/7/1 15:13
 *  @描述：    TODO
 */
public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private MediaPlayer mMediaPlayer;
    private ArrayList<Mp3Info> audioItems;
    private AudioBinder binder;
    public static final int MODE_ALL = 0;
    public static final int MODE_SINGLE = 1;
    public static final int MODE_RANDOM = 2;

    private static final int FROM_CONTENT = 7;
    private static final int FROM_PRE = 8;
    private static final int FROM_NEXT = 9;
    private static final int FROM_PLAY=10;
    private static int play_mode;
    private SharedPreferences sp;
    private String mMp3Url;
//    private int mPos;
    private SharedPreferences mSp;
    private int position = -2;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new AudioBinder();

        mSp = getSharedPreferences("config", MODE_PRIVATE);
        play_mode = mSp.getInt("play_mode", 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //判断获取进入service的方式
        int from = intent.getIntExtra("from", -1);
        switch (from){
            case FROM_CONTENT:
                binder.notifyUpdateUI();
                break;
            case FROM_PRE:
                binder.playPre();
                break;
            case FROM_NEXT:
                binder.playNext();
                break;
            case FROM_PLAY:
                boolean playing = binder.isPlaying();
                if (playing){
                    binder.pause();
                }else{
                    binder.start();
                }


                break;
            default:
                //TODO
                int mPos = intent.getIntExtra("position",-1);
                if (mPos != position){
                    position = mPos;
                    audioItems = (ArrayList<Mp3Info>) intent.getSerializableExtra("list");
                    binder.playItem();
                }else{
                    //播放歌曲是当前歌曲
                    binder.notifyUpdateUI();
                }
                break;
        }


        //START_STICKY service被杀死后悔重新启动，不传递原来的intent
        //START_NOT_STICKY 不重新启动
        //START_REDELIVER_INTENT 重新启动，传递原来的intent
        return START_NOT_STICKY;
    }

    public class AudioBinder extends Binder implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
        private NotificationManager mManager;
        private Notification.Builder mBuilder;
        private NotificationManager manager;

        /*
          歌曲播放
        */
        public void playItem(){
            //先释放再创建，避免多个mediaplayer
            if (mMediaPlayer != null){
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            mMediaPlayer = MediaPlayer.create(MusicService.this, Uri.parse(audioItems.get(position).getUrl()));
//            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
//            try {
//                mMediaPlayer.setDataSource(audioItems.get(mPos).getUrl());
//                mMediaPlayer.prepareAsync();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mMediaPlayer.start();
            //通知界面更新
            notifyUpdateUI();
            //显示通知
            showNotificaton();
        }

        //显示通知
        private void showNotificaton() {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0,getPersonalNotification());
        }

        //自定义通知
        private Notification getPersonalNotification() {
            Notification.Builder  builder = new Notification.Builder(MusicService.this);
            builder.setSmallIcon(R.drawable.play);
            builder.setTicker("正在播放"+audioItems.get(position).getTitle());
            builder.setWhen(System.currentTimeMillis());
            builder.setContentIntent(getPendingIntent());
            builder.setOngoing(true);//不能手动滑动消失
            builder.setContent(getRemoteViews());//自定义通知布局
            return builder.getNotification();
        }

        //自定义通知布局
        private RemoteViews getRemoteViews() {
            //远程view
            RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification);
            remoteViews.setTextViewText(R.id.notification_title,audioItems.get(position).getTitle());
            remoteViews.setTextViewText(R.id.notification_artist,audioItems.get(position).getArtist());
            remoteViews.setOnClickPendingIntent(R.id.notification_pre,getPrePendingIntent());
            remoteViews.setOnClickPendingIntent(R.id.notification_next,getNextPendingIntent());
            remoteViews.setOnClickPendingIntent(R.id.notification_play,getPlayPendingIntent());
            return remoteViews;
        }

        private PendingIntent getPlayPendingIntent() {
            Intent intent = new Intent(MusicService.this,MusicService.class);
            intent.putExtra("from",FROM_PLAY);
            PendingIntent pendingIntent  = PendingIntent.getService(MusicService.this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            return pendingIntent;
        }

        //通知栏主体点击事件
        private PendingIntent getPendingIntent() {
            Intent intentA = new Intent(MusicService.this, MainActivity.class);
            Intent intentB = new Intent(MusicService.this,MusicDetailActivity.class);
            intentB.putExtra("from",FROM_CONTENT);
            Intent[] intents = {intentA,intentB};
//            intentA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent  = PendingIntent.getActivities(MusicService.this,0,intents,PendingIntent.FLAG_UPDATE_CURRENT);
            return pendingIntent;
        }
        //通知栏下一曲点击事件
        private PendingIntent getNextPendingIntent() {
            Intent intent = new Intent(MusicService.this,MusicService.class);
            intent.putExtra("from",FROM_NEXT);
            PendingIntent pendingIntent  = PendingIntent.getService(MusicService.this,2,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            return pendingIntent;
        }

        //通知栏上一曲点击事件
        private PendingIntent getPrePendingIntent() {
            Intent intent = new Intent(MusicService.this,MusicService.class);
            intent.putExtra("from",FROM_PRE);
            PendingIntent pendingIntent  = PendingIntent.getService(MusicService.this,2,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            return pendingIntent;
        }

        private void notifyUpdateUI() {
            //发送
            EventBus.getDefault().post(audioItems.get(position));
        }

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //自动播放下一曲
            autoPlayNext();
        }

        private void autoPlayNext() {
            switch (play_mode){
                case MODE_ALL:
//                    if (mPos == audioItems.size() - 1){
//                        mPos = 0;
//                    }else{
//                        mPos ++;
//                    }
                    position = (position + 1) % audioItems.size();
                    break;
                case MODE_SINGLE:
                    break;
                case MODE_RANDOM:
                    position = new Random().nextInt(audioItems.size());
                    break;
            }
            //播放
            playItem();
        }

        /*
        获取当前播放状态
         */
        public boolean isPlaying() {
            return mMediaPlayer.isPlaying();
        }

        public void pause() {
            mMediaPlayer.pause();
        }

        public void start() {
            mMediaPlayer.start();
        }

        public int getDuration() {
            return mMediaPlayer.getDuration();
        }

        public int getProgress() {
            return mMediaPlayer.getCurrentPosition();
        }

        public void seekTo(int i) {
            mMediaPlayer.seekTo(i);
        }

        public int getPlayMode() {
            return play_mode;
        }

        public void setPlayMode(int play_mode) {
            MusicService.play_mode = play_mode;
            mSp.edit().putInt("play_mode",play_mode).commit();
        }

        public void playPre() {
            switch (play_mode){
                case MODE_RANDOM:
                    position = new Random().nextInt(audioItems.size());
                    break;
                default:
                    if (position == 0){
                        position = audioItems.size() - 1;
                    }else{
                        position = position --;
                    }
                    break;
            }
            playItem();
        }

        public void playNext() {
            switch (play_mode){
                case MODE_RANDOM:
                    position = new Random().nextInt(audioItems.size());
                    break;
                default:
                    position = (position + 1)% audioItems.size();
                    break;
            }
            playItem();
        }

        public ArrayList<Mp3Info> getPlayList() {
            return audioItems;
        }

        public void playPosition(int i) {
            MusicService.this.position = i;
            playItem();
        }
    }

}

