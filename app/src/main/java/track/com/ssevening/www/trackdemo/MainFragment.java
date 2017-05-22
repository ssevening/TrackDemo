package track.com.ssevening.www.trackdemo;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pan on 2017/5/21.
 */

public class MainFragment extends BaseFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return PageNames.PAGE_MAIN;
    }
}
