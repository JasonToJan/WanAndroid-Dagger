package jan.jason.wanandroid.contract.main;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;

/**
 * @Description: 登录 视图和处理器关联接口
 * @Author: jasonjan
 * @Date: 2018/9/5 10:22
 */
public interface LoginContract {

    interface View extends AbstractView{

        /**
         * UI方面，展示登录成功
         */
        void showLoginSuccess();
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 数据方面，设置登录账号，记住
         * @param account
         */
        void setLoginAccount(String account);

        /**
         * 数据方面，设置登录密码，记住
         * @param password
         */
        void setLoginPassword(String password);

        /**
         * 数据方面，获取登录数据，传入用户名和密码，但不返回
         * @param username
         * @param password
         */
        void getLoginData(String username,String password);
    }
}
