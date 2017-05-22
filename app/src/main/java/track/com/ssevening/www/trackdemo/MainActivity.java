package track.com.ssevening.www.trackdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {


    private TextView tv_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_product = (TextView) findViewById(R.id.tv_product);

        final ProductInfo productInfo = new ProductInfo();
        productInfo.channelName = "团购";
        productInfo.spm = "1234";
        productInfo.items = new ArrayList<>();
        ProductInfo.ItemInfo item = new ProductInfo.ItemInfo();
        item.id = "1";
        item.title = "苹果手机";
        item.url = "http://www.example.com";
        item.spm = "1";
        productInfo.items.add(item);

        // 控件点击和SPM打点
        tv_product.setOnClickListener(new TrackOnClickListener() {
            public Map<String, String> getTrackParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("productId", "1234567");
                return map;
            }

            public String getClickName() {
                return ControlNames.control_product_click;
            }

            @Override
            public void onClick(View v) {
                super.onClick(v);
                Intent i = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(i);
            }

            @Override
            // SPM 页面名称
            public String getPageName() {
                return getPageName();
            }

            @Override
            // SPM C字段
            public String getSpmCString() {
                return productInfo.spm;
            }

            @Override
            // SPM D字段
            public String getSpmDString() {
                return productInfo.items.get(0).spm;
            }

        });

        // 这种是不推荐的打点方式，因为每个打点都要自己去实现，开发者关心了打点
//        tv_product.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("productId", "1234567");
//                MyApp.getApp().trackClick(ControlNames.control_product_click, map);
//                Intent i = new Intent(MainActivity.this, ProductActivity.class);
//                startActivity(i);
//            }
//        });

        if (TrackNamesCheck.checkIfHaveMutiPageName(new PageNames())) {
            Toast.makeText(this, "有重复的页面名称，请检查！！！", Toast.LENGTH_LONG).show();
            // 重复的页面名称，属于错误，打自定义打点
            MyApp.trackEvent("MutiPageName");
            finish();
        }


    }

    @Override
    public String getPageName() {
        return PageNames.PAGE_MAIN;
    }
}
