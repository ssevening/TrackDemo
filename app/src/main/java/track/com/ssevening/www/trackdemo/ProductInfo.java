package track.com.ssevening.www.trackdemo;

import java.util.List;

/**
 * Created by Pan on 2017/5/22.
 */

public class ProductInfo {
    public String channelName;
    public String spm;
    public List<ItemInfo> items;

    public static class ItemInfo {
        public String id;
        public String title;
        public String url;
        public String spm;
    }
}
