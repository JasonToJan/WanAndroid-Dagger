package jan.jason.wanandroid.test;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 简单的测试类
 * @Author: jasonjan
 * @Date: 2018/8/27 20:56
 */
public class SimpleCountingIdlingResource implements IdlingResource{

    /**
     * 资源名字
     */
    private final String mResourceName;

    /**
     * 统计标记位
     */
    private final AtomicInteger counter=new AtomicInteger(0);

    /**
     * 资源回调类
     */
    private volatile ResourceCallback resourceCallback;

    public SimpleCountingIdlingResource(String resourceName){
        mResourceName=resourceName;
    }

    /**
     * 获取资源名字
     * @return
     */
    public String getName(){
        return mResourceName;
    }

    /**
     * 标记位是否为0
     * @return
     */
    public boolean isIdleNow(){
        return counter.get()==0;
    }

    /**
     * 设置回调
     * @param resourceCallback
     */
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback){
        this.resourceCallback=resourceCallback;
    }

    /**
     * 执行异步请求，counter值+1
     */
    public void increment(){
        counter.getAndIncrement();
    }

    /**
     * 获取到网络数据后，counter值-1
     */
    public void decrement(){
        int counterVal=counter.decrementAndGet();
        if(counterVal==0){
            if(null!=resourceCallback){
                resourceCallback.onTransitionToIdle();//异步结束，执行回调
            }
        }
        if(counterVal<0){
            throw new IllegalArgumentException("Counter has bean corrupted!");//抛出异常
        }
    }
}
