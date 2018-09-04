package jan.jason.wanandroid.ui.mainpager.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.fragment.BaseRootFragment;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.mainpager.MainPagerContract;
import jan.jason.wanandroid.core.bean.main.banner.BannerData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.event.AutoLoginEvent;
import jan.jason.wanandroid.core.event.LoginEvent;
import jan.jason.wanandroid.core.event.SwitchNavigationEvent;
import jan.jason.wanandroid.core.event.SwitchProjectEvent;
import jan.jason.wanandroid.core.http.cookies.CookiesManager;
import jan.jason.wanandroid.presenter.mainpager.MainPagerPresenter;
import jan.jason.wanandroid.ui.main.activity.LoginActivity;
import jan.jason.wanandroid.ui.mainpager.adapter.ArticleListAdapter;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.GlideImageLoader;
import jan.jason.wanandroid.utils.JudgeUtils;

/**
 * @Description: 主页需要使用的片段
 * @Author: jasonjan
 * @Date: 2018/9/3 16:13
 */
public class MainPagerFragment extends BaseRootFragment<MainPagerPresenter> implements MainPagerContract.View{

    @BindView(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.main_pager_recycler_view)
    RecyclerView mRecyclerView;

    /**
     * 文章列表
     */
    private List<FeedArticleData> mFeedArticleDataList;

    /**
     * 文章列表适配器
     */
    private ArticleListAdapter mAdapter;

    /**
     * 文章位置
     */
    private int articlePosition;

    /**
     * 广告栏标题
     */
    private List<String> mBannerTitleList;

    /**
     * 广告栏Url
     */
    private List<String> mBannerUrlList;

    /**
     * 广告栏实体
     */
    private Banner mBanner;

    /**
     * 是否重新创建
     */
    private boolean isRecreate;

    /**
     * 注入视图时执行
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isRecreate = getArguments().getBoolean(Constants.ARG_PARAM1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
    }

    /**
     * 获取单例片段
     * @param param1
     * @param param2
     * @return
     */
    public static MainPagerFragment getInstance(boolean param1, String param2) {
        MainPagerFragment fragment = new MainPagerFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_pager;
    }

    /**
     * 初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        initRecyclerView();
        setRefresh();
        if (loggedAndNotRebuilt()) {
            mPresenter.loadMainPagerData();//已经登录且不是重建的视图
        } else {
            mPresenter.autoRefresh(true);//其他情况需要调用处理器的自动刷新功能
        }
        if (CommonUtils.isNetworkConnected()) {
            showLoading();
        }
    }

    /**
     * 判断是否已经登录，并且是否不是视图重建的情况
     * @return
     */
    private boolean loggedAndNotRebuilt() {
        return !TextUtils.isEmpty(mPresenter.getLoginAccount())
                && !TextUtils.isEmpty(mPresenter.getLoginPassword())
                && !isRecreate;
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mFeedArticleDataList = new ArrayList<>();
        mAdapter = new ArticleListAdapter(R.layout.item_search_pager, mFeedArticleDataList);
        //item点击事件
        mAdapter.setOnItemClickListener((adapter, view, position) -> startArticleDetailPager(view, position));
        //item中的child点击事件
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> clickChildEvent(view, position));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setHasFixedSize(true);
        //add head banner
        LinearLayout mHeaderGroup = ((LinearLayout) LayoutInflater.from(_mActivity).inflate(R.layout.head_banner, null));
        mBanner = mHeaderGroup.findViewById(R.id.head_banner);
        mHeaderGroup.removeView(mBanner);
        mAdapter.addHeaderView(mBanner);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * item中的子视图点击事件
     * @param view
     * @param position
     */
    private void clickChildEvent(View view, int position) {
        switch (view.getId()) {
            case R.id.item_search_pager_chapterName:
                startSingleChapterKnowledgePager(position);
                break;
            case R.id.item_search_pager_like_iv:
                likeEvent(position);
                break;
            case R.id.item_search_pager_tag_red_tv:
                clickTag(position);
                break;
            default:
                break;
        }
    }

    /**
     * 点击了子视图中的tag标签
     * @param position
     */
    private void clickTag(int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        String superChapterName = mAdapter.getData().get(position).getSuperChapterName();
        if (superChapterName.contains(getString(R.string.open_project))) {
            RxBus.getDefault().post(new SwitchProjectEvent());
        } else if (superChapterName.contains(getString(R.string.navigation))) {
            RxBus.getDefault().post(new SwitchNavigationEvent());
        }
    }

    /**
     * 点击了子视图的章节名
     * @param position
     */
    private void startSingleChapterKnowledgePager(int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        JudgeUtils.startKnowledgeHierarchyDetailActivity(_mActivity,
                true,
                mAdapter.getData().get(position).getSuperChapterName(),
                mAdapter.getData().get(position).getChapterName(),
                mAdapter.getData().get(position).getChapterId());
    }

    /**
     * item点击进入详情
     * @param view
     * @param position
     */
    private void startArticleDetailPager(View view, int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() < position) {
            return;
        }
        //记录点击的文章位置，便于在文章内点击收藏返回到此界面时能展示正确的收藏状态
        articlePosition = position;
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(_mActivity, view, getString(R.string.share_view));
        JudgeUtils.startArticleDetailActivity(_mActivity,
                options,
                mAdapter.getData().get(position).getId(),
                mAdapter.getData().get(position).getTitle(),
                mAdapter.getData().get(position).getLink(),
                mAdapter.getData().get(position).isCollect(),
                false,
                false);
    }

    /**
     * 静默登录成功
     */
    @Override
    public void showAutoLoginSuccess() {
        if (isAdded()) {
            CommonUtils.showSnackMessage(_mActivity, getString(R.string.auto_login_success));
            RxBus.getDefault().post(new AutoLoginEvent());
        }
    }

    /**
     * 静默登录失败
     */
    @Override
    public void showAutoLoginFail() {
        mPresenter.setLoginStatus(false);
        CookiesManager.clearAllCookies();
        RxBus.getDefault().post(new LoginEvent(false));
    }

    /**
     * 展示文章列表
     * @param feedArticleListData
     * @param isRefresh
     */
    @Override
    public void showArticleList(FeedArticleListData feedArticleListData, boolean isRefresh) {
        if (mPresenter.getCurrentPage() == Constants.TYPE_MAIN_PAGER) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
        if (mAdapter == null) {
            return;
        }
        if (isRefresh) {
            mFeedArticleDataList = feedArticleListData.getDatas();
            mAdapter.replaceData(feedArticleListData.getDatas());
        } else {
            mFeedArticleDataList.addAll(feedArticleListData.getDatas());
            mAdapter.addData(feedArticleListData.getDatas());
        }
        showNormal();
    }

    /**
     * 展示收藏文章成功后UI显示
     * @param position
     * @param feedArticleData
     * @param feedArticleListData
     */
    @Override
    public void showCollectArticleData(int position, FeedArticleData feedArticleData, FeedArticleListData feedArticleListData) {
        mAdapter.setData(position, feedArticleData);
        CommonUtils.showSnackMessage(_mActivity, getString(R.string.collect_success));
    }

    /**
     * 取消收藏文章后的UI显示
     * @param position
     * @param feedArticleData
     * @param feedArticleListData
     */
    @Override
    public void showCancelCollectArticleData(int position, FeedArticleData feedArticleData, FeedArticleListData feedArticleListData) {
        mAdapter.setData(position, feedArticleData);
        CommonUtils.showSnackMessage(_mActivity, getString(R.string.cancel_collect_success));
    }

    /**
     * 轮播图UI显示
     * @param bannerDataList
     */
    @Override
    public void showBannerData(List<BannerData> bannerDataList) {
        mBannerTitleList = new ArrayList<>();
        List<String> bannerImageList = new ArrayList<>();
        mBannerUrlList = new ArrayList<>();
        for (BannerData bannerData : bannerDataList) {
            mBannerTitleList.add(bannerData.getTitle());
            bannerImageList.add(bannerData.getImagePath());
            mBannerUrlList.add(bannerData.getUrl());
        }
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(bannerImageList);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(mBannerTitleList);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(bannerDataList.size() * 400);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mBanner.setOnBannerListener(i -> JudgeUtils.startArticleDetailActivity(_mActivity, null,
                0, mBannerTitleList.get(i), mBannerUrlList.get(i),
                false, false, true));
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    /**
     * 已登录的UI
     */
    @Override
    public void showLoginView() {
        mPresenter.getFeedArticleList(false);
    }

    /**
     * 已登出的UI
     */
    @Override
    public void showLogoutView() {
        mPresenter.getFeedArticleList(false);
    }

    /**
     * 收藏成功的数据变更
     */
    @Override
    public void showCollectSuccess() {
        if (mAdapter != null && mAdapter.getData().size() > articlePosition) {
            mAdapter.getData().get(articlePosition).setCollect(true);
            mAdapter.setData(articlePosition, mAdapter.getData().get(articlePosition));
        }
    }

    /**
     * 取消收藏的数据变更
     */
    @Override
    public void showCancelCollectSuccess() {
        if (mAdapter != null && mAdapter.getData().size() > articlePosition) {
            mAdapter.getData().get(articlePosition).setCollect(false);
            mAdapter.setData(articlePosition, mAdapter.getData().get(articlePosition));
        }
    }

    /**
     * 网络异常等错误的UI显示
     */
    @Override
    public void showError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        super.showError();
    }

    /**
     * 重新加载的UI显示
     */
    @Override
    public void reload() {
        if (mRefreshLayout != null && mPresenter != null
                && mRecyclerView.getVisibility() == View.INVISIBLE
                && CommonUtils.isNetworkConnected()) {
            mRefreshLayout.autoRefresh();
        }
    }

    /**
     * 获取轮播图，这个实体是第三方定义的
     * @return
     */
    public Banner getBanner(){
        return mBanner;
    }

    /**
     * 定义item 喜欢(收藏)的事件逻辑
     */
    private void likeEvent(int position){
        if (!mPresenter.getLoginStatus()) {
            startActivity(new Intent(_mActivity, LoginActivity.class));
            CommonUtils.showMessage(_mActivity, getString(R.string.login_tint));
            return;
        }
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        if (mAdapter.getData().get(position).isCollect()) {
            mPresenter.cancelCollectArticle(position, mAdapter.getData().get(position));
        } else {
            mPresenter.addCollectArticle(position, mAdapter.getData().get(position));
        }
    }

    /**
     * recyclerView滑到最上方
     */
    public void jumpToTheTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * 设置刷新和加载更多的逻辑
     */
    private void setRefresh() {
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mPresenter.autoRefresh(false);
            refreshLayout.finishRefresh(1000);
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mPresenter.loadMore();
            refreshLayout.finishLoadMore(1000);
        });
    }

}
