package jan.jason.wanandroid.base.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.DataManager;

/**
 * @Description: 处理器基类，管理事件流订阅的生命周期
 *              帮忙实现了抽象处理器中定义的许多方法
 * @Author: jasonjan
 * @Date: 2018/8/28 7:53
 */
public class BasePresenter<T extends AbstractView> implements AbstractPresenter<T> {

    /**
     * 需要用到的抽象视图管理类
     */
    protected T mView;

    /**
     * 事件流订阅管理类
     */
    private CompositeDisposable compositeDisposable;

    /**
     * 数据管理类
     */
    private DataManager mDataManager;

    /**
     * 构造函数，传入一个数据管理类，方便填充数据
     * @param dataManager
     */
    public BasePresenter(DataManager dataManager){
        this.mDataManager=dataManager;
    }

    /**
     * 真正注入视图
     * @param view
     */
    @Override
    public void attachView(T view){
        this.mView=view;
    }

    /**
     * 回收视图引用，避免内存泄漏
     */
    @Override
    public void detachView(){
        this.mView=null;
        if(compositeDisposable!=null){
            compositeDisposable.clear();
        }
    }

    /**
     * 添加一个订阅事件
     * @param disposable
     */
    @Override
    public void addRxBindingSubscribe(Disposable disposable){
        addSubscribe(disposable);
    }

    /**
     * 获取是否处于夜间模式
     * @return
     */
    @Override
    public boolean getNightModeState() {
        return mDataManager.getNightModeState();
    }

    /**
     * 设置是否处于登录状态
     * @param loginStatus
     */
    @Override
    public void setLoginStatus(boolean loginStatus) {
        mDataManager.setLoginStatus(loginStatus);
    }

    /**
     * 获取是否处于登录状态
     * @return
     */
    @Override
    public boolean getLoginStatus() {
        return mDataManager.getLoginStatus();
    }

    /**
     * 获取登录的账号
     * @return
     */
    @Override
    public String getLoginAccount() {
        return mDataManager.getLoginAccount();
    }

    /**
     * 获取当前存储的页码
     * @return
     */
    @Override
    public int getCurrentPage() {
        return mDataManager.getCurrentPage();
    }

    /**
     * 添加一个订阅事件
     * @param disposable
     */
    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }
}
