package jan.jason.wanandroid.presenter.main;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.contract.main.SplashContract;
import jan.jason.wanandroid.core.DataManager;

/**
 * @Description: 启动页的处理器
 * @Author: jasonjan
 * @Date: 2018/8/29 7:47
 */
public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter{

    /**
     * 构造函数中注入数据管理类
     * @param dataManager
     */
    @Inject
    SplashPresenter(DataManager dataManager){
        super(dataManager);
    }

    /**
     * 定义视图创建完成后的逻辑，
     * 在BaseActivity中利用处理器来调用这个函数
     * @param view
     */
    @Override
    public void attachView(SplashContract.View view){
        super.attachView(view);
        long splashTime=2000;
        //添加定时器，在主线程中订阅该任务
        addSubscribe(Observable.timer(splashTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> view.jumpToMain()));
    }
}
