package com.duomizhibo.phonelive.event;

/**
 * Created by weipeng on 2017/1/20.
 */

public class Event {

    public static class DialogEvent {
        public int action;
    }

    public static class VideoEvent{
        public String[] data;
        public int action;
    }

    public static class PrivateChatEvent{
        public int action;
    }

    public static class CommonEvent{
        public int action;
    }
    public static class DeleteEvent{
        public int action;
    }
}
