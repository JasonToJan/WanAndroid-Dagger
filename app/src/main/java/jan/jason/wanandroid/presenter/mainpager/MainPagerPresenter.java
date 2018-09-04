package jan.jason.wanandroid.presenter.mainpager;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.mainpager.MainPagerContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.BaseResponse;
import jan.jason.wanandroid.core.bean.main.banner.BannerData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.bean.main.login.LoginData;
import jan.jason.wanandroid.core.event.CollectEvent;
import jan.jason.wanandroid.core.event.LoginEvent;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 主页处理器
 * @Author: jasonjan
 * @Date: 2018/9/3 17:22
 */
public class MainPagerPresenter extends BasePresenter<MainPagerContract.View> implements MainPagerContract.Presenter{

    /**
     * 数据源
     */
    private DataManager mDataManager;

    /**
     * 是否刷新
     */
    private boolean isRefresh = true;

    /**
     * 当前页
     */
    private int mCurrentPage;

    @Inject
    MainPagerPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    /**
     * 注入视图的同时，注入事件监听
     * @param view
     */
    @Override
    public void attachView(MainPagerContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    /**
     * 注入订阅事件
     */
    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(collectEvent -> !collectEvent.isCancelCollectSuccess())
                .subscribe(collectEvent -> mView.showCollectSuccess()));

        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(CollectEvent::isCancelCollectSuccess)
                .subscribe(collectEvent -> mView.showCancelCollectSuccess()));

        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .filter(LoginEvent::isLogin)
                .subscribe(loginEvent -> mView.showLoginView()));

        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .filter(loginEvent -> !loginEvent.isLogin())
                .subscribe(loginEvent -> mView.showLogoutView()));
    }

    @Override
    public String getLoginAccount() {
        return mDataManager.getLoginAccount();
    }

    @Override
    public String getLoginPassword() {
        return mDataManager.getLoginPassword();
    }

    /**
     * 加载主页数据
     */
    @Override
    public void loadMainPagerData(){
        //获取登录相关数据
        Observable<BaseResponse<LoginData>> mLoginObservable = mDataManager.getLoginData(getLoginAccount(), getLoginPassword());
        //获取轮播图相关数据
        Observable<BaseResponse<List<BannerData>>> mBannerObservable = mDataManager.getBannerData();
        //获取文章列表相关数据
        Observable<BaseResponse<FeedArticleListData>> mArticleObservable = mDataManager.getFeedArticleList(0);
        //组装所有数据
        addSubscribe(Observable.zip(mLoginObservable, mBannerObservable, mArticleObservable, this::createResponseMap)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObserver<HashMap<String, Object>>(mView) {
                    @Override
                    public void onNext(HashMap<String, Object> map) {
                        BaseResponse<LoginData> loginResponse = CommonUtils.cast(map.get(Constants.LOGIN_DATA));
                        if (loginResponse.getErrorCode() == BaseResponse.SUCCESS) {
                            loginSuccess(loginResponse);
                        } else {
                            mView.showAutoLoginFail();
                        }
                        BaseResponse<List<BannerData>> bannerResponse = CommonUtils.cast(map.get(Constants.BANNER_DATA));
                        if (bannerResponse != null) {
                            mView.showBannerData(bannerResponse.getData());
                        }
                        BaseResponse<FeedArticleListData> feedArticleListResponse = CommonUtils.cast(map.get(Constants.ARTICLE_DATA));
                        if (feedArticleListResponse != null) {
                            mView.showArticleList(feedArticleListResponse.getData(), isRefresh);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.showAutoLoginFail();
                    }
                }));
    }

    /**
     * 自动刷新
     * @param isShowError
     */
    @Override
    public void autoRefresh(boolean isShowError){
        isRefresh=true;
        mCurrentPage=0;
        getBannerData(isShowError);
        getFeedArticleList(isShowError);
    }

    /**
     * 加载更多
     */
    @Override
    public void loadMore() {
        isRefresh = false;
        mCurrentPage++;
        loadMoreData();
    }

    /**
     * 获取轮播图数据
     * @param isShowError
     */
    @Override
    public void getBannerData(boolean isShowError) {
        addSubscribe(mDataManager.getBannerData()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<BannerData>>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_banner_data),
                        isShowError) {
                    @Override
                    public void onNext(List<BannerData> bannerDataList) {
                        mView.showBannerData(bannerDataList);
                    }
                }));
    }

    /**
     * 获取文章列表
     * @param isShowError
     */
    @Override
    public void getFeedArticleList(boolean isShowError) {
        addSubscribe(mDataManager.getFeedArticleList(mCurrentPage)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .filter(feedArticleListResponse -> mView != null)
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_article_list),
                        isShowError) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        mView.showArticleList(feedArticleListData, isRefresh);
                    }
                }));
    }

    /**
     * 加载更多数据，请求网络
     */
    @Override
    public void loadMoreData() {
        addSubscribe(mDataManager.getFeedArticleList(mCurrentPage)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .filter(feedArticleListResponse -> mView != null)
                .subscribeWith(new BaseObserver<FeedArticleListData>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_article_list),
                        false) {
                    @Override
                    public void onNext(FeedArticleListData feedArticleListData) {
                        mView.showArticleList(feedArticleListData, isRefresh);
                    }
                }));
    }

    /**
     * 增加一个收藏记录
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
     * 取消一个收藏记录
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
     * 登录成功
     * @param loginResponse
     */
    private void loginSuccess(BaseResponse<LoginData> loginResponse) {
        LoginData loginData = loginResponse.getData();
        mDataManager.setLoginAccount(loginData.getUsername());
        mDataManager.setLoginPassword(loginData.getPassword());
        mDataManager.setLoginStatus(true);
        mView.showAutoLoginSuccess();
    }

    /**
     * 构建一个网络响应的一个映射类
     * @param loginResponse
     * @param bannerResponse
     * @param feedArticleListResponse
     * @return
     */
    @NonNull
    private HashMap<String, Object> createResponseMap(BaseResponse<LoginData> loginResponse,
                                                      BaseResponse<List<BannerData>> bannerResponse,
                                                      BaseResponse<FeedArticleListData> feedArticleListResponse) {
        HashMap<String, Object> map = new HashMap<>(3);
        map.put(Constants.LOGIN_DATA, loginResponse);
        map.put(Constants.BANNER_DATA, bannerResponse);
        map.put(Constants.ARTICLE_DATA, feedArticleListResponse);
        return map;
    }


}
