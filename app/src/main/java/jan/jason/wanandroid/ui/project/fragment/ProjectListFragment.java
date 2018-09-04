package jan.jason.wanandroid.ui.project.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.fragment.BaseRootFragment;
import jan.jason.wanandroid.contract.project.ProjectListContract;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.bean.project.ProjectListData;
import jan.jason.wanandroid.presenter.project.ProjectListPresenter;
import jan.jason.wanandroid.ui.project.adapter.ProjectListAdapter;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.JudgeUtils;

/**
 * @Description: 项目页面的项目列表碎片
 *               用于分页显示项目
 * @Author: jasonjan
 * @Date: 2018/9/4 16:52
 */
public class ProjectListFragment extends BaseRootFragment<ProjectListPresenter> implements ProjectListContract.View{

    @BindView(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.project_list_recycler_view)
    RecyclerView mRecyclerView;

    /**
     * 文章集合
     */
    private List<FeedArticleData> mDatas;

    /**
     * 项目列表适配器
     */
    private ProjectListAdapter mAdapter;

    /**
     * 是否刷新
     */
    private boolean isRefresh = true;

    /**
     * 当前页码
     */
    private int mCurrentPage;

    /**
     * 当前分类id
     */
    private int cid;

    /**
     * 外部调用，生产一个ProjectListFragment实例
     * @param param1
     * @param param2
     * @return
     */
    public static ProjectListFragment getInstance(int param1, String param2) {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 父类调用，定义碎片布局
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_project_list;
    }

    /**
     * 父类调用，定义默认初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        setRefresh();
        Bundle bundle = getArguments();
        cid = bundle.getInt(Constants.ARG_PARAM1);
        initRecyclerView();
        mPresenter.getProjectListData(mCurrentPage, cid, true);
        if (CommonUtils.isNetworkConnected()) {
            showLoading();
        }
    }

    /**
     * 定义重新加载逻辑，默认从第一个分类id开始
     */
    @Override
    public void reload() {
        if (mPresenter != null) {
            mPresenter.getProjectListData(0, cid, false);
        }
    }

    /**
     * UI方面，显示项目列表
     * @param projectListData
     */
    @Override
    public void showProjectListData(ProjectListData projectListData){
        mDatas=projectListData.getDatas();
        if (isRefresh) {
            mAdapter.replaceData(mDatas);
        } else {
            if (mDatas.size() > 0) {
                mAdapter.addData(mDatas);
            } else {
                CommonUtils.showMessage(_mActivity, getString(R.string.load_more_no_data));
            }
        }
        showNormal();
    }

    /**
     * UI方面，显示收藏成功后的页面
     * @param position
     * @param feedArticleData
     * @param feedArticleListData
     */
    @Override
    public void showCollectOutsideArticle(int position, FeedArticleData feedArticleData, FeedArticleListData feedArticleListData) {
        mAdapter.setData(position, feedArticleData);
        CommonUtils.showSnackMessage(_mActivity, getString(R.string.collect_success));
    }

    /**
     * UI方面，显示取消收藏后的逻辑
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
     * UI方面，滑动到最顶端的逻辑
     */
    @Override
    public void showJumpToTheTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mDatas = new ArrayList<>();
        mAdapter = new ProjectListAdapter(R.layout.item_project_list, mDatas);
        mAdapter.setOnItemClickListener((adapter, view, position) -> startProjectPager(position));
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> clickChildEvent(view, position));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 内部调用，点击了item中的子视图后调用
     * @param view
     * @param position
     */
    private void clickChildEvent(View view, int position) {
        switch (view.getId()) {
            case R.id.item_project_list_install_tv:
                startInstallPager(position);
                break;
            default:
                break;
        }
    }

    /**
     * 内部调用，点击item中的install后执行
     * @param position
     */
    private void startInstallPager(int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        if (TextUtils.isEmpty(mAdapter.getData().get(position).getApkLink())) {
            return;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mAdapter.getData().get(position).getApkLink())));
    }

    /**
     * 内部调用，点击了item后进入详情页
     * @param position
     */
    private void startProjectPager(int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        JudgeUtils.startArticleDetailActivity(_mActivity,
                null,
                mAdapter.getData().get(position).getId(),
                mAdapter.getData().get(position).getTitle().trim(),
                mAdapter.getData().get(position).getLink().trim(),
                mAdapter.getData().get(position).isCollect(),
                false,
                true);
    }

    /**
     * 设置刷新逻辑
     */
    private void setRefresh() {
        mCurrentPage = 1;
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mCurrentPage = 1;
            isRefresh = true;
            mPresenter.getProjectListData(mCurrentPage, cid, false);
            refreshLayout.finishRefresh(1000);
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mCurrentPage++;
            isRefresh = false;
            mPresenter.getProjectListData(mCurrentPage, cid, false);
            refreshLayout.finishLoadMore(1000);
        });
    }
}
