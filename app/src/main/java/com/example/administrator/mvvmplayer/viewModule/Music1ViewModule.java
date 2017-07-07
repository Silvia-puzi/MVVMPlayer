package com.example.administrator.mvvmplayer.viewModule;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.administrator.mvvmplayer.adapter.MusicAdapter;
import com.example.administrator.mvvmplayer.bean.Mp3Info;
import com.example.administrator.mvvmplayer.databinding.MusicFragment1Binding;
import com.example.administrator.mvvmplayer.view.fragment.MusicFragment1;

import java.util.ArrayList;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.viewModule
 *  @文件名:   Music1ViewModule
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/29 23:14
 *  @描述：    TODO
 */
public class Music1ViewModule {
    private static final String TAG = "Music1ViewModule";
    private MusicFragment1Binding mBinding;
    private MusicFragment1 mMusicFragment1;
    private RecyclerView mMusic1Recy;
    private ArrayList<Mp3Info> mMp3Infos = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;

    public Music1ViewModule(MusicFragment1Binding binding, MusicFragment1 musicFragment1) {
        this.mBinding = binding;
        this.mMusicFragment1 = musicFragment1;
        initView();
    }

    private void initView() {
        mMusic1Recy = mBinding.music1Recy;
        mLayoutManager = new LinearLayoutManager(mMusicFragment1.getActivity());
        mMusic1Recy.setLayoutManager(mLayoutManager);
        //设为垂直布局
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        getMP3Infos();
        MusicAdapter musicAdapter = new MusicAdapter(mMusicFragment1.getActivity(), mMp3Infos);
        mMusic1Recy.setAdapter(musicAdapter);
    }

    private void getMP3Infos() {
        //读取歌曲文件
        Cursor cursor = mMusicFragment1.getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        for (int i = 0; i < cursor.getCount(); i++) {
            Mp3Info mp3Info = new Mp3Info();
            cursor.moveToNext();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));   //音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题
            Log.e(TAG, "initView: "+title );
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家
            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));//专辑名
            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));//时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));  //文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));              //文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐
            if (isMusic != 0) {     //只把音乐添加到集合当中
                mp3Info.setId(id);
                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
                mp3Info.setAlbum(album);
                mMp3Infos.add(mp3Info);
            }
        }
    }
}
