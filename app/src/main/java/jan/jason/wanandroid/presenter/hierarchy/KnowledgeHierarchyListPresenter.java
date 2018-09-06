package jan.jason.wanandroid.presenter.hierarchy;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.hierarchy.KnowledgeHierarchyListContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.event.CollectEvent;
import jan.jason.wanandroid.core.event.KnowledgeJumpTopEvent;
import jan.jason.wanandroid.core.event.ReloadDetailEvent;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 知识体系列表的 处理器
 * @Author: jasonjan
 * @Date: 2018/9/5 22:03
 */
public class KnowledgeHierarchyListPresenter extends BasePresenter<KnowledgeHierarchyListContract.View>
        implements KnowledgeHierarchyListContract.Presenter{

    /**
     * 数据管理员
     */
    private DataManager mDataManager;

    /**
     * 注入活力
     * @param dataManager
     */
    @Inject
    KnowledgeHierarchyListPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    /**
     * 视图创建前执行
     * @param view
     */
    @Override
    public void attachView(KnowledgeHierarchyListContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    /**
     * 定义4个事件，触发后的逻辑
     */
    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(collectEvent -> !collectEvent.isCancelCollectSuccess())
                .subscribe(collectEvent -> mView.showCollectSuccess()));

        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(CollectEvent::isCancelCollectSuccess)
                .subscribe(collectEvent -> mView.showCancelCollectSuccess()));

        addSubscribe(RxBus.getDefault().toFlowable(KnowledgeJumpTopEvent.class)
                .subscribe(knowledgeJumpTopEvent -> mView.showJumpTheTop()));

        addSubscribe(RxBus.getDefault().toFlowable(ReloadDetailEvent.class)
                .subscribe(reloadEvent -> mView.showReloadDetailEvent()));
    }

    /**
     * 数据方面，获取知识体系列表（某一个分类下的），最后进行UI显示
     * @param page
     * @param cid
     * @param isShowError
     */
    @Override
    public void getKnowledgeHierarchyDetailData(int page, int cid, boolean isShowError) {
        addSubscribe(mDataManager.getKnowledgeHierarchyDetailData(page, cid)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_knowledge_data),
                        isShowError) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        mView.showKnowledgeHierarchyDetailData(feedArticleListData);
                    }
                }));
    }

    /**
     * 逻辑方面，收藏一个记录，然后UI展示
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
     * 逻辑方面，取消收藏一个记录，然后Ui展示
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
}
