package com.duomizhibo.phonelive.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.duomizhibo.phonelive.bean.MusicBean;
import com.duomizhibo.phonelive.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/5.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add music
     * @param musics
     */
    public void add(List<MusicBean> musics) {
        db.beginTransaction();  //开始事务
        try {
            for (MusicBean music : musics) {
                db.execSQL("INSERT INTO music VALUES(null, ?, ?, ?,?)", new Object[]{music.audio_name, music.audio_id, music.artist_name,music.audio_id});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }
    public void delete(MusicBean music){
        db.delete("music","songid=?",new String[]{music.audio_id});
    }

    /**
     * update music's songid
     * @param music
     */
    public void updateSongId(MusicBean music) {
        ContentValues cv = new ContentValues();
        cv.put("songid", music.audio_id);
        db.update("music", cv, "songname = ?", new String[]{music.audio_name});
    }

    /**
     * query all music, return list
     * @return List<music>
     */
    public List<MusicBean> query() {
        ArrayList<MusicBean> musics = new ArrayList<MusicBean>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            MusicBean music = new MusicBean();
            music.audio_id = c.getString(c.getColumnIndex("songid"));
            music.audio_name = c.getString(c.getColumnIndex("songname"));
            music.artist_name = c.getString(c.getColumnIndex("artistname"));
            //music.setEncrypted_songid( c.getString(c.getColumnIndex("encrypted_songid")));
            musics.add(music);
        }
        c.close();
        return musics;
    }

    /**
     * query all music, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM music", null);
        return c;
    }
    /**
     * query all music, return cursor
     * @return  Cursor
     */
    public Cursor queryFromEncryptedSongId(String encrypted_songid) {
        Cursor c = db.rawQuery("SELECT * FROM music where encrypted_songid = ?", new String[]{encrypted_songid});
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
