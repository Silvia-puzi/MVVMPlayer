package com.example.administrator.mvvmplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.bean.Mp3Info;

import java.util.ArrayList;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.adapter
 *  @文件名:   PopAdapter
 *  @创建者:   Administrator
 *  @创建时间:  2017/7/5 23:32
 *  @描述：    TODO
 */
public class PopAdapter extends BaseAdapter {
    private static final String TAG = "PopAdapter";
    private ArrayList<Mp3Info> audioItems = new ArrayList<>();
    public void updatePlayList(ArrayList<Mp3Info> audioItems){
        this.audioItems.clear();
        this.audioItems = audioItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return audioItems.size();
    }

    @Override
    public Mp3Info getItem(int i) {
        return audioItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.pop_item_title.setText(audioItems.get(position).getTitle());
        holder.pop_item_artist.setText(audioItems.get(position).getArtist());
        return convertView;
    }

    class ViewHolder{
        TextView pop_item_title;
        TextView pop_item_artist;
        public ViewHolder(View view) {
            pop_item_title = (TextView) view.findViewById(R.id.pop_item_name);
            pop_item_artist = (TextView) view.findViewById(R.id.pop_item_artist);
        }
    }
}
