package jan.jason.wanandroid.di.component;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import jan.jason.wanandroid.base.fragment.BaseDialogFragment;

/**
 * @Description: 对话框类型的碎片 基类组件定义
 * @Author: jasonjan
 * @Date: 2018/9/4 21:26
 */
@Subcomponent(modules = {AndroidInjectionModule.class})
public interface BaseDialogFragmentComponent extends AndroidInjector<BaseDialogFragment>{

    /**
     * 每一个继承于BaseDialogFragment的Fragment都继承于同一个子组件
     */
    @Subcomponent.Builder
    abstract class BaseBuilder extends AndroidInjector.Builder<BaseDialogFragment>{

    }
}
