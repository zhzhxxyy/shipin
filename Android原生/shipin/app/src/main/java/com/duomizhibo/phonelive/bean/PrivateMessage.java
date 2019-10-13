package com.duomizhibo.phonelive.bean;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.duomizhibo.phonelive.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weipeng on 16/8/15.
 */
public class PrivateMessage {
    public EMMessage message;
    public String uHead;

    public static PrivateMessage crateMessage(EMMessage message,String uHead){
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.message = message;
        privateMessage.uHead = uHead;
        return privateMessage;
    }



}
