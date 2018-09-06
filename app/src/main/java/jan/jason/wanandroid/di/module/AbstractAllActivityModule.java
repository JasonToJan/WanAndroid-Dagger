package jan.jason.wanandroid.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import jan.jason.wanandroid.di.component.BaseActivityComponent;
import jan.jason.wanandroid.ui.hierarchy.activity.KnowledgeHierarchyDetailActivity;
import jan.jason.wanandroid.ui.main.activity.AboutUsActivity;
import jan.jason.wanandroid.ui.main.activity.ArticleDetailActivity;
import jan.jason.wanandroid.ui.main.activity.LoginActivity;
import jan.jason.wanandroid.ui.main.activity.MainActivity;
import jan.jason.wanandroid.ui.main.activity.RegisterActivity;
import jan.jason.wanandroid.ui.main.activity.SearchListActivity;
import jan.jason.wanandroid.ui.main.activity.SplashActivity;

/**
 * @Description: 所有活动的抽象Module
 *           component类似于Activity,module类似于Activity中想使用的对象
 *
 * @Author: jasonjan
 * @Date: 2018/8/30 20:52
 */
@Module(subcomponents = BaseActivityComponent.class)
public abstract class AbstractAllActivityModule {

    /**
     * 给SplashActivity注入活力
     * @return
     */
    @ContributesAndroidInjector(modules = SplashActivityModule.class)
    abstract SplashActivity contributesSplashActivityInject();

    /**
     * 给MainActivity注入活力
     * @return
     */
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity contributesMainActivityInject();

    /**
     * 给登录页注入新鲜活力
     * @return
     */
    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity contributesLoginActivityInject();

    /**
     * 给注册页注入新鲜活力
     * @return
     */
    @ContributesAndroidInjector(modules = RegisterActivityModule.class)
    abstract RegisterActivity contributeRegisterActivityInject();

    /**
     * 给文章详情注入新鲜活力
     * @return
     */
    @ContributesAndroidInjector(modules = ArticleDetailActivityModule.class)
    abstract ArticleDetailActivity contributeArticleDetailActivityInject();

    /**
     * 给知识体系详情注入新鲜活力
     * @return
     */
    @ContributesAndroidInjector(modules = KnowledgeHierarchyDetailActivityModule.class)
    abstract KnowledgeHierarchyDetailActivity contributeKnowledgeHierarchyDetailActivityInject();

    /**
     * 给 关于我们 页面注入活力，其实不写也行，因为没有用到Inject注入活力
     * @return
     */
    @ContributesAndroidInjector(modules = AboutUsActivityModule.class)
    abstract AboutUsActivity contributeAboutUsActivityInject();

    /**
     * 生产一个SearchListActivity，注入活力
     * @return
     */
    @ContributesAndroidInjector(modules = SearchListActivityModule.class)
    abstract SearchListActivity contributeSearchListActivityInject();
}
