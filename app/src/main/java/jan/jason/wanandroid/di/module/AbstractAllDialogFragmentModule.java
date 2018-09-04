package jan.jason.wanandroid.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import jan.jason.wanandroid.di.component.BaseDialogFragmentComponent;
import jan.jason.wanandroid.ui.main.fragment.SearchDialogFragment;
import jan.jason.wanandroid.ui.main.fragment.UsageDialogFragment;

/**
 * @Description: 抽象类，所有对话框类型的片段都在这里注入生命力
 * @Author: jasonjan
 * @Date: 2018/9/4 21:29
 */
@Module(subcomponents = BaseDialogFragmentComponent.class)
public abstract class AbstractAllDialogFragmentModule {

    /**
     * 给常用网站注入活力
     * @return
     */
    @ContributesAndroidInjector(modules = UsageDialogFragmentModule.class)
    abstract UsageDialogFragment contributesUsageDialogFragmentInject();

    /**
     * 给搜索页面注入活力
     * @return
     */
    @ContributesAndroidInjector(modules = SearchDialogFragmentModule.class)
    abstract SearchDialogFragment contributesSearchDialogFragmentInject();
}
