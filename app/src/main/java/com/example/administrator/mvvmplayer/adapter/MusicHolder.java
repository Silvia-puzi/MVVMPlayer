package com.example.administrator.mvvmplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.mvvmplayer.databinding.ItemMusicBinding;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.adapter
 *  @文件名:   MusicHolder
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/29 23:31
 *  @描述：    TODO
 */
public class MusicHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "MusicHolder";
    private ItemMusicBinding mItemMusicBinding;

    public MusicHolder(View itemView) {
        super(itemView);
    }

    public MusicHolder(ItemMusicBinding binding) {
        super(binding.getRoot());
        this.mItemMusicBinding = binding;
    }

    public ItemMusicBinding getBinding(){
        return mItemMusicBinding;
    }

    public void setBinding(ItemMusicBinding binding){
        this.mItemMusicBinding = binding;
    }
}
