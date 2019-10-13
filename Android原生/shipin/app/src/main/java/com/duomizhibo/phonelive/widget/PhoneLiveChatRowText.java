package com.duomizhibo.phonelive.widget;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.utils.TLog;

/**
 * 私信消息
 */
public class PhoneLiveChatRowText extends PhoneLiveChatRow {
    private TextView contentView;
    private AvatarView mUhead;
    private String uhead;
    public PhoneLiveChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter,String uhead) {
        super(context, message, position, adapter);
        //头像图片
        this.uhead = uhead;
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.item_message_left : R.layout.item_message_right, this);
    }

    @Override
    protected void onFindViewById() {
//        contentView = (TextView) findViewById(R.id.tv_message_text);
//        mUhead = (AvatarView) findViewById(R.id.av_message_head);
    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();

        //设置内容
        contentView.setText(txtBody.getMessage());

        //设置头像
        mUhead.setAvatarUrl(uhead);

        handleTextMessage();
    }
    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    // 发送消息
//                sendMsgInBackground(message);
                    break;
                case SUCCESS: // 发送成功
                    break;
                case FAIL: // 发送失败
                    break;
                case INPROGRESS: // 发送中
                    break;
                default:
                    break;
            }
        }else{
            if(!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat){
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onBubbleClick() {

    }
}
