package jan.jason.wanandroid.di.component;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import jan.jason.wanandroid.base.activity.BaseActivity;

/**
 * @Description: 基类组件 供dagger使用
 *      component类似于Activity,module类似于Activity中想使用的对象
 *
 * @Author: jasonjan
 * @Date: 2018/8/30 20:47
 */
@Subcomponent(modules = {AndroidInjectionModule.class})
public interface BaseActivityComponent extends AndroidInjector<BaseActivity> {

    /**
     * 每一个继承于BaseActivity的Activity都继承于同一个子组件
     */
    @Subcomponent.Builder
    abstract class BaseBuilder extends AndroidInjector.Builder<BaseActivity>{

    }
}
