package com.duomizhibo.phonelive.base;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.event.Event;
import com.duomizhibo.phonelive.fragment.MessageDetailDialogFragment;
import com.duomizhibo.phonelive.ui.other.DrawableRes;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.SimpleUtils;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.PrivateChatUserBean;
import com.duomizhibo.phonelive.interf.DialogInterface;
import com.duomizhibo.phonelive.widget.CircleImageView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Call;


public abstract class PrivateChatPageBase extends BaseFragment {
    private BroadcastReceiver broadCastReceiver;

    //回话列表
    protected ArrayList<PrivateChatUserBean> mPrivateChatListData = new ArrayList<>();
    protected ListView mPrivateListView;
    protected int mPosition = -1;
    protected UserBean mUser;
    protected Map<String, EMConversation> emConversationMap;
    protected Gson mGson = new Gson();
    private UserBaseInfoPrivateChatAdapter mUserBaseInfoPrivateChatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_private_chat,null);
        initCreateView(inflater,container,savedInstanceState);

        ButterKnife.inject(this,v);

        initBroadCast();

        initView(v);

        initData();
        return v;
    }

    @Override
    public void initView(View view) {
        mPrivateListView = (ListView) view.findViewById(R.id.lv_privatechat);

        mPrivateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onPrivateChatItemClick(position);
            }
        });
    }

    //回话点击时间
    private void onPrivateChatItemClick(int position) {
        mPosition = position;
        PrivateChatUserBean privateChatUserBean = mPrivateChatListData.get(position);
        //标注私信状态为已读
        mPrivateChatListData.get(position).unreadMessage = false;

        updatePrivateChatList();

        //判断当前页面是直播间弹窗还是单页面
        if(getParentFragment() instanceof DialogFragment){
            MessageDetailDialogFragment messageFragment = new MessageDetailDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user",privateChatUserBean);
            messageFragment.setArguments(bundle);

            messageFragment.show(getFragmentManager(),"MessageDetailDialogFragment");
            messageFragment.setDialogInterface(new DialogInterface() {
                @Override
                public void cancelDialog(View v, Dialog d) {
                    onResumeUpdate();
                }

                @Override
                public void determineDialog(View v, Dialog d) {

                }
            });
        }else {
            UIHelper.showPrivateChatMessage(getActivity(),privateChatUserBean);
        }
    }


    //注册监听私信消息广播
    private void initBroadCast() {
        IntentFilter cmdFilter = new IntentFilter("com.duomizhibo.phonelive");
        if(broadCastReceiver == null){
            broadCastReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    // TODO Auto-generated method stub
                    onNewMessage((EMMessage)intent.getParcelableExtra("cmd_value"));
                }
            };
        }
        getActivity().registerReceiver(broadCastReceiver,cmdFilter);
    }
    @Override
    public void onResume() {
        super.onResume();
        onResumeUpdate();


    }

    public void onResumeUpdate(){
        if(null == mPrivateChatListData || mPrivateChatListData.size() == 0) return;
        //获取有没有最后一条消息
        try {
            //将该条会话消息未读清零
            EMConversation conversation = EMClient.getInstance()
                    .chatManager()
                    .getConversation(String.valueOf(mPrivateChatListData.get(mPosition).id));
            conversation.markAllMessagesAsRead();
            //设置最后一条消息
            mPrivateChatListData.get(mPosition).lastMessage = ((EMTextMessageBody) conversation.getLastMessage().getBody()).getMessage();

        }catch (Exception e){
            //无最后一条消息
        }
        mPosition = -1;
        updatePrivateChatList();
    }
    //更新回话列表
    protected void updatePrivateChatList(){
        mPrivateListView.setAdapter(mUserBaseInfoPrivateChatAdapter = new UserBaseInfoPrivateChatAdapter());
    }

    //初始化会话列表
    protected void initConversationList(int action) {
        //获取所有会话消息列表
        emConversationMap = EMClient.getInstance().chatManager().getAllConversations();

        StringBuilder keys = new StringBuilder();
        for(String key : emConversationMap.keySet()){
            keys.append(key + ",");
        }
        if(keys.toString().length() == 0)return;
        String uidList = keys.toString().substring(0,keys.length()-1);

        //获取每个绘画用户的信息和关注状态

        //PhoneLiveApi.getMultiBaseInfo(action,mUser.id,uidList,multiBaseInfoCallback);

    }


    protected void fillUI() {
        if(mPrivateChatListData == null){
            return;
        }
        updatePrivateChatList();
    }

    private StringCallback multiBaseInfoCallback = new StringCallback(){

        @Override
        public void onError(Call call, Exception e,int id) {

            TLog.log("[获取会话列表用户信息error]:" + call.request().toString());
        }

        @Override
        public void onResponse(String response,int id) {
            JSONArray fansJsonArr = ApiUtils.checkIsSuccess(response);

            if(null != fansJsonArr){
                TLog.log("[获取会话列表用户信息success]:" + fansJsonArr.toString());
                try {

                    if(fansJsonArr.length() > 0){

                        for(int i =0;i<fansJsonArr.length(); i++){

                            PrivateChatUserBean chatUserBean = mGson.fromJson(fansJsonArr.getString(i), PrivateChatUserBean.class);
                            //获取单条会话信息
                            EMConversation conversation = EMClient.getInstance()
                                    .chatManager()
                                    .getConversation(String.valueOf(chatUserBean.id));

                            try {
                                //设置该会话最后一条信息
                                chatUserBean.lastMessage = (((EMTextMessageBody) conversation.getLastMessage().getBody()).getMessage());
                                //获取该会话是否有未读消息
                                chatUserBean.unreadMessage = (conversation.getUnreadMsgCount() > 0?true:false);

                            }catch (Exception e){
                                //无最后一条消息纪录
                            }

                            if(conversation.getUnreadMsgCount() > 0){
                                mPrivateChatListData.add(0,chatUserBean);
                            }else{
                                mPrivateChatListData.add(chatUserBean);
                            }

                        }
                        //填充会话列表
                        fillUI();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //会话列表中已存在更新该会话最后一条信息
    protected void updataLastMessage(final EMMessage messages) {

        for(int i =0; i < mPrivateChatListData.size(); i++){
            PrivateChatUserBean privateChatUserBean = mPrivateChatListData.get(i);

            //判断当前新消息是那个会话
            if(privateChatUserBean.id.equals(messages.getFrom())){
                privateChatUserBean.lastMessage = ((EMTextMessageBody)(messages.getBody())).getMessage();
                if(i == mPosition){
                    //未读消息
                    privateChatUserBean.unreadMessage = (false);
                }else{
                    privateChatUserBean.unreadMessage = (true);
                }
                //privateChatUserBean.setUnreadMessage(true);
                //将该回话移到第一位
                mPrivateChatListData.remove(i);
                mPrivateChatListData.add(0,privateChatUserBean);
                //mPrivateChatListData.set(i, privateChatUserBean);
                updatePrivateChatList();
                continue;
            }
        }
    }

    //没有在会话列表中,所以添加一个item
    protected void inConversationMapAddItem(final EMMessage messages) {
        emConversationMap = EMClient.getInstance().chatManager().getAllConversations();
//        PhoneLiveApi.getPmUserInfo(mUser.id,messages.getFrom(), new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e,int id) {
//
//            }
//
//            @Override
//            public void onResponse(String response,int id) {
//                JSONArray res = ApiUtils.checkIsSuccess(response);
//                if(null != res){
//                    PrivateChatUserBean privateChatUserBean = null;
//                    try {
//                        privateChatUserBean = mGson.fromJson(res.getString(0),PrivateChatUserBean.class);
//                        privateChatUserBean.lastMessage = (((EMTextMessageBody)(messages.getBody())).getMessage());
//                        privateChatUserBean.unreadMessage = (true);
//                        //将该回话移到第一位
//                        mPrivateChatListData.add(0,privateChatUserBean);
//                        //mPrivateChatListData.add(privateChatUserBean);
//                        updatePrivateChatList();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }
//        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.PrivateChatEvent event) {
        if(event.action == 1){

            for(int i = 0; i < mPrivateChatListData.size(); i++){
                PrivateChatUserBean d = mPrivateChatListData.get(i);
                d.unreadMessage = false;
                mPrivateChatListData.set(i,d);
            }
            mUserBaseInfoPrivateChatAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    protected abstract void initCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract void onNewMessage(EMMessage messages);


    @Override
    public void onDestroy() {
        //获取是否有未读消息
        super.onDestroy();

        getActivity().unregisterReceiver(broadCastReceiver);


    }

    //私信会话列表
    public class UserBaseInfoPrivateChatAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mPrivateChatListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mPrivateChatListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = View.inflate(AppContext.getInstance(),R.layout.item_private_chat,null);
                viewHolder = new ViewHolder();
                viewHolder.mUHead = (CircleImageView) convertView.findViewById(R.id.cv_userHead);
                viewHolder.mUSex  = (ImageView) convertView.findViewById(R.id.tv_item_usex);
                viewHolder.mULevel  = (ImageView) convertView.findViewById(R.id.tv_item_ulevel);
                viewHolder.mUNice = (TextView) convertView.findViewById(R.id.tv_item_uname);
//                viewHolder.mULastMsg = (TextView) convertView.findViewById(R.id.tv_item_last_msg);
//                viewHolder.mUnread = (ImageView) convertView.findViewById(R.id.iv_unread_dot);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            PrivateChatUserBean u = mPrivateChatListData.get(position);


            SimpleUtils.loadImageForView(AppContext.getInstance(),viewHolder.mUHead,u.avatar_thumb,0);
            viewHolder.mUSex.setImageResource(LiveUtils.getSexRes(u.sex));
            viewHolder.mULevel.setImageResource(LiveUtils.getLevelRes(u.level));
            viewHolder.mUNice.setText(u.user_nicename);
            viewHolder.mULastMsg.setText(u.lastMessage);
            viewHolder.mUnread.setVisibility(u.unreadMessage ? View.VISIBLE:View.GONE);


            return convertView;
        }



        private class ViewHolder{
             CircleImageView mUHead;
             ImageView mUSex,mULevel,mUnread;
             TextView mUNice,mULastMsg;
        }
    }
}
