package jan.jason.wanandroid.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import jan.jason.wanandroid.di.component.BaseFragmentComponent;
import jan.jason.wanandroid.ui.hierarchy.fragment.KnowledgeHierarchyFragment;
import jan.jason.wanandroid.ui.mainpager.fragment.MainPagerFragment;
import jan.jason.wanandroid.ui.navigation.fragment.NavigationFragment;
import jan.jason.wanandroid.ui.project.fragment.ProjectFragment;
import jan.jason.wanandroid.ui.project.fragment.ProjectListFragment;

/**
 * @Description: 管理所有基类片段的Module 注入活力
 * @Author: jasonjan
 * @Date: 2018/9/4 10:31
 */
@Module(subcomponents = BaseFragmentComponent.class)
public abstract class AbstractAllFragmentModule {

    /**
     * 注入MainPagerFragmentModule，使其具有生命力
     * @return
     */
    @ContributesAndroidInjector(modules = MainPagerFragmentModule.class)
    abstract MainPagerFragment contributesMainPagerFragmentInject();

    /**
     * 注入KnowledgeFragmentModule，使其具有生命力
     * @return
     */
    @ContributesAndroidInjector(modules = KnowledgeFragmentModule.class)
    abstract KnowledgeHierarchyFragment contributesKnowledgeHierarchyFragmentInject();

    /**
     * 注入NavigationFragmentModule，使其具有生命力
     * @return
     */
    @ContributesAndroidInjector(modules = NavigationFragmentModule.class)
    abstract NavigationFragment contributesNavigationFragmentInject();

    /**
     * 生产一个ProjectFragment实例，使其具有生命力
     * @return
     */
    @ContributesAndroidInjector(modules = ProjectFragmentModule.class)
    abstract ProjectFragment contributesProjectFragmentInject();

    /**
     * 生产一个ProjectListFragment实例，使其具有生命力
     * @return
     */
    @ContributesAndroidInjector(modules = ProjectListFragmentModule.class)
    abstract ProjectListFragment contributesProjectListFragmentInject();

}
