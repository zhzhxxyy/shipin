package cn.jzvd;

import android.widget.SeekBar;

public interface JzInterface {

   //返回按钮缩小时候是否显示
   public boolean isShowBack();

   //播放缩放时候点击返回按钮事件
   public boolean clickBackPress();

   //点击播放
   public boolean clickThumbPre();

   //播放进度监听
   public void onProgressChanged(SeekBar seekBar, int progress, boolean bFromUser);

}
