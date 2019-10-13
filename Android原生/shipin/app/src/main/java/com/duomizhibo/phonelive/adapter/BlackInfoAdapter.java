package com.duomizhibo.phonelive.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.SimpleUtils;
import com.duomizhibo.phonelive.widget.BlackTextView;
import com.duomizhibo.phonelive.widget.CircleImageView;
import com.duomizhibo.phonelive.R;

import java.util.List;

/**
 *黑名单适配器
 */
public class BlackInfoAdapter extends BaseAdapter {
    private List<UserBean> users;
    public BlackInfoAdapter(List<UserBean> users) {
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            
            convertView = View.inflate(AppContext.getInstance(),R.layout.item_black_info,null);
            viewHolder = new ViewHolder();
            viewHolder.mUHead = (CircleImageView) convertView.findViewById(R.id.cv_userHead);
            viewHolder.mUSex  = (ImageView) convertView.findViewById(R.id.tv_item_usex);
            viewHolder.mULevel  = (ImageView) convertView.findViewById(R.id.tv_item_ulevel);
            viewHolder.mUNice = (BlackTextView) convertView.findViewById(R.id.tv_item_uname);
            viewHolder.mUSign = (BlackTextView) convertView.findViewById(R.id.tv_item_usign);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UserBean u = users.get(position);

        SimpleUtils.loadImageForView(AppContext.getInstance(),viewHolder.mUHead,u.avatar_thumb,0);
        viewHolder.mUSex.setImageResource(LiveUtils.getSexRes(u.sex));
        viewHolder.mULevel.setImageResource(LiveUtils.getLevelRes(u.level));
        viewHolder.mUNice.setText(u.user_nicename);
        viewHolder.mUSign.setText(u.signature);
        return convertView;
    }
    private class ViewHolder{
        public CircleImageView mUHead;
        public ImageView mUSex,mULevel;
        public BlackTextView mUNice,mUSign;
    }
}
