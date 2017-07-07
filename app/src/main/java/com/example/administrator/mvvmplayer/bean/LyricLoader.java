package com.example.administrator.mvvmplayer.bean;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import static android.content.ContentValues.TAG;

/**
 * Created by ThinkPad on 2017/1/11.
 */

public class LyricLoader {
    //定义歌词文件夹
    private static final File dir = new File(Environment.getExternalStorageDirectory(),"/Download/");

    //歌词文件加载
    public static File loadLyricFile(String display_name){
        Log.e(TAG, "loadLyricFile: "+dir );
        return new File(dir,display_name+"");
    }
}
