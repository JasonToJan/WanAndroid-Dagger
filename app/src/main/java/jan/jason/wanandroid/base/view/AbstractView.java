package jan.jason.wanandroid.base.view;

/**
 * @Description: 处理器帮助接口需要使用的View基类
 * @Author: jasonjan
 * @Date: 2018/8/27 21:23
 */
public interface AbstractView {

    /**
     * 是否使用夜间模式
     * @param isNightMode
     */
    void useNightMode(boolean isNightMode);

    /**
     * 展示异常信息
     * @param errorMsg
     */
    void showErrorMsg(String errorMsg);

    /**
     * 正常显示
     */
    void showNormal();

    /**
     * 发生错误时显示
     */
    void showError();

    /**
     * 加载的时候显示
     */
    void showLoading();

    /**
     * 重新加载的时候显示
     */
    void reload();

    /**
     * 展示登录的视图
     */
    void showLoginView();

    /**
     * 展示登出的视图
     */
    void showLogoutView();

    /**
     * 展示收集成功
     */
    void showCollectSuccess();

    /**
     * 展示取消收集成功
     */
    void showCancelCollectSuccess();

    /**
     * 展示一个toast
     * @param message
     */
    void showToast(String message);

    /**
     * 展示一个SnackBar
     * @param message
     */
    void showSnackBar(String message);
}
