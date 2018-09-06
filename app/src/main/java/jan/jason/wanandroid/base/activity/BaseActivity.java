package jan.jason.wanandroid.base.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.utils.CommonUtils;

/**
 * @Description: MVP模式的基础活动，存储一个抽象处理类
 *              帮忙实现了抽象视图接口中定义的许多方法呢
 * @Author: jasonjan
 * @Date: 2018/8/27 20:14
 */
public abstract class BaseActivity<T extends AbstractPresenter> extends AbstractSimpleActivity
        implements HasSupportFragmentInjector,AbstractView {

    /**
     * dagger注入者
     */
    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    /**
     * 活动处理器
     */
    @Inject
    protected T mPresenter;

    /**
     * 率先执行
     * @param saveInstanceState
     */
    @Override
    protected void onCreate(Bundle saveInstanceState){
        AndroidInjection.inject(this);//先注入活动，在调用父类onCreate
        super.onCreate(saveInstanceState);
    }

    /**
     * 布局视图创建完成后执行
     */
    @Override
    protected void onViewCreated(){
        if(mPresenter!=null){
            mPresenter.attachView(this);//处理器绑定视图，以此方便处理器进行视图操作
        }
    }

    /**
     * 最后执行
     */
    @Override
    protected void onDestroy(){
        if(mPresenter!=null){
            mPresenter.detachView();//活动基类销毁前，将回收处理器绑定的视图
            mPresenter=null;
        }
        super.onDestroy();
    }

    /**
     * HasSupportFragmentInjector接口需实现的注入方法
     * @return
     */
    @Override
    public AndroidInjector<Fragment> supportFragmentInjector(){
        return mFragmentDispatchingAndroidInjector;
    }

    /**
     * 使用夜间模式,在MainPresenter中注册了夜间模式事件
     * @param isNight
     */
    @Override
    public void useNightMode(boolean isNight){
        if(isNight){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }

    /**
     * 展示异常信息
     */
    @Override
    public void showErrorMsg(String errorMsg){
        CommonUtils.showSnackMessage(this,errorMsg);
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
        CommonUtils.showMessage(this, message);
    }

    @Override
    public void showSnackBar(String message) {
        CommonUtils.showSnackMessage(this, message);
    }


}
