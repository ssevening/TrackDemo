package track.com.ssevening.www.trackdemo;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pan on 2017/5/21.
 */

public class TrackNamesCheck {

    /**
     * 检查页面名称是否有重复
     *
     * @param trackName
     * @return
     */
    public static boolean checkIfHaveMutiPageNames(Class trackClass) {
        List<String> pageList = new ArrayList<String>();

       /*
        * 得到类中的所有属性集合
        */
        Field[] fs = trackClass.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); //设置些属性是可以访问的
            Object val = null;//得到此属性的值
            try {
                val = f.get(trackClass);
                // L.d("name:" + f.getName() + "value = " + val.toString());
                String pageName = val.toString();
                if (pageName.length() > 19) {
                    pageName = pageName.substring(0, 19);
                }

                if (pageList.contains(pageName)) {
                    Log.d("muti", pageName);
                    return true;
                } else {
                    pageList.add(pageName);
                    Log.d("pageName", pageName);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
