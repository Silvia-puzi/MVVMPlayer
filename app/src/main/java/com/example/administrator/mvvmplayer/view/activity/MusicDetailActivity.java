package com.example.administrator.mvvmplayer.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.databinding.ActivityMusicBinding;
import com.example.administrator.mvvmplayer.viewModule.DetailViewModule;

import de.greenrobot.event.EventBus;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.adapter
 *  @文件名:   MusicDetailActivity
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/30 21:28
 *  @描述：    TODO
 */
public class MusicDetailActivity extends AppCompatActivity{
    private static final String TAG = "MusicDetailActivity";
    private ActivityMusicBinding mBinding;
    private DetailViewModule mDetailViewModule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_music);
        mDetailViewModule = new DetailViewModule(mBinding, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册eventbus
        EventBus.getDefault().unregister(mDetailViewModule);
        //移除handler发送的消息
        mDetailViewModule.handler.removeCallbacksAndMessages(null);
    }
}
