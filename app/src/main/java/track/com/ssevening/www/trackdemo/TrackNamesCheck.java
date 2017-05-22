package track.com.ssevening.www.trackdemo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pan on 2017/5/21.
 */

public class TrackNamesCheck {

    /**
     * 检查页面名称是否有重复
     * @param trackName
     * @return
     */
    public static boolean checkIfHaveMutiPageName(PageNames trackName) {
        Class userCla = (Class) trackName.getClass();
        List<String> pageList = new ArrayList<String>();

       /*
        * 得到类中的所有属性集合
        */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); //设置些属性是可以访问的
            Object val = null;//得到此属性的值
            try {
                val = f.get(trackName);
                // L.d("name:" + f.getName() + "value = " + val.toString());
                String pageName = val.toString();
                if (pageList.contains(pageName)) {
                    return true;
                } else {
                    pageList.add(pageName);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
