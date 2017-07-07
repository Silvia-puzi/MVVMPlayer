package com.example.administrator.mvvmplayer.viewModule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.databinding.ActivitySplashBinding;
import com.example.administrator.mvvmplayer.view.activity.MainActivity;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.viewModule
 *  @文件名:   SplashViewModule
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/3 11:27
 *  @描述：    TODO
 */
public class SplashViewModule implements Animation.AnimationListener {
    private static final String TAG = "SplashViewModule";

    private ActivitySplashBinding mSplashBinding;
    private AppCompatActivity mActivity;

    public SplashViewModule(ActivitySplashBinding splashBinding, AppCompatActivity activity) {
        mSplashBinding = splashBinding;
        mActivity = activity;
        init();
    }

    private void init() {
        Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.anim_splash);
        animation.setAnimationListener(this);
        mSplashBinding.splashImg.setAnimation(animation);
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intent = new Intent(mActivity, MainActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
