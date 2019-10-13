package com.duomizhibo.phonelive.adapter.video;


import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.video.VideoClass;


public class VideoTagAdapter extends ObjectAdapter {
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.adapter_video_tag_item, null);
            holder.ll_check = convertView.findViewById(R.id.ll_check);
            holder.tv_check_name = (TextView) convertView.findViewById(R.id.tv_check_name);
            holder.tv_check_tag = (TextView) convertView.findViewById(R.id.tv_check_tag);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        VideoClass bean = (VideoClass) getItem(position);
        holder.tv_check_name.setText(bean.getName());

        if(bean.isCheck()){
            holder.ll_check.setBackgroundResource(R.drawable.bg_corner_color_5fb878);
            holder.tv_check_name.setBackgroundResource(R.color.color_5fb878);
            holder.tv_check_tag.setTextColor(convertView.getResources().getColor(R.color.color_5fb878));
        }else{
            holder.ll_check.setBackgroundResource(R.drawable.bg_corner_color_d8d8d8);
            holder.tv_check_name.setBackgroundResource(R.color.colorGray7);
            holder.tv_check_tag.setTextColor(convertView.getResources().getColor(R.color.white));
        }
        holder.ll_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final VideoClass bean = (VideoClass) getItem(position);
                bean.setCheck(!bean.isCheck());
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder{
        View ll_check;
        TextView tv_check_name;
        TextView tv_check_tag;
    }
}
