package jan.jason.wanandroid.contract.main;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;

/**
 * @Description: 启动页处理器辅助接口
 * @Author: jasonjan
 * @Date: 2018/8/29 7:49
 */
public interface SplashContract {

    interface View extends AbstractView {

        /**
         * 界面跳转到主页面
         */
        void jumpToMain();
    }

    interface Presenter extends AbstractPresenter<View>{

    }
}
