package track.com.ssevening.www.trackdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.Map;

/**
 * Created by Pan on 2017/5/21.
 */

public class MyApp extends Application {
    public static String TAG = "MyApp";

    private static MyApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        // 打点测试ID
        trackDevice();
        app = this;

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                Log.v(TAG, activity.getClass().getName() + "onActivityStopped");
            }

            @Override
            public void onActivityStarted(Activity activity) {

                Log.v(TAG, activity.getClass().getName() + "onActivityStarted");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.v(TAG, activity.getClass().getName() + "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.v(TAG, activity.getClass().getName() + "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.v(TAG, activity.getClass().getName() + "onActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.v(TAG, activity.getClass().getName() + "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.v(TAG, activity.getClass().getName() + "onActivityCreated");
                if (activity instanceof Track) {
                    Track track = (Track) activity;
                    if (!TextUtils.isEmpty(track.getPageName())) {
                        trackPage(track.getPageName(), track.getTrackParam());
                    }
                }

            }
        });
    }

    public static MyApp getApp() {
        return app;
    }

    public static void trackEvent(String eventName) {
        Log.d(TAG, "track event: " + eventName);
    }


    /**
     * 比如 TrackDemo的 首页 产品点击区 产品ID点击
     *
     * @param a AppID
     * @param b 页面ID
     * @param c 区块ID
     * @param d 控件ID
     */
    public void trackSpm(String a, String b, String c, String d) {
        Log.d(TAG, "track spm: " + a + "." + b + "." + c + "." + d);
    }

    public void trackDevice() {
        Log.d(TAG, "track deviceId: " + Installation.id(this));
    }

    public void trackPage(String pageName, Map<String, String> params) {
        Log.d(TAG, "track page: " + pageName);
    }

    public void trackClick(String click, Map<String, String> params) {
        Log.d(TAG, "track click: " + click);
    }
}
