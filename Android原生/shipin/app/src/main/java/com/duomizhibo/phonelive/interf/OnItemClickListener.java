package com.duomizhibo.phonelive.interf;

import com.duomizhibo.phonelive.bean.LiveAndVideoBean;
import com.duomizhibo.phonelive.bean.LiveJson;

/**
 * Created by cxf on 2017/7/3.
 */

public interface OnItemClickListener<T> {
    void onItemClick(T item);
}