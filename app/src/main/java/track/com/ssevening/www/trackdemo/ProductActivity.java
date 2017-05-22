package track.com.ssevening.www.trackdemo;

import android.os.Bundle;

/**
 * Created by Pan on 2017/5/21.
 */

public class ProductActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public String getPageName() {
        return PageNames.PAGE_MAIN;
    }
}
