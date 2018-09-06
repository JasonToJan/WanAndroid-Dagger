package jan.jason.wanandroid.contract.main;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;

/**
 * @Description: 注册 视图和处理器关联接口
 * @Author: jasonjan
 * @Date: 2018/9/5 10:53
 */
public interface RegisterContract {

    interface View extends AbstractView{

        /**
         * UI方面，展示注册成功
         */
        void showRegisterSuccess();
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 逻辑方面，执行注册，请求后台
         * @param username
         * @param password
         * @param rePassword
         */
        void getRegisterData(String username,String password,String rePassword);
    }
}
