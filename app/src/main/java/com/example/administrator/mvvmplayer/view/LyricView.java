package com.example.administrator.mvvmplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.mvvmplayer.R;
import com.example.administrator.mvvmplayer.bean.LyricBean;
import com.example.administrator.mvvmplayer.bean.LyricLoader;
import com.example.administrator.mvvmplayer.bean.LyricParser;

import java.io.File;
import java.util.ArrayList;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.view
 *  @文件名:   LyricView
 *  @创建者:   Administrator
 *  @创建时间:  2017/7/1 23:41
 *  @描述：    TODO
 */

public class LyricView extends View {

    private int halfWhite;
    private int green;
    private float smallSize;
    private float bigSize;
    private Paint paint;
    private int viewH;
    private int viewW;
    private ArrayList<LyricBean> lyricBeens;
    private int centerLine;
    private int lineHeight;
    private int duration;
    private int progress;
    private boolean handleLyric = true;
    private float downY;
    private int offsetY;
    private PlayListener playListener;

    /**
     * new
     *
     * @param context
     */
    public LyricView(Context context) {
        this(context, null);
    }

    /**
     * 布局
     *
     * @param context
     * @param attrs
     */
    public LyricView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * style
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        bigSize = getResources().getDimension(R.dimen.bigSize);
        smallSize = getResources().getDimension(R.dimen.smallSize);
        green = getResources().getColor(R.color.orange);
        halfWhite = getResources().getColor(R.color.halfwhite);

        //行高
        lineHeight = getResources().getDimensionPixelOffset(R.dimen.lineHeight);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);//x方向以中间确定位置  y方向没有变

        //创建歌词数据
        lyricBeens = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            lyricBeens.add(new LyricBean(2000*i,"第"+i+"行歌词"));
//        }
//        centerLine = 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawSingleLineA(canvas);
        if (lyricBeens.size() == 0) {
            drawSingleLineB(canvas);
        } else {
            drawMultipleLine(canvas);
        }
    }

    //绘制多行文本
    private void drawMultipleLine(Canvas canvas) {
        if(handleLyric) {
            //如果居中行为最后一行：
            int lineTime;
            if (centerLine == lyricBeens.size() - 1) {
                //行可用时间=duration-当前行开始时间
                lineTime = duration - lyricBeens.get(centerLine).getStartTime();
            } else {
                //行可用时间=下一行开始时间-当前行开始时间
                int nextT = lyricBeens.get(centerLine + 1).getStartTime();
                int curT = lyricBeens.get(centerLine).getStartTime();
                lineTime = nextT - curT;
            }
            //偏移时间=progress-开始时间
            int offsetTime = progress - lyricBeens.get(centerLine).getStartTime();
            //偏移百分比=偏移时间/行可用时间
            float offsetPercent = offsetTime / (float) lineTime;
            //偏移y=偏移百分比*行高
            offsetY = (int) (offsetPercent * lineHeight);
        }

        //找到居中行 x y
        String centerText = lyricBeens.get(centerLine).getContent();

        //求剧中行文本的宽度和高度
        Rect bounds = new Rect();
        paint.getTextBounds(centerText, 0, centerText.length(), bounds);
        int centerTextH = bounds.height();
        //        y=viewH/2+textH/2
        //最终y=居中y-偏移y
        int centerY = viewH / 2 + centerTextH / 2 - offsetY;

        for (int i = 0; i < lyricBeens.size(); i++) {
            if (i == centerLine) {
                paint.setTextSize(bigSize);
                paint.setColor(green);
            } else {
                paint.setTextSize(smallSize);
                paint.setColor(halfWhite);
            }
            String text = lyricBeens.get(i).getContent();
//        x=viewW/2
//        y=centerY+(i-centeLine)*lineHeight
            int curY = centerY + (i - centerLine) * lineHeight;
            //如果界面不显示，则不绘制
            if (curY < 0 || curY > viewH + lineHeight) continue;
            canvas.drawText(text, viewW / 2, curY, paint);
        }

    }

    //绘制单行文本
    private void drawSingleLineB(Canvas canvas) {
        //绘制单行居中文本
        String text = "正在加载歌词...";
        paint.setColor(green);
        paint.setTextSize(bigSize);
//        //计算text文本的宽度和高度
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
//        int textW = bounds.width();
        int textH = bounds.height();
////        x=viewW/2-textW/2
//        int x = viewW/2-textW/2;
//        y=viewH/2+textH/2
        int y = viewH / 2 + textH / 2;
        canvas.drawText(text, viewW / 2, y, paint);
    }

    private void drawSingleLineA(Canvas canvas) {
        //绘制单行居中文本
        String text = "正在加载歌词...";
        paint.setColor(green);
        paint.setTextSize(bigSize);
        //计算text文本的宽度和高度
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int textW = bounds.width();
        int textH = bounds.height();
//        x=viewW/2-textW/2
        int x = viewW / 2 - textW / 2;
//        y=viewH/2+textH/2
        int y = viewH / 2 + textH / 2;
        canvas.drawText(text, x, y, paint);
    }

    //layout-setFrame（保存left top bottom right信息）-sizeChanged-onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = w;
        viewH = h;
    }

    //播放歌词
    public void playLyric(int progress, int duration) {
        //如果手指按下  停止进度更新歌词
        if(!handleLyric) return;
        this.duration = duration;
        this.progress = progress;
//        居中行position
//                判断是否最后一行居中
//        progress>=最后一行开始时间
        if (progress >= lyricBeens.get(lyricBeens.size() - 1).getStartTime()) {
            centerLine = lyricBeens.size() - 1;
        } else {
//        遍历集合
//        progress>=当前行开始时间&&progress<下一行开始时间
            for (int i = 0; i < lyricBeens.size() - 1; i++) {
                int curT = lyricBeens.get(i).getStartTime();
                int nextT = lyricBeens.get(i + 1).getStartTime();
                if (progress >= curT && progress < nextT) {
                    centerLine = i;
                    break;
                }
            }
        }
        //重新按照最新的居中行绘制歌词
        invalidate();//ondraw
    }

    //设置歌词文件
    public void setLyricFile(String display_name) {
        File file = LyricLoader.loadLyricFile(display_name);
        ArrayList<LyricBean> lyrics = LyricParser.parseLyric(file);

        lyricBeens.clear();
        lyricBeens.addAll(lyrics);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手指按下时 停止通过进度更新歌词
                handleLyric  =false;
                //记录按下的y
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                //移动的y距离
                offsetY = (int) (downY-moveY);
                //是否需要重新计算居中行
                if(Math.abs(offsetY)>lineHeight){
                    //需要变化的行数
                    int offsetLine = offsetY/lineHeight;
                    //计算居中行数
                    centerLine = centerLine+offsetLine;
                    //判断是否居中行越界
                    if(centerLine<0){
                        centerLine = 0;
                    }else if(centerLine>lyricBeens.size()-1){
                        centerLine = lyricBeens.size()-1;
                    }
                    //修改offsetY
                    offsetY=offsetY%lineHeight;
                    //重新改变downy
                    downY=moveY;
                }
                //重新绘制
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                handleLyric = true;
                //跳转到制定的位置播放
                if(playListener!=null){
                    playListener.onPlay(lyricBeens.get(centerLine).getStartTime());
                }
                break;
        }
        return true;
    }
    //歌词播放进度
    public interface PlayListener{
        void onPlay(int progress);
    }
    public void setPlayListener(PlayListener playListener){
        this.playListener =  playListener;
    }
}

