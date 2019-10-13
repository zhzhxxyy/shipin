package com.duomizhibo.phonelive.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.ConversationListAdapter;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsDialogFragment;
import com.duomizhibo.phonelive.bean.ChatListBean;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.event.AttentEvent;
import com.duomizhibo.phonelive.event.ChatExitEvent;
import com.duomizhibo.phonelive.ui.ChatRoomActivity;
import com.duomizhibo.phonelive.ui.customviews.ViewPagerIndicator;
import com.duomizhibo.phonelive.utils.DpOrSp2PxUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class MessageFragment extends AbsDialogFragment {

    private View mRootView;
    private TextView mTvIgnoreMessage;
    private Context mContext;
    private RecyclerView mFollowView;
    private RecyclerView mNotFollowView;

    private ConversationListAdapter mFollowAdapter;
    private ConversationListAdapter mNotFollowAdapter;

    private Gson mGson = new Gson();
    private Type mGsonType = new TypeToken<List<ChatListBean>>() {
    }.getType();

    private EMChatManager mChatManager;
    Map<String, EMConversation> mMap;
    Map<String, ChatListBean> mUserMap;
    private ViewPager mViewPager;
    private TextView mFollowUnReadPoint;
    private TextView mNotFollowUnReadPoint;
    private int mFollowUnReadCount;
    private int mNotFollowUnReadCount;
    private BroadcastReceiver mMessageReceiver;
    private int mType;//0 个人主页进入的聊天页面 1是直播间进入的聊天页面
    private static List<ChatStateObserver> sChatStateObservers;
    public static boolean ignoreUnReadMessage;
    public static ChatExitEvent chatExitEvent;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        Dialog dialog = new Dialog(mContext, R.style.BottomViewTheme_Transparent);
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_message, null, false);
        dialog.setContentView(mRootView);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.BottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DpOrSp2PxUtil.dp2pxConvertInt(mContext, 300);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mRootView = inflater.inflate(R.layout.fragment_message, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public void initView() {
        mType = getArguments().getInt("type");
        ViewPagerIndicator indicator = (ViewPagerIndicator) mRootView.findViewById(R.id.indicator2);
        indicator.setTitles(new String[]{"已关注", "未关注"});
        indicator.setVisibleChildCount(2);
        indicator.setChangeSize(false);

        mFollowView = new RecyclerView(mContext);
        mFollowView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFollowView.setHasFixedSize(true);
        mFollowView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mNotFollowView = new RecyclerView(mContext);
        mNotFollowView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mNotFollowView.setHasFixedSize(true);
        mNotFollowView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mViewPager = (ViewPager) mRootView.findViewById(R.id.viewPager);
        final List<View> viewList = new ArrayList<>();
        viewList.add(mFollowView);
        viewList.add(mNotFollowView);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = viewList.get(position);
                container.addView(v);
                return v;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }
        });
        indicator.setViewPager(mViewPager);

        mTvIgnoreMessage = (TextView) mRootView.findViewById(R.id.tv_ignore_message);
        mTvIgnoreMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ignoreUnReadMessage = true;
                if (sChatStateObservers != null) {
                    for (ChatStateObserver observer : sChatStateObservers) {
                        observer.onIgnoreUnReadMessage();
                    }
                }
                AppContext.toast("已忽略未读消息");
            }
        });
        mFollowUnReadPoint = (TextView) mRootView.findViewById(R.id.follow_unread_count);
        mNotFollowUnReadPoint = (TextView) mRootView.findViewById(R.id.notfollow_unread_count);
        mUserMap = new HashMap<>();
        mChatManager = EMClient.getInstance().chatManager();
        mMap = mChatManager.getAllConversations();
        registerReceiver();
        EventBus.getDefault().register(this);
    }

    //忽略未读消息的时候调用这个
    public void onIgnoreUnReadMessage() {
        for (Map.Entry<String, ChatListBean> entry : mUserMap.entrySet()) {
            entry.getValue().setUnReadCount(0);
        }
        if(mFollowAdapter!=null){
            mFollowAdapter.notifyDataSetChanged();
        }
        if(mNotFollowAdapter!=null){
            mNotFollowAdapter.notifyDataSetChanged();
        }
        mChatManager.markAllConversationsAsRead();
        mFollowUnReadCount = 0;
        mNotFollowUnReadCount = 0;
        if (mNotFollowUnReadPoint.getVisibility() == View.VISIBLE) {
            mNotFollowUnReadPoint.setVisibility(View.GONE);
        }
        if (mFollowUnReadPoint.getVisibility() == View.VISIBLE) {
            mFollowUnReadPoint.setVisibility(View.GONE);
        }
    }

    //注册监听私信消息广播
    private void registerReceiver() {
        IntentFilter cmdFilter = new IntentFilter("com.duomizhibo.phonelive");
        if (mMessageReceiver == null) {
            mMessageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    onNewMessage((EMMessage) intent.getParcelableExtra("cmd_value"));
                }
            };
        }
        getActivity().registerReceiver(mMessageReceiver, cmdFilter);
    }


    private String getUids() {
        mMap = mChatManager.getAllConversations();
        String uids = "";
        for (String key : mMap.keySet()) {
            uids += key + ",";
        }
        if(uids.length()>0){
            uids = uids.substring(0, uids.length() - 1);
        }
        return uids;
    }

    //同时请求查看已关注和未关注的接口
    private void requestAll() {
        String uids = getUids();
        if (!"".equals(uids)) {
            requestChatList("1", uids);//已关注的接口
            requestChatList("0", uids);//未关注的接口
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mType == 1){
            requestAll();
        }
    }

    private void requestChatList(final String type, String uids) {
        PhoneLiveApi.getMultiBaseInfo(type, uids, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("200".equals(obj.getString("ret"))) {
                        String s = obj.getJSONObject("data").getString("info");
                        List<ChatListBean> list = mGson.fromJson(s, mGsonType);
                        refreshList(list, type);
                    } else {
                        AppContext.toast("无法获取会话户列表");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ConversationListAdapter createAdapter(List<ChatListBean> list) {
        ConversationListAdapter adapter = new ConversationListAdapter(mContext, list);
        adapter.setOnItemClickListener(new ConversationListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ChatListBean bean, int position) {
                onItemClick(bean);
            }
        });
        adapter.setItemStateChangeListener(new ConversationListAdapter.ItemStateChangeListener() {
            @Override
            public void onInsertItem(ChatListBean bean) {
                onNewUnReadMessageComing(bean);
            }

            @Override
            public void onUpdateItem(ChatListBean bean, int unReadCount) {
                boolean isFollow = "1".equals(bean.getUtot());
                if (isFollow) {
                    mFollowUnReadCount += unReadCount;
                } else {
                    mNotFollowUnReadCount += unReadCount;
                }
                refreshUnReadPoint(isFollow);
            }

            @Override
            public void onRemoveItem(ChatListBean bean) {
                boolean isFollow = "1".equals(bean.getUtot());
                int unReadCount = bean.getUnReadCount();
                if (isFollow) {
                    mFollowUnReadCount -= unReadCount;
                } else {
                    mNotFollowUnReadCount -= unReadCount;
                }
                refreshUnReadPoint(isFollow);
            }
        });
        return adapter;
    }


    private void refreshList(List<ChatListBean> list, String type) {
        int unReadCount = 0;
        for (ChatListBean bean : list) {
            EMConversation conversation = mChatManager.getConversation(bean.getId());
            bean.setLastMessage(((EMTextMessageBody) conversation.getLastMessage().getBody()).getMessage());
            int count = conversation.getUnreadMsgCount();
            bean.setUnReadCount(count);
            mUserMap.put(bean.getId(), bean);
            unReadCount += count;
        }
        boolean isFollow = "1".equals(type);
        if (isFollow) {
            if (mFollowAdapter == null) {
                mFollowAdapter = createAdapter(list);
                mFollowView.setAdapter(mFollowAdapter);
            } else {
                mFollowAdapter.setList(list);
                mFollowAdapter.notifyDataSetChanged();
            }
            mFollowUnReadCount = unReadCount;
        } else {
            if (mNotFollowAdapter == null) {
                mNotFollowAdapter = createAdapter(list);
                mNotFollowView.setAdapter(mNotFollowAdapter);
            } else {
                mNotFollowAdapter.setList(list);
                mNotFollowAdapter.notifyDataSetChanged();
            }
            mNotFollowUnReadCount = unReadCount;
        }
        refreshUnReadPoint(isFollow);
    }

    private void onItemClick(ChatListBean bean) {
        if (mType == 0) {
            Intent intent = new Intent(mContext, ChatRoomActivity.class);
            UserBean userBean = new UserBean();
            userBean.id = bean.getId();
            userBean.user_nicename = bean.getUser_nicename();
            userBean.avatar = bean.getAvatar();
            intent.putExtra("user", userBean);
            startActivity(intent);
        } else {
            UserBean userBean = new UserBean();
            userBean.id = bean.getId();
            userBean.user_nicename = bean.getUser_nicename();
            userBean.avatar = bean.getAvatar();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", userBean);
            bundle.putInt("type", 1);
            MessageDetailFragment2 messageDetailFragment = new MessageDetailFragment2();
            messageDetailFragment.setArguments(bundle);
            messageDetailFragment.show(getActivity().getSupportFragmentManager(), "MessageDetailFragment");
        }
    }

    private void onNewMessage(EMMessage message) {
        //获取消息的来源，得到的是用户的id
        String from = message.getFrom();
        final String msg = ((EMTextMessageBody) message.getBody()).getMessage();
        //如果会话列表中不包含这个key
        if (!mMap.containsKey(from)) {
            //请求接口，确定这个陌生人是关注的还是未关注的
            PhoneLiveApi.getPmUserInfo(from, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject info0 = obj.getJSONObject("data").getJSONArray("info").getJSONObject(0);
                        ChatListBean bean = mGson.fromJson(info0.toString(), ChatListBean.class);
                        bean.setUtot(info0.getString("isattention"));
                        bean.setTtou(info0.getString("isattention2"));
                        bean.setLastMessage(msg);
                        if (!ignoreUnReadMessage) {
                            EMConversation conversation = mChatManager.getConversation(bean.getId());
                            if(conversation==null){
                                bean.setUnReadCount(1);
                            }else{
                                bean.setUnReadCount(conversation.getUnreadMsgCount());
                            }

                        }
                        mUserMap.put(bean.getId(), bean);
                        insertItem(bean);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            mMap = mChatManager.getAllConversations();
        } else {
            if (mUserMap.size() != mMap.size()) {
                requestAll();
            } else {
                ChatListBean bean = mUserMap.get(from);
                bean.setLastMessage(msg);
                EMConversation conversation = mChatManager.getConversation(bean.getId());
                bean.setUnReadCount(conversation.getUnreadMsgCount());
                onNewMessageUpdate(bean);

            }
        }
        if(ignoreUnReadMessage){
            mChatManager.markAllConversationsAsRead();
        }
    }


    //插入一条新的会话
    private void insertItem(ChatListBean bean) {
        boolean isFollow = "1".equals(bean.getUtot());
        if (isFollow) {//我已经关注对方
            if (mFollowAdapter != null) {
                int position = mFollowAdapter.insertItem(bean);
                mFollowView.scrollToPosition(position);
            } else {
                List<ChatListBean> list = new ArrayList<>();
                list.add(bean);
                mFollowAdapter = createAdapter(list);
                mFollowView.setAdapter(mFollowAdapter);
                onNewUnReadMessageComing(bean);
            }

        } else {
            if (mNotFollowAdapter != null) {
                int position = mNotFollowAdapter.insertItem(bean);
                mNotFollowView.scrollToPosition(position);
            } else {
                List<ChatListBean> list = new ArrayList<>();
                list.add(bean);
                mNotFollowAdapter = createAdapter(list);
                mNotFollowView.setAdapter(mNotFollowAdapter);
                onNewUnReadMessageComing(bean);
            }
        }

    }


    //来消息时候更新item
    private void onNewMessageUpdate(ChatListBean bean) {
        boolean isFollow = "1".equals(bean.getUtot());
        if (isFollow) {//我已经关注对方
            mFollowAdapter.updateItem(bean, 1);
        } else {//我未关注对方
            mNotFollowAdapter.updateItem(bean, 1);
        }
        //onNewUnReadMessageComing(isFollow);
    }

    //返回的时候更新item
    private void onComeBackUpdateItem(ChatListBean bean, int unReadCount) {
        if ("0".equals(bean.getUtot())) {//我未关注对方
            if(mNotFollowAdapter!=null){
                mNotFollowAdapter.updateItem(bean, unReadCount);
            }
        } else if ("1".equals(bean.getUtot())) {//我已经关注对方
            if(mFollowAdapter!=null){
                mFollowAdapter.updateItem(bean, unReadCount);
            }
        }
    }

    //有新的未读消息来的时候执行
    private void onNewUnReadMessageComing(ChatListBean bean) {
        boolean isFollow = "1".equals(bean.getUtot());
        if (isFollow) {
            mFollowUnReadCount += bean.getUnReadCount();
        } else {
            mNotFollowUnReadCount += bean.getUnReadCount();
        }
        refreshUnReadPoint(isFollow);
    }

    //刷新未读消息的红点
    private void refreshUnReadPoint(boolean isFollow) {//是否关注了
        if (isFollow) {
            refreshFollowUnReadPoint();
        } else {
            refreshNotFollowUnReadPoint();
        }
        notifyChatStateObservers(mFollowUnReadCount, mNotFollowUnReadCount);
    }

    //刷新未关注的未读红点
    private void refreshNotFollowUnReadPoint() {
        if (!ignoreUnReadMessage) {
            if (mNotFollowUnReadCount > 0) {
                if (mNotFollowUnReadPoint.getVisibility() == View.GONE) {
                    mNotFollowUnReadPoint.setVisibility(View.VISIBLE);
                }
                mNotFollowUnReadPoint.setText(String.valueOf(mNotFollowUnReadCount));
            } else {
                if (mNotFollowUnReadPoint.getVisibility() == View.VISIBLE) {
                    mNotFollowUnReadPoint.setVisibility(View.GONE);
                }
            }
        }

    }

    //刷新已关注的未读红点
    private void refreshFollowUnReadPoint() {
        if (!ignoreUnReadMessage) {
            if (mFollowUnReadCount > 0) {
                if (mFollowUnReadPoint.getVisibility() == View.GONE) {
                    mFollowUnReadPoint.setVisibility(View.VISIBLE);
                }
                mFollowUnReadPoint.setText(String.valueOf(mFollowUnReadCount));
            } else {
                if (mFollowUnReadPoint.getVisibility() == View.VISIBLE) {
                    mFollowUnReadPoint.setVisibility(View.GONE);
                }
            }
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mMap != null && mUserMap.size() != mMap.size()) {
                requestAll();
            }
        }
    }

    public void onChatBack(ChatExitEvent e) {
        ChatListBean bean = mUserMap.get(e.getTouid());
        if (bean == null) {
            return;
        }
        int unReadCount = bean.getUnReadCount();
        bean.setUnReadCount(0);
        bean.setLastMessage(e.getLastMsg());
        onComeBackUpdateItem(bean, -unReadCount);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mMessageReceiver);
        if (mType == 0) {
            if(sChatStateObservers!=null){
                sChatStateObservers.clear();
                sChatStateObservers = null;
            }
        }
        EventBus.getDefault().unregister(this);
    }

    public static void addChatStateObserver(ChatStateObserver observer) {
        if (sChatStateObservers == null) {
            sChatStateObservers = new ArrayList<>();
        }
        sChatStateObservers.add(observer);
    }

    public static void removeChatStateObserver(ChatStateObserver observer) {
        if(sChatStateObservers!=null){
            sChatStateObservers.remove(observer);
        }
    }

    private void notifyChatStateObservers(int followUnReadCount, int notFollowUnReadCount) {
        if(sChatStateObservers==null){
            return;
        }
        for (ChatStateObserver observer : sChatStateObservers) {
            observer.onMessageCountChanged(followUnReadCount, notFollowUnReadCount);
        }
    }

    public static void notifyComeback(ChatExitEvent e) {
        if(sChatStateObservers==null){
            return;
        }
        for (ChatStateObserver observer : sChatStateObservers) {
            observer.onComeBack(e);
        }
    }

//    @Override
//    public void onResumeRefresh() {
//        if (mMap != null && mUserMap.size() != mMap.size()) {
//            requestAll();
//        }
//    }

    public interface ChatStateObserver {
        /**
         * 当从一个聊天页面返回来的的时候执行这个
         *
         * @param followUnReadCount    已关注的未读消息数量
         * @param notFollowUnReadCount 关注的未读消息数量
         */
        void onMessageCountChanged(int followUnReadCount, int notFollowUnReadCount);

        void onComeBack(ChatExitEvent e);

        void onIgnoreUnReadMessage();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAttentionChanged(AttentEvent e) {
        String uid = e.getUid();
        if (mUserMap.containsKey(uid)) {
            ChatListBean bean = mUserMap.get(uid);
            if (e.isAttention()) {
                mNotFollowAdapter.removeItem(uid);
                bean.setUtot("1");
                mFollowAdapter.insertItem(bean);
            } else {
                mFollowAdapter.removeItem(uid);
                bean.setUtot("0");
                mNotFollowAdapter.insertItem(mUserMap.get(uid));
            }
        }
    }

}
