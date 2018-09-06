package jan.jason.wanandroid.presenter.main;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.main.MainContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.event.AutoLoginEvent;
import jan.jason.wanandroid.core.event.LoginEvent;
import jan.jason.wanandroid.core.event.NightModeEvent;
import jan.jason.wanandroid.core.event.SwitchNavigationEvent;
import jan.jason.wanandroid.core.event.SwitchProjectEvent;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseSubscribe;

/**
 * @Description: 主页的处理器
 * @Author: jasonjan
 * @Date: 2018/9/3 11:30
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter{

    /**
     * 数据管理员
     */
    private DataManager mDataManager;

    @Inject
    MainPresenter(DataManager dataManager){
        super(dataManager);
        this.mDataManager=dataManager;
    }

    @Override
    public void attachView(MainContract.View view){
        super.attachView(view);
        registerEvent();
    }

    /**
     * 注册事件，定义如果接收到别人发送的事件后怎么做的逻辑
     */
    private void registerEvent(){


        //添加事件1-登录事件
        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .filter(LoginEvent::isLogin)
                .subscribe(loginEvent -> mView.showLoginView()));
        //添加事件2-退出登录事件
        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .filter(loginEvent -> !loginEvent.isLogin())
                .subscribe(loginEvent -> mView.showLogoutView()));

        //添加事件3-自动登录
        addSubscribe(RxBus.getDefault().toFlowable(AutoLoginEvent.class)
                .subscribe(autoLoginEvent -> mView.showAutoLoginView()));

        //添加事件4-选择项目
        addSubscribe(RxBus.getDefault().toFlowable(SwitchProjectEvent.class)
                .subscribe(switchProjectEvent -> mView.showSwitchProject()));

        //添加事件5-选择导航
        addSubscribe(RxBus.getDefault().toFlowable(SwitchNavigationEvent.class)
                .subscribe(switchNavigationEvent -> mView.showSwitchNavigation()));

        //添加事件6-选择夜间模式
        addSubscribe(RxBus.getDefault().toFlowable(NightModeEvent.class)
                .compose(RxUtils.rxFlSchedulerHelper())
                .map(NightModeEvent::isNightMode)
                .subscribeWith(new BaseSubscribe<Boolean>(mView, WanAndroidApp.getInstance().getString(R.string.failed_to_cast_mode)) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.useNightMode(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        registerEvent();
                    }
                })
        );
    }

    /**
     * 设置当前页码
     * @param page
     */
    @Override
    public void setCurrentPage(int page){
        mDataManager.setCurrentPage(page);
    }

    /**
     * 设置页面模式
     * @param b
     */
    public void setNightModeState(boolean b){
        mDataManager.setNightModeState(b);
    }

}
