package com.duomizhibo.phonelive.adapter.video;


import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.video.VideoClass;


public class VideoClassAdapter extends ObjectAdapter {
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.adapter_video_class_item, null);
            holder.tv_class = (TextView) convertView.findViewById(R.id.tv_class);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        VideoClass bean = (VideoClass) getItem(position);
        holder.tv_class.setText(bean.getName());
        return convertView;
    }

    class ViewHolder{
        TextView tv_class;
    }
}
