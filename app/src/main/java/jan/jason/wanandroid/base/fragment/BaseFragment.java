package jan.jason.wanandroid.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.utils.CommonUtils;

/**
 * @Description: 基础片段
 * @Author: jasonjan
 * @Date: 2018/9/3 15:32
 */
public abstract class BaseFragment<T extends AbstractPresenter> extends AbstractSimpleFragment implements AbstractView{

    @Inject
    protected T mPresenter;

    /**
     * 活动注入
     * @param activity
     */
    @Override
    public void onAttach(Activity activity){
        AndroidSupportInjection.inject(this);
        super.onAttach(activity);
    }

    /**
     * 视图被创建
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        if(mPresenter!=null){
            mPresenter.attachView(this);//间接调用处理器
        }
    }

    /**
     * 销毁视图
     */
    @Override
    public void onDestroyView(){
        if(mPresenter!=null){
            mPresenter.detachView();
            mPresenter=null;
        }
        super.onDestroyView();
    }

    /**
     * 使用夜间模式
     * @param isNightMode
     */
    @Override
    public void useNightMode(boolean isNightMode){

    }

    /**
     * 显示错误消息
     * @param errorMsg
     */
    @Override
    public void showErrorMsg(String errorMsg){
        if(isAdded()){
            CommonUtils.showSnackMessage(_mActivity,errorMsg);
        }
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
        CommonUtils.showMessage(_mActivity, message);
    }

    @Override
    public void showSnackBar(String message) {
        CommonUtils.showSnackMessage(_mActivity, message);
    }
}
