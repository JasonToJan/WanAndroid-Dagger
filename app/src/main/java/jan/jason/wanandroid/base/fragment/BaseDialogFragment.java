package jan.jason.wanandroid.base.fragment;

import android.content.Context;
import android.os.Bundle;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.utils.CommonUtils;

/**
 * @Description: MVP模式下的DialogFragment 升级版
 * @Author: jasonjan
 * @Date: 2018/9/4 21:00
 */
public abstract class BaseDialogFragment<T extends AbstractPresenter> extends AbstractSimpleDialogFragment
        implements AbstractView{

    /**
     * 注入活力的处理类
     */
    @Inject
    protected T mPresenter;

    /**
     * 在视图创建之前注入活力
     * @param context
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    /**
     * 首先执行 将执行处理器的attachView来关联视图
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 销毁视图
     */
    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroyView();
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        if (getActivity() != null) {
            CommonUtils.showSnackMessage(getActivity(), errorMsg);
        }
    }

    @Override
    public void useNightMode(boolean isNightMode) {
    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void showCollectSuccess() {

    }

    @Override
    public void showCancelCollectSuccess() {

    }

    @Override
    public void showLoginView() {

    }

    @Override
    public void showLogoutView() {

    }

    @Override
    public void showToast(String message) {
        if (getActivity() == null) {
            return;
        }
        CommonUtils.showMessage(getActivity(), message);
    }

    @Override
    public void showSnackBar(String message) {
        if (getActivity() == null) {
            return;
        }
        CommonUtils.showSnackMessage(getActivity(), message);
    }
}
