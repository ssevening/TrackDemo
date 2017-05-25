package track.com.ssevening.www.trackdemo;

import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pan on 2017/5/21.
 */

public class BaseActivity extends AppCompatActivity implements Track {

    @Override
    public String getPageName() {
        return "";
    }

    /**
     * 实现自定义参数请覆盖此方法
     *
     * @return
     */
    @Override
    public Map<String, String> getTrackParam() {

        return new HashMap<>();
    }
}

