package com.duomizhibo.phonelive;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.duomizhibo.phonelive.bean.UserBean;

public class RemoteGameAppService extends Service {
    Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
           if (msg.getData().getString("data").equals("userinfo")){
               Message message = Message.obtain();
               Bundle bundle = new Bundle();
               UserBean mUser=AppContext.getInstance().getLoginUser();
               bundle.putString("token",mUser.token);
               bundle.putString("id",mUser.id);
               bundle.putString("name",mUser.user_nicename);
               bundle.putString("avatar",mUser.avatar);
               bundle.putString("coin",mUser.coin);
               bundle.putString("level",mUser.level);
               bundle.putString("signature",mUser.signature);
               bundle.putString("sex",mUser.sex);
               bundle.putString("avatar_thumb",mUser.avatar_thumb);
               message.setData(bundle);
               try {
                   msg.replyTo.send(message);
               } catch (RemoteException e) {
                   e.printStackTrace();
               }
           }
            super.handleMessage(msg);
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("kk", "绑定成功！");
        return messenger.getBinder();
    }
}
