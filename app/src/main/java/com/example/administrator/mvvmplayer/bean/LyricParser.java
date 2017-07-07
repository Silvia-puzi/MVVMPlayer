package com.example.administrator.mvvmplayer.bean;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.ContentValues.TAG;

/**
 * Created by ThinkPad on 2017/1/11.
 */

public class LyricParser {
    private String min;
    private static BufferedReader bfr;

    /**
     * 根据歌词file解析歌词文件
     *
     * @param file
     * @return
     */
    public static ArrayList<LyricBean> parseLyric(File file) {
        BufferedReader bfr = null;
        //创建集合
        ArrayList<LyricBean> lyricBeens = new ArrayList<>();
        StringBuffer lyr = new StringBuffer();
        //判断是否为空
        Log.e(TAG, "initData: " + file.exists() + file.getAbsolutePath());
        if (file == null || !file.exists()) {
            Log.e(TAG, "initData: " + "aaa");
            lyricBeens.add(new LyricBean(0, "歌词加载错误"));
        } else {
            //解析
            try {
                //读取一行  可以指定编码方式
                bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                //读取一行
                String line = bfr.readLine();
                //当前行有内容
                while (line != null) {
                    Log.e(TAG, "initData: " + line);
                    lyr.append(line);
                    line = bfr.readLine();
                    //解析 添加到集合中
                    ArrayList<LyricBean> lyrics = parseLine(line);

                    lyricBeens.addAll(lyrics);
                    //读取下一行
                    line = bfr.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bfr != null) {
                    try {
                        bfr.close();
                        bfr = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Log.e(TAG, "parseLyric: "+lyr );
        //排序
        Collections.sort(lyricBeens, new Comparator<LyricBean>() {
            @Override
            public int compare(LyricBean o1, LyricBean o2) {
                int o1T = o1.getStartTime();
                int o2T = o2.getStartTime();
                return o1T-o2T;
            }
        });
        return lyricBeens;
    }

    //解析一行歌词  [00:48.08 [02:27.15 经过你快乐时少烦恼多
    private static ArrayList<LyricBean> parseLine(String line) {
        //创建集合
        ArrayList<LyricBean> lyricBeens = new ArrayList<>();
        //解析
        String[] arr = line.split("]");
        //获取歌词内容
        String content = arr[arr.length - 1];
        //解析时间
        for (int i = 0; i < arr.length - 1; i++) {
            //获取时间
            int startTime = parseTime(arr[i]);
            //添加到集合中
            lyricBeens.add(new LyricBean(startTime, content));
        }
        //返回
        return lyricBeens;
    }

    //解析时间  [00:48.08
    private static int parseTime(String s) {
        String st = s.substring(1);
        String[] arr = st.split(":");
        String min;
        String sec;
        String hour;
        int time=0;
        if(arr.length==2) {
            min =  arr[0];
            sec = arr[1];
            time = (int) (Integer.parseInt(min)*60*1000+Float.parseFloat(sec)*1000);
        }else if(arr.length ==3){
            hour = arr[0];
            min = arr[1];
            sec = arr[2];
            time = (int) (Integer.parseInt(hour)*60*60*1000+Integer.parseInt(min)*60*1000+Float.parseFloat(sec)*1000);
        }

        return time;
    }
}
