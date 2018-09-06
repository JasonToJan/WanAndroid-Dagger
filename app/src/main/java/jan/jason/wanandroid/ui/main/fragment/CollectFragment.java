package jan.jason.wanandroid.ui.main.fragment;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.fragment.BaseRootFragment;
import jan.jason.wanandroid.contract.main.CollectContract;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.presenter.main.CollectPresenter;
import jan.jason.wanandroid.ui.mainpager.adapter.ArticleListAdapter;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.JudgeUtils;

/**
 * @Description: 收藏片段，要确保已经登录才可见
 * @Author: jasonjan
 * @Date: 2018/9/5 9:37
 */
public class CollectFragment extends BaseRootFragment<CollectPresenter> implements CollectContract.View{

    @BindView(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.collect_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.collect_floating_action_btn)
    FloatingActionButton mFloatingActionButton;

    /**
     * 是否刷新
     */
    private boolean isRefresh = true;

    /**
     * 当前页码
     */
    private int mCurrentPage;

    /**
     * 收藏的文章列表
     */
    private List<FeedArticleData> mArticles;

    /**
     * 文章列表适配器
     */
    private ArticleListAdapter mAdapter;

    /**
     * 外部调用，生产一个收藏碎片
     * @param param1
     * @param param2
     * @return
     */
    public static CollectFragment getInstance(String param1, String param2) {
        CollectFragment fragment = new CollectFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 父类调用，获取布局id
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collect;
    }

    /**
     * 父类调用，初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        initRecyclerView();
        mPresenter.getCollectList(mCurrentPage, true);
        setRefresh();
        if (CommonUtils.isNetworkConnected()) {
            showLoading();
        }
    }

    /**
     * 视图显示后执行，调用自动刷新事件
     */
    @Override
    public void onResume() {
        super.onResume();
        showRefreshEvent();
    }

    /**
     * UI方面，展示收藏列表
     * @param feedArticleListData
     */
    @Override
    public void showCollectList(FeedArticleListData feedArticleListData) {
        if (mAdapter == null) {
            return;
        }
        mArticles = feedArticleListData.getDatas();
        if (isRefresh) {
            mAdapter.replaceData(mArticles);
        } else {
            showLoadMore(feedArticleListData);
        }
        if (mAdapter.getData().size() == 0) {
            CommonUtils.showSnackMessage(_mActivity, getString(R.string.no_collect));
        }
        showNormal();
    }

    /**
     * UI方面，展示取消收藏后的视图
     * @param position
     * @param feedArticleData
     * @param feedArticleListData
     */
    @Override
    public void showCancelCollectPageArticleData(int position, FeedArticleData feedArticleData, FeedArticleListData feedArticleListData) {
        mAdapter.remove(position);
        CommonUtils.showSnackMessage(_mActivity, getString(R.string.cancel_collect_success));
    }

    /**
     * Ui方面，展示自动刷新事件
     */
    @Override
    public void showRefreshEvent() {
        if (isVisible()) {
            mCurrentPage = 0;
            isRefresh = true;
            mPresenter.getCollectList(mCurrentPage, false);
        }
    }

    /**
     * 定义点击事件
     * @param view
     */
    @OnClick({R.id.collect_floating_action_btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.collect_floating_action_btn:
                mRecyclerView.smoothScrollToPosition(0);
                break;
            default:
                break;
        }
    }

    /**
     * 定义重新加载的逻辑
     */
    @Override
    public void reload() {
        mRefreshLayout.autoRefresh();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mArticles = new ArrayList<>();
        mAdapter = new ArticleListAdapter(R.layout.item_search_pager, mArticles);
        mAdapter.isCollectPage();
        mAdapter.setOnItemClickListener((adapter, view, position) -> startArticleDetailPager(view, position));
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> clickChildEvent(view, position));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 内部调用，item子视图点击事件
     * @param view
     * @param position
     */
    private void clickChildEvent(View view, int position) {
        switch (view.getId()) {
            case R.id.item_search_pager_chapterName:
                startSingleChapterKnowledgePager(position);
                break;
            case R.id.item_search_pager_like_iv:
                cancelCollect(position);
                break;
            default:
                break;
        }
    }

    /**
     * 内部调用,定义取消收藏的逻辑
     * @param position
     */
    private void cancelCollect(int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        mPresenter.cancelCollectPageArticle(position, mAdapter.getData().get(position));
    }

    /**
     * 内部调用，定义跳转到某一个知识体系章节列表
     * @param position
     */
    private void startSingleChapterKnowledgePager(int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        JudgeUtils.startKnowledgeHierarchyDetailActivity(_mActivity,
                true,
                mAdapter.getData().get(position).getChapterName(),
                mAdapter.getData().get(position).getChapterName(),
                mAdapter.getData().get(position).getChapterId());
    }

    /**
     * 内部调用，定义跳转到详情页的逻辑
     * @param view
     * @param position
     */
    private void startArticleDetailPager(View view, int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(_mActivity, view, getString(R.string.share_view));
        JudgeUtils.startArticleDetailActivity(_mActivity, options,
                mAdapter.getData().get(position).getId(),
                mAdapter.getData().get(position).getTitle(),
                mAdapter.getData().get(position).getLink(),
                true,
                true,
                false);
    }

    /**
     * Ui方面，定义加载更多的逻辑
     * @param feedArticleListData
     */
    private void showLoadMore(FeedArticleListData feedArticleListData) {
        if (mArticles.size() > 0) {
            mArticles.addAll(feedArticleListData.getDatas());
            mAdapter.addData(feedArticleListData.getDatas());
        } else {
            if (mAdapter.getData().size() != 0) {
                CommonUtils.showMessage(_mActivity, getString(R.string.load_more_no_data));
            }
        }
    }

    /**
     * 定义刷新逻辑
     */
    private void setRefresh() {
        mRefreshLayout.setPrimaryColorsId(Constants.BLUE_THEME, R.color.white);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            showRefreshEvent();
            refreshLayout.finishRefresh(1000);
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mCurrentPage++;
            isRefresh = false;
            mPresenter.getCollectList(mCurrentPage, false);
            refreshLayout.finishLoadMore(1000);
        });
    }

}
