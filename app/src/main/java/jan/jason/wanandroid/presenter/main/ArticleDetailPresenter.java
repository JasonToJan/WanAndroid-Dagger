package jan.jason.wanandroid.presenter.main;

import android.Manifest;

import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.contract.main.ArticleDetailContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 文章详情处理器
 * @Author: jasonjan
 * @Date: 2018/9/5 16:45
 */
public class ArticleDetailPresenter extends BasePresenter<ArticleDetailContract.View> implements ArticleDetailContract.Presenter{

    /**
     * 数据中心
     */
    private DataManager mDataManager;

    /**
     * 注入新鲜活力
     * @param dataManager
     */
    @Inject
    ArticleDetailPresenter(DataManager dataManager){
        super(dataManager);
        mDataManager=dataManager;
    }

    /**
     * 数据方面，获取自动缓存状态（true or false）
     * @return
     */
    @Override
    public boolean getAutoCacheState() {
        return mDataManager.getAutoCacheState();
    }

    /**
     * 数据方面，获取无图模式状态
     * @return
     */
    @Override
    public boolean getNoImageState() {
        return mDataManager.getNoImageState();
    }

    /**
     * 逻辑方面，先请求后台添加收藏记录，然后UI显示
     * @param articleId
     */
    public void addCollectArticle(int articleId) {
        addSubscribe(mDataManager.addCollectArticle(articleId)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleCollectResult())
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.collect_fail)) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        mView.showCollectArticleData(feedArticleListData);
                    }
                }));
    }

    /**
     * 逻辑方面，先请求后台取消收藏记录，然后UI显示
     * @param articleId
     */
    @Override
    public void cancelCollectArticle(int articleId) {
        addSubscribe(mDataManager.cancelCollectArticle(articleId)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleCollectResult())
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.cancel_collect_fail)) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        mView.showCancelCollectArticleData(feedArticleListData);
                    }
                }));
    }

    /**
     * 逻辑方面，先请求后台取消收藏记录，然后UI显示
     * @param articleId
     */
    @Override
    public void cancelCollectPageArticle(int articleId) {
        addSubscribe(mDataManager.cancelCollectPageArticle(articleId)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleCollectResult())
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.cancel_collect_fail)) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        mView.showCancelCollectArticleData(feedArticleListData);
                    }
                }));
    }

    /**
     * 动态申请权限，然后分享文章
     * @param rxPermissions
     */
    @Override
    public void shareEventPermissionVerify(RxPermissions rxPermissions) {
        addSubscribe(rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        mView.shareEvent();
                    } else {
                        mView.shareError();
                    }
                }));
    }
}
