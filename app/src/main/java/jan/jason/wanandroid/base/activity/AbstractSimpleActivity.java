package jan.jason.wanandroid.base.activity;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jan.jason.wanandroid.component.ActivityCollector;
import jan.jason.wanandroid.test.EspressoIdlingResource;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * @Description: 抽象最简单的活动,继承了一个解决官方bug的一个牛逼类
 * @Author: jasonjan
 * @Date: 2018/8/27 20:22
 */
public abstract class AbstractSimpleActivity extends SupportActivity{

    /**
     * 存放一个butterKnife需要使用的对象
     */
    private Unbinder unbinder;

    /**
     * 存放一个本类实例
     */
    protected AbstractSimpleActivity mActivity;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(getLayoutId());//加载视图
        unbinder= ButterKnife.bind(this);//将本活动绑定黄油刀
        mActivity=this;
        ActivityCollector.getInstance().addActivity(this);//收集活动
        onViewCreated();//视图创建完成


        initEventAndData();//初始化事件和一些数据
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.getInstance().removeActivity(this);
        if(unbinder!=null&&unbinder!=Unbinder.EMPTY){
            unbinder.unbind();//解绑
            unbinder=null;
        }
    }

    /**
     * 获取一个测试资源类
     * @return
     */
    @VisibleForTesting
    public IdlingResource getCountingIdlingResource(){
        return EspressoIdlingResource.getIdlingResource();
    }

    /**
     * 获取当前Activity的布局id
     */
    protected abstract int getLayoutId();

    /**
     * 率先创建视图
     */
    protected abstract void onViewCreated();

    /**
     * 初始化标题栏
     */
    protected abstract void initToolbar();

    /**
     * 初始化事件和数据
     */
    protected abstract void initEventAndData();

}
