package jan.jason.wanandroid.presenter.main;

import javax.inject.Inject;

import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.main.MainContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.event.CollectEvent;
import jan.jason.wanandroid.core.event.LoginEvent;

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
     * 注册事件
     */
    private void registerEvent(){
        //添加事件1-收集
       addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(collectEvent -> !collectEvent.isCancelCollectSuccess())
                .subscribe(collectEvent -> mView.showCollectSuccess()));
       //添加事件2-取消收集
       addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(CollectEvent::isCancelCollectSuccess)
                .subscribe(collectEvent -> mView.showCancelCollectSuccess()));
        //添加事件3-登录事件
        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .filter(LoginEvent::isLogin)
                .subscribe(loginEvent -> mView.showLoginView()));
        //添加事件4-退出登录事件
        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .filter(loginEvent -> !loginEvent.isLogin())
                .subscribe(loginEvent -> mView.showLogoutView()));

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
