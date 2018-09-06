package jan.jason.wanandroid.presenter.main;

import android.text.TextUtils;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.contract.main.RegisterContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.main.login.LoginData;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 注册页处理器
 * @Author: jasonjan
 * @Date: 2018/9/5 11:05
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter{

    /**
     * 数据中心
     */
    private DataManager mDataManager;

    /**
     * 注入活力
     * @param dataManager
     */
    @Inject
    public RegisterPresenter(DataManager dataManager){
        super(dataManager);
        mDataManager=dataManager;
    }

    /**
     * 逻辑方面，注册去请求后台
     * @param username
     * @param password
     * @param rePassword
     */
    @Override
    public void getRegisterData(String username,String password,String rePassword){
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(rePassword)) {
            mView.showSnackBar(WanAndroidApp.getInstance().getString(R.string.account_password_null_tint));
            return;
        }
        if (!password.equals(rePassword)) {
            mView.showSnackBar(WanAndroidApp.getInstance().getString(R.string.password_not_same));
            return;
        }
        addSubscribe(mDataManager.getRegisterData(username, password, rePassword)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .filter(loginResponse -> !TextUtils.isEmpty(username)
                        && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(rePassword))
                .subscribeWith(new BaseObserver<LoginData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.register_fail)) {
                    @Override
                    public void onNext(LoginData loginData) {
                        mView.showRegisterSuccess();
                    }
                }));
    }
}
