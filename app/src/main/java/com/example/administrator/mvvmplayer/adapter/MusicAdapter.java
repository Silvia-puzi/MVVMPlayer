package com.example.administrator.mvvmplayer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.bean.Mp3Info;
import com.example.administrator.mvvmplayer.databinding.ItemMusicBinding;
import com.example.administrator.mvvmplayer.view.activity.MusicDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.viewModule
 *  @文件名:   MusicAdapter
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/29 23:23
 *  @描述：    TODO
 */
public class MusicAdapter extends RecyclerView.Adapter{
    private static final String TAG = "MusicAdapter";
    private Activity mMusicActivity;
    private ArrayList<Mp3Info> mFileList;

    public MusicAdapter(Activity musicActivity, ArrayList<Mp3Info> fileList) {
        this.mMusicActivity = musicActivity;
        this.mFileList = fileList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMusicBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(mMusicActivity), R.layout.item_music,parent,false);
        return new MusicHolder(binding);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ItemMusicBinding binding = ((MusicHolder) holder).getBinding();

        final Mp3Info mp3Info = mFileList.get(position);
        binding.itemName.setText(mp3Info.getTitle());
        binding.itemAlbum.setText(mp3Info.getArtist()+" | "+mp3Info.getAlbum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = position;
                Intent intent = new Intent(mMusicActivity,MusicDetailActivity.class);
                intent.putExtra("path",mp3Info.getUrl());
                intent.putExtra("lrc",mp3Info.getTitle());
                intent.putExtra("position",pos);
                intent.putExtra("list", (Serializable) mFileList);
                Log.e(TAG, "onClick: "+mFileList );
                mMusicActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }
}
