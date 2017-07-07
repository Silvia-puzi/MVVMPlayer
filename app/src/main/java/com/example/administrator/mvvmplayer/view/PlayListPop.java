package com.example.administrator.mvvmplayer.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.adapter.PopAdapter;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.view
 *  @文件名:   PlayListPop
 *  @创建者:   Administrator
 *  @创建时间:  2017/7/5 23:20
 *  @描述：    TODO
 */
public class PlayListPop extends PopupWindow {
    private static final String TAG = "PlayListPop";
    public PlayListPop(Context context, PopAdapter adapter, AdapterView.OnItemClickListener listener){
        //解析布局获取布局view
        View view = LayoutInflater.from(context).inflate(R.layout.pop,null,false);
        final ListView pop_list = (ListView) view.findViewById(R.id.pop_list);
        pop_list.setAdapter(adapter);
        //设置条目点击事件
        pop_list.setOnItemClickListener(listener);
        //添加view
        setContentView(view);
        //设置宽度和高度
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置焦点
        setFocusable(true);
        //设置背景，响应返回事件
        setBackgroundDrawable(new BitmapDrawable());
        //设置动画
        setAnimationStyle(R.style.pop);
        //设置触摸事件
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getY() < pop_list.getTop())dismiss();
                return false;
            }
        });
    }
}
