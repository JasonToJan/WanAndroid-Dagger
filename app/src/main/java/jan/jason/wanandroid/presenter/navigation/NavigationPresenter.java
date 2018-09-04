package jan.jason.wanandroid.presenter.navigation;

import java.util.List;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.contract.navigation.NavigationContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.navigation.NavigationListData;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 导航页面的处理器
 * @Author: jasonjan
 * @Date: 2018/9/4 15:13
 */
public class NavigationPresenter extends BasePresenter<NavigationContract.View> implements NavigationContract.Presenter{

    /**
     * 数据管理员
     */
    private DataManager mDataManager;

    /**
     * 注入活力
     */
    @Inject
    NavigationPresenter(DataManager dataManager){
        super(dataManager);
        this.mDataManager=dataManager;
    }

    /**
     * 逻辑方面，先获取数据，后显示视图
     * @param isShowError
     */
    public void getNavigationListData(boolean isShowError){
        addSubscribe(mDataManager.getNavigationListData()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<NavigationListData>>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_navigation_list),
                        isShowError) {
                    @Override
                    public void onNext(List<NavigationListData> navigationDataList) {
                        mView.showNavigationListData(navigationDataList);
                    }
                }));
    }
}
