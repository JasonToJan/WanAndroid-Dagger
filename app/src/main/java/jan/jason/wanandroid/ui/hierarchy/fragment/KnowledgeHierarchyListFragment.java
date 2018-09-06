package jan.jason.wanandroid.ui.hierarchy.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.fragment.BaseRootFragment;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.hierarchy.KnowledgeHierarchyListContract;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.event.CollectEvent;
import jan.jason.wanandroid.core.event.SwitchNavigationEvent;
import jan.jason.wanandroid.core.event.SwitchProjectEvent;
import jan.jason.wanandroid.presenter.hierarchy.KnowledgeHierarchyListPresenter;
import jan.jason.wanandroid.ui.main.activity.LoginActivity;
import jan.jason.wanandroid.ui.mainpager.adapter.ArticleListAdapter;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.JudgeUtils;

/**
 * @Description: 知识体系 列表的碎片
 * @Author: jasonjan
 * @Date: 2018/9/5 22:03
 */
public class KnowledgeHierarchyListFragment extends BaseRootFragment<KnowledgeHierarchyListPresenter> implements KnowledgeHierarchyListContract.View{

    @BindView(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.knowledge_hierarchy_list_recycler_view)
    RecyclerView mRecyclerView;

    /**
     * 传递过来的id
     */
    private int id;

    /**
     * 当前页码
     */
    private int mCurrentPage;

    /**
     * 文章列表集合
     */
    private List<FeedArticleData> mArticles;

    /**
     * 文章列表适配器
     */
    private ArticleListAdapter mAdapter;

    /**
     * 是否刷新
     */
    private boolean isRefresh = true;

    /**
     * 记录的文章位置
     */
    private int articlePosition;

    /**
     * 场景动画配置参数
     */
    private ActivityOptions mOptions;

    /**
     * 外部调用，生产一个KnowledgeHierarchyListFragment碎片
     * @param id
     * @param param2
     * @return
     */
    public static KnowledgeHierarchyListFragment getInstance(int id, String param2) {
        KnowledgeHierarchyListFragment fragment = new KnowledgeHierarchyListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_PARAM1, id);
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
        return R.layout.fragment_knowledge_hierarchy_list;
    }

    /**
     * 父类调用，初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        isInnerFragment = true;
        setRefresh();
        Bundle bundle = getArguments();
        id = bundle.getInt(Constants.ARG_PARAM1, 0);
        if (id == 0) {
            return;
        }
        //重置当前页数，防止页面切换后当前页数为较大而加载后面的数据或没有数据
        mCurrentPage = 0;
        mPresenter.getKnowledgeHierarchyDetailData(mCurrentPage, id, true);
        initRecyclerView();
        if (CommonUtils.isNetworkConnected()) {
            showLoading();
        }
    }

    /**
     * UI方面，进行展示列表数据
     * @param feedArticleListData
     */
    @Override
    public void showKnowledgeHierarchyDetailData(FeedArticleListData feedArticleListData) {
        mArticles = feedArticleListData.getDatas();
        if (isRefresh) {
            mAdapter.replaceData(mArticles);
        } else {
            if (mArticles.size() > 0) {
                mAdapter.addData(mArticles);
            } else {
                CommonUtils.showMessage(_mActivity, getString(R.string.load_more_no_data));
            }
        }
        showNormal();
    }

    /**
     * 定义重新加载的逻辑
     */
    @Override
    public void reload() {
        if (mPresenter != null) {
            mRefreshLayout.autoRefresh();
        }
    }

    /**
     * 定义收藏的逻辑，发送收藏事件
     * @param position
     * @param feedArticleData
     * @param feedArticleListData
     */
    @Override
    public void showCollectArticleData(int position, FeedArticleData feedArticleData, FeedArticleListData feedArticleListData) {
        mAdapter.setData(position, feedArticleData);
        RxBus.getDefault().post(new CollectEvent(false));
        CommonUtils.showSnackMessage(_mActivity, getString(R.string.collect_success));
    }

    /**
     * UI方面，展示收藏成功后的界面
     */
    @Override
    public void showCollectSuccess() {
        if (mAdapter != null && mAdapter.getData().size() > articlePosition) {
            mAdapter.getData().get(articlePosition).setCollect(true);
            mAdapter.setData(articlePosition, mAdapter.getData().get(articlePosition));
        }
    }

    /**
     * UI方面，展示取消收藏成功的界面
     */
    @Override
    public void showCancelCollectSuccess() {
        if (mAdapter != null && mAdapter.getData().size() > articlePosition) {
            mAdapter.getData().get(articlePosition).setCollect(false);
            mAdapter.setData(articlePosition, mAdapter.getData().get(articlePosition));
        }
    }

    /**
     * 定义取消收藏的逻辑
     * @param position
     * @param feedArticleData
     * @param feedArticleListData
     */
    @Override
    public void showCancelCollectArticleData(int position, FeedArticleData feedArticleData, FeedArticleListData feedArticleListData) {
        mAdapter.setData(position, feedArticleData);
        RxBus.getDefault().post(new CollectEvent(true));
        CommonUtils.showSnackMessage(_mActivity, getString(R.string.cancel_collect_success));
    }

    /**
     * UI方面，滑动到顶部
     */
    @Override
    public void showJumpTheTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * 定义重新加载的逻辑
     */
    @Override
    public void showReloadDetailEvent() {
        if (mRefreshLayout != null) {
            mRefreshLayout.autoRefresh();
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mAdapter = new ArticleListAdapter(R.layout.item_search_pager, mArticles);
        mAdapter.setOnItemClickListener((adapter, view, position) -> startArticleDetailPager(view, position));
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> clickChildEvent(view, position));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 定义子视图点击事件的逻辑
     * @param view
     * @param position
     */
    private void clickChildEvent(View view, int position) {
        switch (view.getId()) {
            case R.id.item_search_pager_chapterName:
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
     * 点击了Tag
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
     * 定义item点击事件，进入文章详情
     * @param view
     * @param position
     */
    private void startArticleDetailPager(View view, int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        articlePosition = position;
        mOptions = ActivityOptions.makeSceneTransitionAnimation(_mActivity, view, getString(R.string.share_view));
        JudgeUtils.startArticleDetailActivity(_mActivity,
                mOptions,
                mAdapter.getData().get(position).getId(),
                mAdapter.getData().get(position).getTitle().trim(),
                mAdapter.getData().get(position).getLink().trim(),
                mAdapter.getData().get(position).isCollect(),
                false,
                false);
    }

    /**
     * 点击了收藏图标
     * @param position
     */
    private void likeEvent(int position) {
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
     * 定义刷新和加载的逻辑
     */
    private void setRefresh() {
        mRefreshLayout.setPrimaryColorsId(Constants.BLUE_THEME, R.color.white);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mCurrentPage = 0;
            if (id != 0) {
                isRefresh = true;
                mPresenter.getKnowledgeHierarchyDetailData(0, id, false);
            }
            refreshLayout.finishRefresh(1000);
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mCurrentPage++;
            if (id != 0) {
                isRefresh = false;
                mPresenter.getKnowledgeHierarchyDetailData(mCurrentPage, id, false);
            }
            refreshLayout.finishLoadMore(1000);
        });
    }
}
