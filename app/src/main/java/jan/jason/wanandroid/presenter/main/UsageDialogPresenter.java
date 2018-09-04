package jan.jason.wanandroid.presenter.main;

import java.util.List;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.contract.main.UsageDialogContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.main.search.UsefulSiteData;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 常用网站处理器
 * @Author: jasonjan
 * @Date: 2018/9/4 21:21
 */
public class UsageDialogPresenter extends BasePresenter<UsageDialogContract.View> implements UsageDialogContract.Presenter{

    /**
     * 数据管理员
     */
    private DataManager mDataManager;

    /**
     * 注入活力
     * @param dataManager
     */
    @Inject
    UsageDialogPresenter(DataManager dataManager){
        super(dataManager);
        mDataManager=dataManager;
    }

    /**
     * 逻辑方面，先请求网络获取网站数据，再进行UI显示
     */
    @Override
    public void getUsefulSites() {
        addSubscribe(mDataManager.getUsefulSites()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<UsefulSiteData>>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_useful_sites_data)) {
                    @Override
                    public void onNext(List<UsefulSiteData> usefulSiteDataList) {
                        mView.showUsefulSites(usefulSiteDataList);
                    }
                }));
    }
}
