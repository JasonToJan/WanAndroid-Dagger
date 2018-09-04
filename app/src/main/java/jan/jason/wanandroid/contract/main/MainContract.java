package jan.jason.wanandroid.contract.main;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;

/**
 * @Description: 主页视图帮助接口
 * @Author: jasonjan
 * @Date: 2018/9/3 11:25
 */
public interface MainContract {

    interface View extends AbstractView{

        /**
         * 展示选择的项目
         */
        void showSwitchProject();

        /**
         * 展示选择的导航栏目
         */
        void showSwitchNavigation();

        /**
         * 展示自动登录视图
         */
        void showAutoLoginView();
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 设置当前页码
         * @param page
         */
        void setCurrentPage(int page);

        /**
         * 设置夜间模式
         * @param b
         */
        void setNightModeState(boolean b);
    }
}
