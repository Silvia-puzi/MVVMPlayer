package com.example.administrator.mvvmplayer.bean;

import java.util.List;

/*
 *  @项目名：  MVVMPlayer 
 *  @包名：    com.example.administrator.mvvmplayer.bean
 *  @文件名:   MusicBean
 *  @创建者:   Administrator
 *  @创建时间:  2017/6/29 23:35
 *  @描述：    TODO
 */
public class MusicBean {
    private static final String TAG = "MusicBean";

    /**
     * code : 0
     * count : 15
     * result : [{"aid":1069481,"artist_id":2463,"lrc":"http://s.gecimi.com/lrc/108/10848/1084861.lrc","sid":1084861,"song":"好久不见"},{"aid":1287623,"artist_id":8653,"lrc":"http://s.gecimi.com/lrc/132/13274/1327424.lrc","sid":1327424,"song":"好久不见"},{"aid":1341668,"artist_id":8653,"lrc":"http://s.gecimi.com/lrc/138/13895/1389519.lrc","sid":1389519,"song":"好久不见"},{"aid":1509752,"artist_id":8653,"lrc":"http://s.gecimi.com/lrc/159/15984/1598453.lrc","sid":1598453,"song":"好久不见"},{"aid":2038061,"artist_id":8653,"lrc":"http://s.gecimi.com/lrc/231/23133/2313359.lrc","sid":2313359,"song":"好久不见"},{"aid":2377370,"artist_id":8653,"lrc":"http://s.gecimi.com/lrc/278/27875/2787585.lrc","sid":2787585,"song":"好久不见"},{"aid":2996822,"artist_id":8653,"lrc":"http://s.gecimi.com/lrc/364/36426/3642609.lrc","sid":3642609,"song":"好久不见"},{"aid":2902448,"artist_id":9931,"lrc":"http://s.gecimi.com/lrc/351/35185/3518500.lrc","sid":3518500,"song":"好久不见"},{"aid":2335898,"artist_id":10446,"lrc":"http://s.gecimi.com/lrc/272/27280/2728031.lrc","sid":2728031,"song":"好久不见"},{"aid":2346599,"artist_id":10446,"lrc":"http://s.gecimi.com/lrc/274/27441/2744170.lrc","sid":2744170,"song":"好久不见"},{"aid":2864846,"artist_id":10893,"lrc":"http://s.gecimi.com/lrc/346/34667/3466785.lrc","sid":3466785,"song":"好久不见"},{"aid":2949734,"artist_id":20030,"lrc":"http://s.gecimi.com/lrc/358/35813/3581394.lrc","sid":3581394,"song":"好久不见"},{"aid":3098306,"artist_id":20030,"lrc":"http://s.gecimi.com/lrc/377/37798/3779825.lrc","sid":3779825,"song":"好久不见"},{"aid":3347462,"artist_id":27927,"lrc":"http://s.gecimi.com/lrc/410/41034/4103447.lrc","sid":4103447,"song":"好久不见"},{"aid":3441269,"artist_id":30796,"lrc":"http://s.gecimi.com/lrc/421/42134/4213491.lrc","sid":4213491,"song":"好久不见"}]
     * ts : 1499006461
     */

    private int code;
    private int count;
    private int ts;
    /**
     * aid : 1069481
     * artist_id : 2463
     * lrc : http://s.gecimi.com/lrc/108/10848/1084861.lrc
     * sid : 1084861
     * song : 好久不见
     */

    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int aid;
        private int artist_id;
        private String lrc;
        private int sid;
        private String song;

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(int artist_id) {
            this.artist_id = artist_id;
        }

        public String getLrc() {
            return lrc;
        }

        public void setLrc(String lrc) {
            this.lrc = lrc;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getSong() {
            return song;
        }

        public void setSong(String song) {
            this.song = song;
        }
    }
}
