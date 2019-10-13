package com.duomizhibo.phonelive.adapter.video;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.video.VideoComment;
import com.duomizhibo.phonelive.bean.video.VideoObject;
import com.duomizhibo.phonelive.utils.UIHelper;


public class VideoTuijianAdapter extends ObjectAdapter {
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.adapter_video_tuijian_item, null);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_zan = (TextView) convertView.findViewById(R.id.tv_zan);
            holder.tv_click = (TextView) convertView.findViewById(R.id.tv_click);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        VideoObject bean = (VideoObject) getItem(position);

        Glide.with(convertView.getContext()).load(bean.getThumbnail()).placeholder(null).into(holder.iv_img);
        holder.tv_time.setText(bean.getPlay_time()+"");
        holder.tv_title.setText(bean.getTitle()+"");
        holder.tv_zan.setText(bean.getGood()+"");
        holder.tv_click.setText(bean.getClick()+"");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoObject bean = (VideoObject) getItem(position);

                UIHelper.showVideoPlayerNew(v.getContext(),bean);
            }
        });
        return convertView;
    }



    class ViewHolder{
        ImageView iv_img;
        TextView tv_time;
        TextView tv_title;
        TextView tv_zan;
        TextView tv_click;
    }
}
