package jan.jason.wanandroid.test;

import android.support.test.espresso.IdlingResource;

/**
 * @Description: 测试异步类
 * @Author: jasonjan
 * @Date: 2018/8/27 20:56
 */
public class EspressoIdlingResource {

    /**
     * 资源字符
     */
    private static final String RESOURCE="GLOBAL";

    /**
     * 新建一个简单测试类
     */
    private static SimpleCountingIdlingResource mCountingIdlingResource=new SimpleCountingIdlingResource(RESOURCE);

    /**
     * 调用增加
     */
    public static void increment(){
        mCountingIdlingResource.increment();
    }

    /**
     * 调用减少
     */
    public static void decrement(){
        mCountingIdlingResource.decrement();
    }

    /**
     * 获取资源
     * @return
     */
    public static IdlingResource getIdlingResource(){
        return mCountingIdlingResource;
    }

}
