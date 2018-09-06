package jan.jason.wanandroid.widget;

import android.text.TextUtils;

import io.reactivex.subscribers.ResourceSubscriber;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.http.exception.ServerException;
import jan.jason.wanandroid.utils.LogHelper;
import retrofit2.HttpException;

/**
 * @Description: 选择夜间模式中用到的包装类
 * @Author: jasonjan
 * @Date: 2018/9/5 14:52
 */
public abstract class BaseSubscribe <T> extends ResourceSubscriber<T> {

    /**
     * 抽象视图实例
     */
    private AbstractView mView;

    /**
     * 错误信息
     */
    private String mErrorMsg;

    /**
     * 是否显示错误信息
     */
    private boolean isShowError = true;


    protected BaseSubscribe(AbstractView view){
        this.mView = view;
    }

    protected BaseSubscribe(AbstractView view, String errorMsg){
        this.mView = view;
        this.mErrorMsg = errorMsg;
    }

    protected BaseSubscribe(AbstractView view, boolean isShowError){
        this.mView = view;
        this.isShowError = isShowError;
    }

    protected BaseSubscribe(AbstractView view, String errorMsg, boolean isShowError){
        this.mView = view;
        this.mErrorMsg = errorMsg;
        this.isShowError = isShowError;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }
        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            mView.showErrorMsg(mErrorMsg);
        } else if (e instanceof ServerException) {
            mView.showErrorMsg(e.toString());
        } else if (e instanceof HttpException) {
            mView.showErrorMsg(WanAndroidApp.getInstance().getString(R.string.http_error));
        } else {
            mView.showErrorMsg(WanAndroidApp.getInstance().getString(R.string.unKnown_error));
            LogHelper.d(e.toString());
        }
        if (isShowError) {
            mView.showError();
        }
    }
}
