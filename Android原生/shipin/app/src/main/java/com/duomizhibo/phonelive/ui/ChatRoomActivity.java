package com.duomizhibo.phonelive.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.duomizhibo.phonelive.fragment.MessageDetailFragment2;
import com.duomizhibo.phonelive.R;


//个人中心私信发送页面
public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        initView();
    }

    public void initView() {
        MessageDetailFragment2 fragment = new MessageDetailFragment2();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", getIntent().getParcelableExtra("user"));
        bundle.putInt("type", 0);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.replaced, fragment);
        ft.commit();
    }

}
