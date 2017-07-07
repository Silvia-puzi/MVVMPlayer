package com.example.administrator.mvvmplayer.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.databinding.MusicFragment1Binding;
import com.example.administrator.mvvmplayer.viewModule.Music1ViewModule;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.view.fragment
 *  @文件名:   MusicFragment1
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/29 0:53
 *  @描述：    TODO
 */
public class MusicFragment1 extends Fragment {
    private static final String TAG = "MusicFragment1";
    private MusicFragment1Binding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.music_fragment1, container, false);
        new Music1ViewModule(mBinding,this);
        return mBinding.getRoot();
    }
}
