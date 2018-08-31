package jan.jason.wanandroid.base.presenter;

import io.reactivex.disposables.Disposable;
import jan.jason.wanandroid.base.view.AbstractView;

/**
 * @Description: 处理器帮助接口需要使用的接口
 * @Author: jasonjan
 * @Date: 2018/8/27 21:31
 */
public interface AbstractPresenter<T extends AbstractView> {

    /**
     * 注入视图
     * @param view
     */
    void attachView(T view);

    /**
     * 回收视图
     */
    void detachView();

    /**
     * 添加一个订阅事件管理器
     * @param disposable
     */
    void addRxBindingSubscribe(Disposable disposable);

    /**
     * 是否是夜间模式
     * @return
     */
    boolean getNightModeState();

    /**
     * 设置登录状态
     * @param loginStatus
     */
    void setLoginStatus(boolean loginStatus);

    /**
     * 获取登录状态
     * @return
     */
    boolean getLoginStatus();

    /**
     * 获取登录账号
     * @return
     */
    String getLoginAccount();

    /**
     * 获取当前页
     * @return
     */
    int getCurrentPage();
}
