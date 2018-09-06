package jan.jason.wanandroid.presenter.main;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.main.CollectContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.event.CollectEvent;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 收藏记录 处理器
 * @Author: jasonjan
 * @Date: 2018/9/5 9:46
 */
public class CollectPresenter extends BasePresenter<CollectContract.View> implements CollectContract.Presenter{

    /**
     * 数据中心
     */
    private DataManager mDataManager;

    /**
     * 注入生命力
     * @param dataManager
     */
    @Inject
    CollectPresenter(DataManager dataManager){
        super(dataManager);
        mDataManager=dataManager;
    }

    /**
     * 视图未创建前执行
     * @param view
     */
    @Override
    public void attachView(CollectContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    /**
     * 注册搜集事件，UI执行刷新
     */
    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .subscribe(collectEvent -> mView.showRefreshEvent()));
    }

    /**
     * 数据方面，获取收藏列表，然后UI显示
     * @param page
     * @param isShowError
     */
    @Override
    public void getCollectList(int page,boolean isShowError){
        addSubscribe(mDataManager.getCollectList(page)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_collection_data),
                        isShowError) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        mView.showCollectList(feedArticleListData);
                    }
                }));
    }

    /**
     * 数据方面，取消收藏记录，然后UI显示
     * @param position
     * @param feedArticleData
     */
    public void cancelCollectPageArticle(int position, FeedArticleData feedArticleData){
        addSubscribe(mDataManager.cancelCollectPageArticle(feedArticleData.getId())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleCollectResult())
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.cancel_collect_fail)) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        feedArticleData.setCollect(false);
                        mView.showCancelCollectPageArticleData(position, feedArticleData, feedArticleListData);
                    }
                }));
    }
}
