package track.com.ssevening.www.trackdemo;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pan on 2017/5/21.
 */

public class TrackOnClickListener implements View.OnClickListener {

    public Map<String, String> getTrackParams() {
        return new HashMap<>();
    }

    public String getClickName() {
        return "";
    }

    @Override
    public void onClick(View v) {
        MyApp.getApp().trackClick(getClickName(), getTrackParams());
        MyApp.getApp().trackSpm("1234", getPageName(), getSpmCString(), getSpmDString());
    }

    public String getPageName() {
        return "";
    }

    public String getSpmCString() {
        return "";
    }

    public String getSpmDString() {
        return "";
    }
}
