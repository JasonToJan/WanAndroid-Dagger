package jan.jason.wanandroid.di.component;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import jan.jason.wanandroid.base.fragment.BaseFragment;

/**
 * @Description: 定义抽象片段组件 使BaseFragment具有了生命力（可以注入）
 * @Author: jasonjan
 * @Date: 2018/9/4 10:33
 */
@Subcomponent(modules = {AndroidInjectionModule.class})
public interface BaseFragmentComponent extends AndroidInjector<BaseFragment>{

    /**
     * 每一个继承于BaseFragment的片段都继承于同一个子组件
     */
    @Subcomponent.Builder
    abstract class BaseBuilder extends AndroidInjector.Builder<BaseFragment>{

    }
}
