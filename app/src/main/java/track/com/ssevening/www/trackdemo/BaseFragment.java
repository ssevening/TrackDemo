package track.com.ssevening.www.trackdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pan on 2017/5/21.
 */

public class BaseFragment extends Fragment implements Track {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(getPageName())) {
            MyApp.getApp().trackPage(getPageName(), getTrackParam());
        }
    }


    /**
     * 业务页面实现需要覆盖此方法
     *
     * @return
     */
    public Map<String, String> getTrackParam() {
        return new HashMap<>();
    }


    /**
     * 返回页面名称，当为空时，代表此页面不执行打点
     *
     * @return
     */
    @Override
    public String getPageName() {
        return "";
    }

}
