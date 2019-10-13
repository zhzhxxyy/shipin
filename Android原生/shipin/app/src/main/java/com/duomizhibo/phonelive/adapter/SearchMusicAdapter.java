package com.duomizhibo.phonelive.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.bean.MusicBean;
import com.duomizhibo.phonelive.fragment.SearchMusicDialogFragment;
import com.duomizhibo.phonelive.utils.DBManager;
import com.dd.CircularProgressButton;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.ui.StartLiveActivity;
import com.duomizhibo.phonelive.widget.BlackTextView;

import java.io.File;
import java.util.List;

/**
 * 搜索音乐列表
 */
public class SearchMusicAdapter extends BaseAdapter {
    private List<MusicBean> mMusicList;
    private SearchMusicDialogFragment mFragment;
    private DBManager mDbManager;

    public SearchMusicAdapter(List<MusicBean> MusicList, SearchMusicDialogFragment fragment, DBManager dbManager){
        this.mMusicList =  MusicList;
        this.mFragment = fragment;
        this.mDbManager = dbManager;
    }
    public void notifyDataSetChangedMusicList(List<MusicBean> MusicList){
        this.mMusicList =  MusicList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mMusicList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        convertView = View.inflate(AppContext.getInstance(), R.layout.item_search_music,null);
        viewHolder.mMusicName = (BlackTextView) convertView.findViewById(R.id.item_tv_search_music_name);
        viewHolder.mMusicAuthor = (BlackTextView) convertView.findViewById(R.id.item_tv_search_music_author);
        viewHolder.mBtnDownload = (CircularProgressButton) convertView.findViewById(R.id.item_btn_search_music_download);

        final MusicBean music = mMusicList.get(position);
        viewHolder.mMusicName.setText(music.audio_name);
        viewHolder.mMusicAuthor.setText(music.artist_name);
        final File file = new File(AppConfig.DEFAULT_SAVE_MUSIC_PATH + music.audio_name + ".mp3");


        //判断该音乐是否存在
        if(mDbManager.queryFromEncryptedSongId(music.audio_id).getCount() != 0){
            viewHolder.mBtnDownload.setText(R.string.select);
        }
        //点击下载或播放
        viewHolder.mBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                //判断该音乐是否存在,存在直接播放
                if(mDbManager.queryFromEncryptedSongId(music.audio_id).getCount() != 0){
                    intent = new Intent();
                    ((StartLiveActivity)mFragment.getActivity()).onSelectMusic(intent.putExtra("filepath",file.getPath()));
                }else {
                    mFragment.downloadMusic(music,(CircularProgressButton)v);
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        BlackTextView mMusicName,mMusicAuthor;
        CircularProgressButton mBtnDownload;
    }
}
