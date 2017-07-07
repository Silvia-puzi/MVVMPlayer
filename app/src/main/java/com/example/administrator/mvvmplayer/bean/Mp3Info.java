package com.example.administrator.mvvmplayer.bean;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;
import java.util.ArrayList;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.bean
 *  @文件名:   Mp3Info
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/30 0:30
 *  @描述：    TODO
 */
public class Mp3Info implements Serializable{
    private static final String TAG = "Mp3Info";

    private Long id;
    private String title;
    private Long duration;
    private String artist;
    private Long size;
    private String url;
    private String album;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public static String getTAG() {
        return TAG;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static ArrayList<Mp3Info> getAudioItems(Cursor cursor){
        ArrayList<Mp3Info> audioItems = new ArrayList<>();
        if ((cursor == null || cursor.getCount() == 0)) {
            return audioItems;
        }
        cursor.moveToPosition(-1);
        while(cursor.moveToNext()){
            Mp3Info audioItem = getAudioItem(cursor);
            audioItems.add(audioItem);
        }
        return audioItems;
    }

    public static Mp3Info getAudioItem(Cursor cursor){
        Mp3Info audioItem = new Mp3Info();
        //判断cursor是否为空
        if (cursor == null || cursor.getCount() == 0) return audioItem;
        //解析cursor
        audioItem.url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        audioItem.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        audioItem.title = audioItem.title.substring(0,audioItem.title.lastIndexOf("."));
        audioItem.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        //返回audioitem
        return audioItem;
    }
}
