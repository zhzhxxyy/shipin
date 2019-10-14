package com.helloworld;

import android.app.Application;


import com.facebook.react.ReactApplication;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jiguang.plugins.push.JPushPackage;
import cn.jpush.reactnativejanalytics.JAnalyticsPackage;
import com.github.yamill.orientation.OrientationPackage;
import com.helloworld.reactpackage.AppModule;
import com.learnium.RNDeviceInfo.RNDeviceInfo;
import com.brentvatne.react.ReactVideoPackage;

import org.devio.rn.splashscreen.SplashScreenReactPackage;
import com.microsoft.codepush.react.CodePush;

import com.swmansion.gesturehandler.react.RNGestureHandlerPackage;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.List;

import com.helloworld.reactpackage.AppReactPackage;


public class MainApplication extends Application implements ReactApplication {


  private boolean SHUTDOWN_TOAST = true;
  private boolean SHUTDOWN_LOG = true;

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

        @Override
        protected String getJSBundleFile() {
        return CodePush.getJSBundleFile();
        }
    
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
              new CodePush(BuildConfig.CODEPUSH_KEY, MainApplication.this, BuildConfig.DEBUG),
            new MainReactPackage(),
            new JAnalyticsPackage(SHUTDOWN_TOAST,SHUTDOWN_LOG),
              new AppReactPackage(),
            new OrientationPackage(),
            new RNDeviceInfo(),
            new ReactVideoPackage(),
            new SplashScreenReactPackage(), // Add/change this line.

            new RNGestureHandlerPackage(),
             new JPushPackage()


      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

//  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    // 在 Init 之前调用，设置为 true，则会打印 debug 级别日志，否则只会打印 warning 级别以上的日志
    // JAnalyticsInterface.setDebugMode(true);
    JAnalyticsInterface.init(this);
  }
}
