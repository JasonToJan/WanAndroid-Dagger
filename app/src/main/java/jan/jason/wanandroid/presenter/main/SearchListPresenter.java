package jan.jason.wanandroid.presenter.main;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.main.SearchListContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.event.CollectEvent;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 搜索列表 处理器
 * @Author: jasonjan
 * @Date: 2018/9/6 9:43
 */
public class SearchListPresenter extends BasePresenter<SearchListContract.View> implements SearchListContract.Presenter{

    /**
     * 数据中心
     */
    private DataManager mDataManager;

    /**
     * 注入新鲜活力
     * @param dataManager
     */
    @Inject
    SearchListPresenter(DataManager dataManager){
        super(dataManager);
        this.mDataManager=dataManager;
    }

    /**
     * 视图创建之前执行
     * @param view
     */
    @Override
    public void attachView(SearchListContract.View view){
        super.attachView(view);
        registerEvent();
    }

    /**
     * 注入事件,定义接收到CollectEvent事件后的视图逻辑
     */
    private void registerEvent(){
        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(collectEvent -> !collectEvent.isCancelCollectSuccess())
                .subscribe(collectEvent -> mView.showCollectSuccess()));

        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(CollectEvent::isCancelCollectSuccess)
                .subscribe(collectEvent -> mView.showCancelCollectSuccess()));
    }

    /**
     * 定义获取搜索列表的逻辑，然后UI进行显示
     * @param page
     * @param k
     * @param isShowError
     */
    @Override
    public void getSearchList(int page, String k, boolean isShowError) {
        addSubscribe(mDataManager.getSearchList(page, k)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_search_data_list),
                        isShowError) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        mView.showSearchList(feedArticleListData);
                    }
                }));
    }

    /**
     * 定义收藏一篇文章的逻辑
     * @param position
     * @param feedArticleData
     */
    @Override
    public void addCollectArticle(int position, FeedArticleData feedArticleData) {
        addSubscribe(mDataManager.addCollectArticle(feedArticleData.getId())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleCollectResult())
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.collect_fail)) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        feedArticleData.setCollect(true);
                        mView.showCollectArticleData(position, feedArticleData, feedArticleListData);
                    }
                }));
    }

    /**
     * 定义取消收藏一篇文章的逻辑
     * @param position
     * @param feedArticleData
     */
    @Override
    public void cancelCollectArticle(int position, FeedArticleData feedArticleData) {
        addSubscribe(mDataManager.cancelCollectArticle(feedArticleData.getId())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleCollectResult())
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.cancel_collect_fail)) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        feedArticleData.setCollect(false);
                        mView.showCancelCollectArticleData(position, feedArticleData, feedArticleListData);
                    }
                }));
    }

    /**
     * 获取夜间模式状态
     * @return
     */
    @Override
    public boolean getNightModeState() {
        return mDataManager.getNightModeState();
    }

}
