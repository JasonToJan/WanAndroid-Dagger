package jan.jason.wanandroid.presenter.project;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.project.ProjectListContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.bean.project.ProjectListData;
import jan.jason.wanandroid.core.event.JumpToTheTopEvent;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 项目列表处理器
 * @Author: jasonjan
 * @Date: 2018/9/4 17:04
 */
public class ProjectListPresenter extends BasePresenter<ProjectListContract.View> implements ProjectListContract.Presenter{

    /**
     * 数据管理员
     */
    private DataManager mDataManager;

    /**
     * 注入生命力
     * @param dataManager
     */
    @Inject
    ProjectListPresenter(DataManager dataManager){
        super(dataManager);
        this.mDataManager=dataManager;
    }

    /**
     * 视图创建之前设置一个事件
     * @param view
     */
    @Override
    public void attachView(ProjectListContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    /**
     * 在视图创建之前
     * 注册一个滑动到顶部的事件
     */
    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(JumpToTheTopEvent.class)
                .subscribe(jumpToTheTopEvent -> mView.showJumpToTheTop()));
    }

    /**
     * 逻辑方面，先获取项目列表，然后UI进行显示
     * @param page
     * @param cid
     * @param isShowError
     */
    @Override
    public void getProjectListData(int page, int cid, boolean isShowError) {
        addSubscribe(mDataManager.getProjectListData(page, cid)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<ProjectListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_project_list),
                        isShowError) {
                    @Override
                    public void onNext(ProjectListData projectListData) {
                        mView.showProjectListData(projectListData);
                    }
                }));
    }

    /**
     * 逻辑方面，先增加一个收藏记录，然后UI显示
     * @param position
     * @param feedArticleData
     */
    @Override
    public void addCollectOutsideArticle(int position, FeedArticleData feedArticleData) {
        addSubscribe(mDataManager.addCollectOutsideArticle(feedArticleData.getTitle(),
                feedArticleData.getAuthor(), feedArticleData.getLink())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleCollectResult())
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.collect_fail)) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        feedArticleData.setCollect(true);
                        mView.showCollectOutsideArticle(position, feedArticleData, feedArticleListData);
                    }
                }));
    }

    /**
     * 逻辑方面，先取消一个收藏记录，然后UI显示
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
