package jan.jason.wanandroid.ui.navigation.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.fragment.BaseRootFragment;
import jan.jason.wanandroid.contract.navigation.NavigationContract;
import jan.jason.wanandroid.core.bean.navigation.NavigationListData;
import jan.jason.wanandroid.presenter.navigation.NavigationPresenter;
import jan.jason.wanandroid.ui.navigation.adapter.NavigationAdapter;
import jan.jason.wanandroid.utils.CommonUtils;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * @Description: 导航碎片
 * @Author: jasonjan
 * @Date: 2018/9/3 20:35
 */
public class NavigationFragment extends BaseRootFragment<NavigationPresenter> implements NavigationContract.View {

    @BindView(R.id.navigation_tab_layout)
    VerticalTabLayout mTabLayout;
    @BindView(R.id.normal_view)
    LinearLayout mNavigationGroup;
    @BindView(R.id.navigation_divider)
    View mDivider;
    @BindView(R.id.navigation_RecyclerView)
    RecyclerView mRecyclerView;

    /**
     * 线性视图管理员
     */
    private LinearLayoutManager mManager;

    /**
     * 是否需要滑动
     */
    private boolean needScroll;

    /**
     * 索引号
     */
    private int index;

    /**
     * 是否点击了左侧tab
     */
    private boolean isClickTab;

    /**
     * 是否加载过
     */
    private volatile static boolean isLoaded=false;

    /**
     * 外部调用，生产出一个导航碎片
     * @param param1
     * @param param2
     * @return
     */
    public static NavigationFragment getInstance(String param1, String param2) {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 是否加载过,
     * 在搜索页面可以根据是否加载，点击导航tag后，判断能否跳转到当前页
     * @return
     */
    public static boolean isLoaded(){
        return isLoaded;
    }

    /**
     * 定义碎片布局，在父类中已被调用
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation;
    }

    /**
     * 定义初始化事件和数据，在父类中默认执行
     */
    @Override
    protected void initEventAndData(){
        super.initEventAndData();
        mPresenter.getNavigationListData(true);
        if(CommonUtils.isNetworkConnected()){
            showLoading();
        }
        isLoaded=true;//加载过
    }

    /**
     * UI方面，显示数据
     * @param navigationDataList
     */
    @Override
    public void showNavigationListData(List<NavigationListData> navigationDataList){
        //定义左侧垂直布局样式
        mTabLayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return navigationDataList == null ? 0 : navigationDataList.size();
            }

            @Override
            public ITabView.TabBadge getBadge(int i) {
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int i) {
                return null;
            }

            @Override
            public ITabView.TabTitle getTitle(int i) {
                return new TabView.TabTitle.Builder()
                        .setContent(navigationDataList.get(i).getName())
                        .setTextColor(ContextCompat.getColor(_mActivity, R.color.shallow_green),
                                ContextCompat.getColor(_mActivity, R.color.shallow_grey))
                        .build();
            }

            @Override
            public int getBackground(int i) {
                return -1;
            }
        });

        if (mPresenter.getCurrentPage() == Constants.TYPE_NAVIGATION) {
            setChildViewVisibility(View.VISIBLE);
        } else {
            setChildViewVisibility(View.INVISIBLE);
        }
        initRecyclerView(navigationDataList);
        leftRightLinkage();
        showNormal();
    }

    /**
     * UI方面，定义发生错误时的显示情况
     */
    @Override
    public void showError() {
        mTabLayout.setVisibility(View.INVISIBLE);
        mNavigationGroup.setVisibility(View.INVISIBLE);
        mDivider.setVisibility(View.INVISIBLE);
        super.showError();
    }

    /**
     * 逻辑方面，定义重新加载的逻辑
     */
    @Override
    public void reload() {
        if (mPresenter != null && mNavigationGroup.getVisibility() == View.INVISIBLE) {
            mPresenter.getNavigationListData(false);
        }
    }

    /**
     * UI方面，定义item样式
     * @param navigationDataList
     */
    private void initRecyclerView(List<NavigationListData> navigationDataList) {
        NavigationAdapter adapter = new NavigationAdapter(R.layout.item_navigation, navigationDataList);
        mRecyclerView.setAdapter(adapter);
        mManager = new LinearLayoutManager(_mActivity);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 逻辑方面，将左右相关联起来，实现控制左右滑动
     */
    private void leftRightLinkage(){
        //RecyclerView设置滑动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (needScroll && (newState == RecyclerView.SCROLL_STATE_IDLE)) {
                    scrollRecyclerView();
                }
                rightLinkageLeft(newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (needScroll) {
                    scrollRecyclerView();
                }
            }
        });

        //设置tablayout设置选择监听
        mTabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tabView, int i) {
                isClickTab = true;
                selectTag(i);
            }

            @Override
            public void onTabReselected(TabView tabView, int i) {
            }
        });
    }

    /**
     * 滑动RecyclerView
     */
    private void scrollRecyclerView() {
        needScroll = false;
        int indexDistance = index - mManager.findFirstVisibleItemPosition();
        if (indexDistance >= 0 && indexDistance < mRecyclerView.getChildCount()) {
            int top = mRecyclerView.getChildAt(indexDistance).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        }
    }

    /**
     * UI方面，设置子视图的显示或隐藏
     * @param visibility
     */
    private void setChildViewVisibility(int visibility) {
        mNavigationGroup.setVisibility(visibility);
        mTabLayout.setVisibility(visibility);
        mDivider.setVisibility(visibility);
    }

    /**
     * 逻辑方面，右侧RecyclerView关联左侧TabLayout
     * @param newState
     */
    private void rightLinkageLeft(int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (isClickTab) {
                isClickTab = false;
                return;
            }
            int firstPosition = mManager.findFirstVisibleItemPosition();
            if (index != firstPosition) {
                index = firstPosition;
                setChecked(index);
            }
        }
    }

    /**
     * 选左边，右边跟着选
     * @param i
     */
    private void selectTag(int i) {
        index = i;
        mRecyclerView.stopScroll();
        smoothScrollToPosition(i);
    }

    /**
     * 滑右边，左边跟着选
     * @param position
     */
    private void setChecked(int position) {
        if (isClickTab) {
            isClickTab = false;
        } else {
            if (mTabLayout == null) {
                return;
            }
            mTabLayout.setTabSelected(index);
        }
        index = position;
    }

    /**
     * 定义右边滑动逻辑
     * @param currentPosition
     */
    private void smoothScrollToPosition(int currentPosition) {
        int firstPosition = mManager.findFirstVisibleItemPosition();
        int lastPosition = mManager.findLastVisibleItemPosition();
        if (currentPosition <= firstPosition) {
            mRecyclerView.smoothScrollToPosition(currentPosition);
        } else if (currentPosition <= lastPosition) {
            int top = mRecyclerView.getChildAt(currentPosition - firstPosition).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        } else {
            mRecyclerView.smoothScrollToPosition(currentPosition);
            needScroll = true;
        }
    }

    /**
     * 定义浮动按钮点击事件
     */
    public void jumpToTheTop() {
        if (mTabLayout != null) {
            mTabLayout.setTabSelected(0);
        }
    }

}
