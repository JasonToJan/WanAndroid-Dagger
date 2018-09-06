package jan.jason.wanandroid.presenter.main;

import android.text.TextUtils;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.main.LoginContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.main.login.LoginData;
import jan.jason.wanandroid.core.event.LoginEvent;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 登录处理器
 * @Author: jasonjan
 * @Date: 2018/9/5 10:26
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter{

    /**
     * 数据中心
     */
    private DataManager mDataManager;

    /**
     * 注入生命力
     * @param dataManager
     */
    @Inject
    public LoginPresenter(DataManager dataManager){
        super(dataManager);
        mDataManager=dataManager;
    }

    /**
     * 数据方面，记住登录账号
     * @param account
     */
    @Override
    public void setLoginAccount(String account) {
        mDataManager.setLoginAccount(account);
    }

    /**
     * 数据方面，记住登录密码
     * @param password
     */
    @Override
    public void setLoginPassword(String password) {
        mDataManager.setLoginPassword(password);
    }

    /**
     * 逻辑方面，执行登录，传入账号和密码，最后UI显示登录成功
     * @param username
     * @param password
     */
    @Override
    public void getLoginData(String username,String password){
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            mView.showSnackBar(WanAndroidApp.getInstance().getString(R.string.account_password_null_tint));
            return;
        }
        addSubscribe(mDataManager.getLoginData(username, password)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<LoginData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.login_fail)) {
                    @Override
                    public void onNext(LoginData loginData) {
                        setLoginAccount(loginData.getUsername());
                        setLoginPassword(loginData.getPassword());
                        setLoginStatus(true);
                        RxBus.getDefault().post(new LoginEvent(true));
                        mView.showLoginSuccess();
                    }
                }));
    }
}
