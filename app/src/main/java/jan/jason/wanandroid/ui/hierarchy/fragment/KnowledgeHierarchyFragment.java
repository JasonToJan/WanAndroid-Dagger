package jan.jason.wanandroid.ui.hierarchy.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
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
import jan.jason.wanandroid.contract.hierarchy.KnowledgeHierarchyContract;
import jan.jason.wanandroid.core.bean.hierarchy.KnowledgeHierarchyData;
import jan.jason.wanandroid.presenter.hierarchy.KnowledgeHierarchyPresenter;
import jan.jason.wanandroid.ui.hierarchy.activity.KnowledgeHierarchyDetailActivity;
import jan.jason.wanandroid.ui.hierarchy.adapter.KnowledgeHierarchyAdapter;
import jan.jason.wanandroid.utils.CommonUtils;

/**
 * @Description: 体系结构碎片
 * @Author: jasonjan
 * @Date: 2018/9/3 20:29
 */
public class KnowledgeHierarchyFragment extends BaseRootFragment<KnowledgeHierarchyPresenter> implements KnowledgeHierarchyContract.View{

    @BindView(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.knowledge_hierarchy_recycler_view)
    RecyclerView mRecyclerView;

    /**
     * 知识体系数据集合
     */
    private List<KnowledgeHierarchyData> mKnowledgeHierarchyDataList;

    /**
     * 知识体系页面需要用到的适配器
     */
    private KnowledgeHierarchyAdapter mAdapter;

    /**
     * 是否刷新
     */
    private boolean isRefresh;

    /**
     * 给外部使用，生产出一个碎片
     * @param param1
     * @param param2
     * @return
     */
    public static KnowledgeHierarchyFragment getInstance(String param1, String param2) {
        KnowledgeHierarchyFragment fragment = new KnowledgeHierarchyFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 传入整体的布局id
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowledge_hierarchy;
    }

    /**
     * 默认懒加载时会执行
     */
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        setRefresh();
        initRecyclerView();
        mPresenter.getKnowledgeHierarchyData(true);
        if (CommonUtils.isNetworkConnected()) {
            showLoading();
        }
    }

    /**
     * 视图方面上，将数据显示出来的逻辑
     * @param knowledgeHierarchyDataList
     */
    @Override
    public void showKnowledgeHierarchyData(List<KnowledgeHierarchyData> knowledgeHierarchyDataList) {
        if (mPresenter.getCurrentPage() == 1) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
        if (mAdapter.getData().size() < knowledgeHierarchyDataList.size()) {
            mKnowledgeHierarchyDataList = knowledgeHierarchyDataList;
            mAdapter.replaceData(mKnowledgeHierarchyDataList);
        } else {
            if (!isRefresh) {
                CommonUtils.showMessage(_mActivity, getString(R.string.load_more_no_data));
            }
        }
        showNormal();
    }

    /**
     * UI方面，显示出错
     */
    @Override
    public void showError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        super.showError();
    }

    /**
     * 数据方面，进行重新加载
     */
    @Override
    public void reload() {
        if (mPresenter != null && mRecyclerView.getVisibility() == View.INVISIBLE) {
            mPresenter.getKnowledgeHierarchyData(false);
        }
    }

    /**
     * UI方面，滑动顶部
     */
    public void jumpToTheTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * UI方面，设置item的布局，以及设置item监听
     */
    private void initRecyclerView() {
        mAdapter = new KnowledgeHierarchyAdapter(R.layout.item_knowledge_hierarchy, mKnowledgeHierarchyDataList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> startDetailPager(view, position));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 逻辑方面，点击item进入详情页
     * @param view
     * @param position
     */
    private void startDetailPager(View view, int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() <= position) {
            return;
        }
        //设置场景动画
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(_mActivity, view, getString(R.string.share_view));
        Intent intent = new Intent(_mActivity, KnowledgeHierarchyDetailActivity.class);
        intent.putExtra(Constants.ARG_PARAM1, mAdapter.getData().get(position));
        if (modelFiltering()) {
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    /**
     * 机型适配
     *
     * @return 返回true表示非三星机型且Android 6.0以上
     */
    private boolean modelFiltering() {
        return !Build.MANUFACTURER.contains(Constants.SAMSUNG) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 逻辑方面，设置刷新的逻辑
     */
    private void setRefresh() {
        mRefreshLayout.setPrimaryColorsId(Constants.BLUE_THEME, R.color.white);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            isRefresh = true;
            mPresenter.getKnowledgeHierarchyData(false);
            refreshLayout.finishRefresh(1000);
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            isRefresh = false;
            mPresenter.getKnowledgeHierarchyData(false);
            refreshLayout.finishLoadMore(1000);
        });
    }
}
