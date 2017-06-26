package track.com.ssevening.www.trackdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by Pan on 2017/5/21.
 */

public class ProductActivity extends BaseActivity {
    Random random = new Random(System.currentTimeMillis());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ImageView iv_product_image = (ImageView) findViewById(R.id.iv_product_image);
        // 随机设置一个图片,这样我们这三个图片要用到，不应该被清理掉
        int next = random.nextInt(2);
        int resID = getResources().getIdentifier("ic_" + next, "drawable", MyApp.getApp().getPackageName());
        iv_product_image.setImageResource(resID);

//        int allResID = getResources().getIdentifier("t_all", "drawable", MyApp.getApp().getPackageName());
//        iv_product_image.setImageResource(allResID);
    }

    @Override
    public String getPageName() {
        return PageNames.TRACK_PAGE_HOME;
    }
}
