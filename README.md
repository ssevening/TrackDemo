# TrackDemo

## 一、背景
在技术就是生产力，数据就是生产力的前景下，大数据，云计算越来越被人们所重视，所以做为客户端开发，我们也要认识一下数据统计都有哪此方式？以及在我们日常中开发的作用，方便后期近一步的完成产品经理给的需求，近一步了解用户。
总之一句话：通过在客户端埋点，可以得用户使用客户端的行为，并最终影响商业上的决策，为客户端后期发展提供数据支撑。
首先来一张概览图
和GitHub地址：[TrackDemo](https://github.com/ssevening/TrackDemo)

![打点概览图](https://ssevening.github.io/assets/track/1_track.png)

## 二、打点主要分为以下四类
1. 页面浏览量打点：即PV(Page View)打点，用于统计具体的页面被访问的次数，即访问一次页面，打一次点，比如在Activity 或 Fragment的onCreate中添加打点事件。
2. 控件点击打点：页面中某个按钮被点需的打点记录，为什么电商App：添加购物车和购买按钮的位置都是 购买按钮在右边呢？因通常我们是右边使用手机，那离拇指最近的按钮，决定着我们操作行为，是提高购买转化率的小妙招噢！
3. 用户量打点，即：使用App用户量，UV(User View)的意思，即使用过该App的独立设备数。基于此定义，衍生出：DAU(日活跃用户量)。
4. [SPM打点](https://www.biaodianfu.com/spm.html)，通常那些运营的H5页面，或通过RN或Weex搭建的活动页面，到底具体坑位的效果如何？又要如何调整，就用到了SPM打点了。
5. 自定义打点、开发自用，一般用于排查故障。

## 三、页面打点的实现方式和注意事项
* 先上数据演示图：

![页面打点演示图](https://ssevening.github.io/assets/track/2_pageview.png)

* 页面浏览量打点，我们必须做到以下几点：
  * Activity的 onCreate中都要添加打点代码。
  * Fragment页面的 onCreate中都添加打点代码。
  * 代码复用度高，对开发友好。
  * 重复页面名称的检查功能，免得A和B定义了同样的PageName，导致线上数据出错。

针对上述问题，我们来各各击破： 

|功能列表|推荐实现方案|普通实现方案|
|---|----|-----|
|Activity onCreate添加页面事件打点|通过ActivityLifecycleCallbacks动态实现|继承或每个Ac中添加打点代码|
|Frag onCreate添加页面事件打点|继承的BaseFragment中实现打点|每个Fragment自已实现|
|代码复用度高|按如上推荐方案，只需要实现页面名称和参数即可|每个页面都要写同样的代码，哪天替换打点方案时，涉及页面众多|
|页面名称重复命名的问题|通过反射查出相同字符串并报错|人为肉眼保证| 

所以综上所述的实现代码如下：

* Activity的实现如下,通过ActivityLifecycleCallbacks 实现Activity的页面打点。

```
public class MyApp extends Application {
    public static String TAG = "MyApp";

    private static MyApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                Log.v(TAG, activity.getClass().getName() + "onActivityStopped");
            }

            @Override
            public void onActivityStarted(Activity activity) {

                Log.v(TAG, activity.getClass().getName() + "onActivityStarted");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.v(TAG, activity.getClass().getName() + "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.v(TAG, activity.getClass().getName() + "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.v(TAG, activity.getClass().getName() + "onActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.v(TAG, activity.getClass().getName() + "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.v(TAG, activity.getClass().getName() + "onActivityCreated");
                if (activity instanceof Track) {
                    Track track = (Track) activity;
                    if (!TextUtils.isEmpty(track.getPageName())) {
                        trackPage(track.getPageName(), track.getTrackParam());
                    }
                }

            }
        });
    }

    public static MyApp getApp() {

        return app;
    }

    public void trackPage(String pageName, Map<String, String> params) {
        Log.d(TAG, "track page: " + pageName);
    }

    public void trackClick(String click, Map<String, String> params) {
        Log.d(TAG, "track click: " + click);
    }
}

```

* Fragment的页面打点实现如下：

```
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

具体的业务类的实现就可以这样：

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

```

* 按照上方的代码实现后，就直接到最后一个问题：编写如下反射代码，查找是否有重复的页面命名。

```

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

```


## 四、控件打点的实现方案
* 上一个概览图

![打点概览图](https://ssevening.github.io/assets/track/3_control_click.png)

* 所谓的控件打点，就是指：点击，长按，选中之类的一系列动作，都要进行打点。简单的做法，在每一个具体的实现方案中打点：形如下面代码

```
// 实现自己的TrackOnClickListener,这样就只要填上控件名称和参数就可以了
tv_product.setOnClickListener(new TrackOnClickListener() {
            public Map<String, String> getTrackParams() {
                HashMap<String, String> map = new HashMap<String, String>();
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
        });
        
// 打点代码的具体实现如下：

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
    }
}

通过这种方式，我们再实现自己的长按和选中，在使用的时候，使用Track相关的Lister，就可以实现打点和业务的解耦了。        

```


## 五、用户量的打点
* 用户量概览图

![打点概览图](https://ssevening.github.io/assets/track/4_dau.png)

关于用户量打点，这里最需要关注的就是：我们怎么样去标识一个唯一设备或唯一用户。初步的想法肯定如下：

* 拿手机号，不就行了？ 【可是如果没有SIM卡呢？或者平板设备本来就没有SIM卡】
* 拿用户登陆用户名啊！ 【可是如果用户没有在登陆状态怎么办？】
* 拿用户手机的IMEI串号啊？ 【可是6.0去行时权限，拿IMEI需要权限】
* 那运行的时候，自动生成一个ID，后面直接用这个ID 【想法不错，但如果用户刷机呢？一切荡然无存，又会被计算成一个新用户】

这个时候，是不是想骂娘了？ What can i do? 没想到一个小小的ID都有这么多事！去搜搜看吧。



|Identifier	|Example Value	|Permission Required | 
|---|----|-----|
|Android ID via Settings.Secure|	2fc4b5912826ad1 | NONE|
|Android Build.SERIAL	|HT6C90202028|	NONE|
|Android Build.MODEL	|Pixel XL	|NONE|
|Android Build.BRAND	|google	|NONE|
|Android Build.MANUFACTURER|	Google	|NONE|
|Android Build.DEVICE	|marlin	|NONE|
|Android Build.PRODUCT	|marlin	|NONE|
|IMEI	|352698276144152	|READ_PHONE_STATE|
|Phone Number	 |2028675309|	READ_PHONE_STATE or READ_SMS|
|ICCID (Sim Serial Number)	|311477629513071|READ_PHONE_STATE|

上图表格来自于：[identifying-an-android-device](http://handstandsam.com/2017/05/04/identifying-an-android-device/?utm_source=Android+Weekly&utm_campaign=1a4899612b-android-weekly-257&utm_medium=email&utm_term=0_4eb677ad19-1a4899612b-337950077)

在上面的文章中，介绍到了一种ID方法：[Identifying App Installations](https://android-developers.googleblog.com/2011/03/identifying-app-installations.html)

即安装每个App安装的时候，获得一个ID, 实现代码如下：

```
public class Installation {
    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String id(Context context) {
        if (sID == null) {  
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}

第一次安装数据：
2ac11f3e-1d0a-4a05-b940-a71343b241de
清除数据后数据：
0862445a-73d3-40a2-a145-e696078347f2
卸载后安装数据：
5794bd73-0ab9-430e-ad92-e3e77b26bbed
杀掉进程后：
5794bd73-0ab9-430e-ad92-e3e77b26bbed

所以上面的方式，只针对安装有效，生命周期更多是在安装上，如此的话，我们可以增加附着条件：

拿：Android ID via Settings.Secure 这个只有刷机有效，拿IMEI，这个怎么刷机都无效，
再加上UUID的方式，增加相应的因子后组成：AndroidId_imei_UUID这样的前缀，
然后落实到打点记录中，任意一个ID相同，便认为是一个设备，相应的数据就会准确很多。

```

搞定上面的ID以后，就是服务端统计计数的事情了。
当然，如果你是一名黑客，或者是一个广告开发者，那对于那种新系统，或平台奖励，是按设备ID来计算的，你就知道，嘿嘿嘿，你要做的就是去猜测改变相应的因子了，我只说这么多了。



## 六、SPM打点

SPM打点是淘宝引入的一套新型打点：详细内容点击：[SPM打点](https://www.biaodianfu.com/spm.html)
我的理解如下：
* 和上面控件打点相关，控件打点主要用于具体的业务页面，比如产品页的购买按钮，下单页面购买按钮等，但SPM再多是活动页面，由服务端下发，客户端进行动态缓制的页面。比如双十一的主分会场，App中会有很多网格（坑位），然后需要计算每个坑位的点击、转化、热度。

然后SPM涉及到四个字符如下：
A：App端
B：具体页面
C：频道ID，比如：团购、女装促销等频道。
D：具体的点击ID。

比如点击了 A位:淘宝App的 B位:双十一会场页面的 C位:团购 中的 D位:小米平衡车，那打点数据就是：
1234.5678.1.1
如果点击了 A位:淘宝App的 B位:双十一会场页面的 C位：团购 中的 D位：苹果手机，那打点数据就是：
1234.5678.1.1
如果点击了 A位：淘宝App的 B位：双十一会场页面的 C位：秒杀 中的 D位：小米手机，那打点数据就是：
1234.5678.2.2

其中 ABCD位，分别用 . 分隔。后期就可以制作出如下表格：

|团购频道(100)	|苹果手机(80)	|小米平衡车(20) | 
|---|----|-----|
|秒杀频道(200)|	尿不湿(50) | 华为P8(150)|
|XX频道(3000)	|xxx(1000)|	xxx(2000)|

这样，很清晰的知道，看来 苹果手机 和华为P8 很受欢迎啊！下次要不要再搞个华为P8的专场啊？用户肯定感兴趣，所谓数据驱动商业，就是这样产生的。

* 那SPM打点客户端要做什么呢？

  * A，B AppID 和 PageName 我们轻轻松松可以拿到。
  * 那频道名称和具体产品ID要怎么得到呢？方案是让服务端下发！不要总想着什么都由客户端自己去实现，转变一下思路。服务端下发数据如下：

```
{
     "ChannelName": "团购",
    "spm": "12345678",
    "items": [
        {
            "id": "1",
            "title": " 苹果手机",
            "url": "http://www.example.com",
            "spm": "1"
        }，{
            "id": "2",
            "title": "  小米平衡车",
            "url": "http://www.example.com",
            "spm": "1"
        }
    ]
}

那客户端在获取到服务端数据后，然后针对性添加SPM打点如下：

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
```


## 七、最后再说一下自定义打点

* 自定义打点，更多的是开发自己排查问题使用，比如下面的场景
   * 【耗时操作成功率统计】用户上传视频，有多少上传行为，有多少上传失败行为？
   * 【关键业务成功率统计】用户下单，一天成功了多少笔？失败了多少笔？失败的具体原因分析？
   * 【分类错误分析场景】 图片下载显示，因为DNS失败多少个？因为服务器超时又有多少次？
   * 【单点错误问题排查】 比如json解析、raw文件读取之类的。
