package com.example.administrator.mvvmplayer.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.databinding.ActivitySplashBinding;
import com.example.administrator.mvvmplayer.viewModule.SplashViewModule;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.view.activity
 *  @文件名:   SplashActivity
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/3 11:22
 *  @描述：    TODO
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    private ActivitySplashBinding mSplashBinding;
    private SplashViewModule mSplashViewModule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        //创建viewModule
        mSplashViewModule = new SplashViewModule(mSplashBinding,this);
    }
}
