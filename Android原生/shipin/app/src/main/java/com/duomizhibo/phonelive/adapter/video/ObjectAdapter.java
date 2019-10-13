package com.duomizhibo.phonelive.adapter.video;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectAdapter extends BaseAdapter {

    protected List content = new ArrayList();

    public int getCount() {
        return content.size();
    }

    public Object getItem(int position) {
        return content.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int indexOf(Object o) {
        return content.indexOf(o);
    }

    public void addItem(Object o) {
        this.content.add(o);
    }

    public void addItem(List ls) {
        this.content.addAll(ls);
    }

    public void addItem(Object[] list) {
        for (Object o : list) {
            this.content.add(o);
        }
    }

    public void clear() {
        this.content.clear();
    }

    public List getContent() {
        return content;
    }
}
