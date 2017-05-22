# TrackDemo

## 一、背景
在技术就是生产力，数据就是生产力的前景下，大数据，云计算越来越被人们所重视，所以做为客户端开发，我们也要认识一下数据统计都有哪此方式？以及在我们日常中开发的作用，方便后期近一步的完成产品经理给的需求，近一步了解用户。
总之一句话：通过在客户端埋点，可以得用户使用客户端的行为，并最终影响商业上的决策，为客户端后期发展提供数据支撑。

## 二、打点主要分为以下四类
1. 页面浏览量打点：即PV(Page View)打点，用于统计具体的页面被访问的次数，即访问一次页面，打一次点，比如在Activity 或 Fragment的onCreate中添加打点事件。
2. 控件点击打点：页面中某个按钮被点需的打点记录，为什么电商App：添加购物车和购买按钮的位置都是 购买按钮在右边呢？因通常我们是右边使用手机，那离拇指最近的按钮，决定着我们操作行为，是提高购买转化率的小妙招噢！
3. 用户量打点，即：使用App用户量，UV(User View)的意思，即使用过该App的独立设备数。基于此定义，衍生出：DAU(日活跃用户量)。
4. [SPM打点](https://www.biaodianfu.com/spm.html)，通常那些运营的H5页面，或通过RN或Weex搭建的活动页面，到底具体坑位的效果如何？又要如何调整，就用到了SPM打点了。