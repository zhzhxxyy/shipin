package com.duomizhibo.phonelive.model;

import com.hyphenate.chat.EMMessage;

/**
 * Created by Administrator on 2016/4/14.
 */
public class MyNotifier extends Notifier {
    @Override
    public void sendNotification( EMMessage message,  boolean isForeground) {
       super.sendNotification(message,isForeground);

    }

}
