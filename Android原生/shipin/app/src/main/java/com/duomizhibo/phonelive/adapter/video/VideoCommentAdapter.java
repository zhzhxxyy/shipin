package com.duomizhibo.phonelive.adapter.video;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tedcoder.wkvideoplayer.model.Video;
import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.video.VideoClass;
import com.duomizhibo.phonelive.bean.video.VideoComment;


public class VideoCommentAdapter extends ObjectAdapter {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.adapter_video_comment_item, null);
            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        VideoComment bean = (VideoComment) getItem(position);

        Glide.with(convertView.getContext()).load(bean.getHeadimgurl()).placeholder(R.drawable.user_dafault_headimg).into(holder.iv_head);
        holder.tv_name.setText(bean.getNickname());
        holder.tv_time.setText(bean.getLastTimeString());
        holder.tv_content.setText(bean.getContent()+"");
        return convertView;
    }



    class ViewHolder{
        ImageView iv_head;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
    }
}
