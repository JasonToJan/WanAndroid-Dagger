package jan.jason.wanandroid.ui.main.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.activity.BaseRootActivity;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.main.SearchListContract;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.event.SwitchNavigationEvent;
import jan.jason.wanandroid.core.event.SwitchProjectEvent;
import jan.jason.wanandroid.presenter.main.SearchListPresenter;
import jan.jason.wanandroid.ui.mainpager.adapter.ArticleListAdapter;
import jan.jason.wanandroid.ui.navigation.fragment.NavigationFragment;
import jan.jason.wanandroid.ui.project.fragment.ProjectFragment;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.JudgeUtils;
import jan.jason.wanandroid.utils.StatusBarUtil;

/**
 * @Description: 搜索结果列表
 * @Author: jasonjan
 * @Date: 2018/9/5 9:37
 */
public class SearchListActivity extends BaseRootActivity<SearchListPresenter> implements SearchListContract.View {

    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.common_toolbar_title_tv)
    TextView mTitleTv;
    @BindView(R.id.search_list_refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.normal_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_list_floating_action_btn)
    FloatingActionButton mFloatingActionButton;

    /**
     * 当前文章位置
     */
    private int articlePosition;

    /**
     * 当前页面，以便记住
     */
    private int mCurrentPage;

    /**
     * 文章列表集合
     */
    private List<FeedArticleData> mArticleList;

    /**
     * 文章列表适配器
     */
    private ArticleListAdapter mAdapter;

    /**
     * 是否添加数据到搜索记录
     */
    private boolean isAddData;

    /**
     * 搜索关键字
     */
    private String searchText;

    /**
     * 父类调用，获取布局id
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_list;
    }

    /**
     * 父类调用，初始化标题栏
     */
    @Override
    protected void initToolbar() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        searchText = ((String) bundle.get(Constants.SEARCH_TEXT));
        if (!TextUtils.isEmpty(searchText)) {
            mTitleTv.setText(searchText);
        }

        StatusBarUtil.setStatusColor(getWindow(), ContextCompat.getColor(this, R.color.search_status_bar_white), 1f);
        if (mPresenter.getNightModeState()) {
            mToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.blue_gradient_bg));
            setToolbarView(R.color.white, R.drawable.ic_arrow_back_white_24dp);
        } else {
            StatusBarUtil.setStatusDarkColor(getWindow());
            mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            setToolbarView(R.color.title_black, R.drawable.ic_arrow_back_grey_24dp);
        }
        mToolbar.setNavigationOnClickListener(v -> onBackPressedSupport());
    }

    /**
     * 初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mPresenter.getSearchList(mCurrentPage, searchText, true);
        initRecyclerView();
        setRefresh();
        if (CommonUtils.isNetworkConnected()) {
            showLoading();
        }
    }

    /**
     * 定义重新加载的逻辑
     */
    @Override
    public void reload() {
        if (mPresenter != null) {
            mPresenter.getSearchList(0, searchText, false);
        }
    }

    /**
     * UI方面，展示搜索列表界面
     * @param feedArticleListData
     */
    @Override
    public void showSearchList(FeedArticleListData feedArticleListData) {
        mArticleList = feedArticleListData.getDatas();
        if (isAddData) {
            if (mArticleList.size() > 0) {
                mAdapter.addData(mArticleList);
            } else {
                CommonUtils.showMessage(this, getString(R.string.load_more_no_data));
            }
        } else {
            mAdapter.replaceData(mArticleList);
        }
        showNormal();
    }

    /**
     * UI方面，展示收藏成功界面
     * @param position
     * @param feedArticleData
     * @param feedArticleListData
     */
    @Override
    public void showCollectArticleData(int position, FeedArticleData feedArticleData, FeedArticleListData feedArticleListData) {
        mAdapter.setData(position, feedArticleData);
        CommonUtils.showSnackMessage(this, getString(R.string.collect_success));
    }

    /**
     * UI方面，展示取消收藏成功界面
     * @param position
     * @param feedArticleData
     * @param feedArticleListData
     */
    @Override
    public void showCancelCollectArticleData(int position, FeedArticleData feedArticleData, FeedArticleListData feedArticleListData) {
        mAdapter.setData(position, feedArticleData);
        CommonUtils.showSnackMessage(this, getString(R.string.cancel_collect_success));
    }

    /**
     * 接收到内部收藏成功后的事件后的执行逻辑
     */
    @Override
    public void showCollectSuccess() {
        showCollectResult(true);
    }

    /**
     * 接收到内部取消收藏后的事件后的执行逻辑
     */
    @Override
    public void showCancelCollectSuccess() {
        showCollectResult(false);
    }

    /**
     * UI方面，直接界面展示收藏结果
     * @param collectResult
     */
    private void showCollectResult(boolean collectResult) {
        if (mAdapter.getData().size() > articlePosition) {
            mAdapter.getData().get(articlePosition).setCollect(collectResult);
            mAdapter.setData(articlePosition, mAdapter.getData().get(articlePosition));
        }
    }

    /**
     * 定义浮动按钮事件
     * @param view
     */
    @OnClick({R.id.search_list_floating_action_btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_list_floating_action_btn:
                mRecyclerView.smoothScrollToPosition(0);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mArticleList = new ArrayList<>();
        mAdapter = new ArticleListAdapter(R.layout.item_search_pager, mArticleList);
        mAdapter.isSearchPage();
        mAdapter.isNightMode(mPresenter.getNightModeState());
        mAdapter.setOnItemClickListener((adapter, view, position) -> startArticleDetailPager(view, position));
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> clickChildrenEvent(view, position));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 内部调用，定义item中子视图的点击事件
     * @param view
     * @param position
     */
    private void clickChildrenEvent(View view, int position) {
        switch (view.getId()) {
            case R.id.item_search_pager_chapterName:
                startSingleChapterPager(position);
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
     * 内部调用，定义点击了Tag后的逻辑，发送事件
     * @param position
     */
    private void clickTag(int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        String superChapterName = mAdapter.getData().get(position).getSuperChapterName();
        //点击了项目或导航
        if (superChapterName.contains(getString(R.string.open_project))) {
               //判断是否加载过再发送事件
                if(ProjectFragment.isLoaded()){
                    RxBus.getDefault().post(new SwitchProjectEvent());
                    onBackPressedSupport();
                }else{
                    onBackPressedSupport();
                }
        } else if (superChapterName.contains(getString(R.string.navigation))) {
                //判断是否加载过再发送事件
                if(NavigationFragment.isLoaded()){
                    RxBus.getDefault().post(new SwitchNavigationEvent());
                    onBackPressedSupport();
                }else{
                    onBackPressedSupport();
                }
        }
    }

    /**
     * 内部调用，定义点击了独立的章节后的逻辑
     * @param position
     */
    private void startSingleChapterPager(int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        JudgeUtils.startKnowledgeHierarchyDetailActivity(this,
                true,
                mAdapter.getData().get(position).getSuperChapterName(),
                mAdapter.getData().get(position).getChapterName(),
                mAdapter.getData().get(position).getChapterId());
    }

    /**
     * 定义item 点击事件
     * @param view
     * @param position
     */
    private void startArticleDetailPager(View view, int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        articlePosition = position;
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, getString(R.string.share_view));
        JudgeUtils.startArticleDetailActivity(this,
                options,
                mAdapter.getData().get(position).getId(),
                mAdapter.getData().get(position).getTitle(),
                mAdapter.getData().get(position).getLink(),
                mAdapter.getData().get(position).isCollect(),
                false,
                false);
    }

    /**
     * 内部调用，设置标题栏根据主题色而改变的东西
     * @param textColor
     * @param navigationIcon
     */
    private void setToolbarView(@ColorRes int textColor, @DrawableRes int navigationIcon) {
        mTitleTv.setTextColor(ContextCompat.getColor(this, textColor));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, navigationIcon));
    }

    /**
     * 收藏点击事件
     * @param position
     */
    private void likeEvent(int position) {
        if (!mPresenter.getLoginStatus()) {
            startActivity(new Intent(this, LoginActivity.class));
            CommonUtils.showMessage(this, getString(R.string.login_tint));
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
     * 定义刷新的逻辑
     */
    private void setRefresh() {
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mCurrentPage = 0;
            isAddData = false;
            mPresenter.getSearchList(mCurrentPage, searchText, false);
            refreshLayout.finishRefresh(1000);
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mCurrentPage++;
            isAddData = true;
            mPresenter.getSearchList(mCurrentPage, searchText, false);
            refreshLayout.finishLoadMore(1000);
        });
    }
}
