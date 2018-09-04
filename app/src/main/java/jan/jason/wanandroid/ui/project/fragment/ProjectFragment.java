package jan.jason.wanandroid.ui.project.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.fragment.BaseFragment;
import jan.jason.wanandroid.base.fragment.BaseRootFragment;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.project.ProjectContract;
import jan.jason.wanandroid.core.bean.project.ProjectClassifyData;
import jan.jason.wanandroid.core.event.JumpToTheTopEvent;
import jan.jason.wanandroid.presenter.project.ProjectPresenter;
import jan.jason.wanandroid.utils.CommonUtils;

/**
 * @Description: 项目碎片
 * @Author: jasonjan
 * @Date: 2018/9/3 20:43
 */
public class ProjectFragment extends BaseRootFragment<ProjectPresenter> implements ProjectContract.View {

    @BindView(R.id.project_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.project_divider)
    View mDivider;
    @BindView(R.id.project_viewpager)
    ViewPager mViewPager;

    /**
     * 项目分类集合
     */
    private List<ProjectClassifyData> mData;

    /**
     * 相应的片段集合，一个项目分类对应一个片段
     */
    private List<BaseFragment> mFragments = new ArrayList<>();

    /**
     * 当前选择的项目
     */
    private int currentPage;

    /**
     * 外部调用，生成一个ProjectFragment
     * @param param1
     * @param param2
     * @return
     */
    public static ProjectFragment getInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 父类调用，定义布局
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_project;
    }

    /**
     * 父类调用，定义初始化数据
     */
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mPresenter.getProjectClassifyData();
        currentPage = mPresenter.getProjectCurrentPage();
        if (CommonUtils.isNetworkConnected()) {
            showLoading();
        }
    }

    /**
     * UI方面，展示项目分类
     * @param projectClassifyDataList
     */
    @Override
    public void showProjectClassifyData(List<ProjectClassifyData> projectClassifyDataList) {
        if (mPresenter.getCurrentPage() == Constants.TYPE_PROJECT) {
            setChildViewVisibility(View.VISIBLE);
        } else {
            setChildViewVisibility(View.INVISIBLE);
        }
        mData = projectClassifyDataList;
        initViewPagerAndTabLayout();
        showNormal();
    }

    /**
     * UI方面，发生异常后显示
     */
    @Override
    public void showError() {
        setChildViewVisibility(View.INVISIBLE);
        super.showError();
    }

    /**
     * 定义点击重新加载的逻辑
     */
    @Override
    public void reload() {
        if (mPresenter != null && mTabLayout.getVisibility() == View.INVISIBLE) {
            mPresenter.getProjectClassifyData();
        }
    }

    /**
     * 内部调用，初始化分页和Tablayout
     */
    private void initViewPagerAndTabLayout(){

        for (ProjectClassifyData data : mData) {
            ProjectListFragment projectListFragment = ProjectListFragment.getInstance(data.getId(), null);
            mFragments.add(projectListFragment);
        }

        //设置分页适配器
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mData == null? 0 : mData.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mData.get(position).getName();
            }
        });

        //设置页面改变监听器
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(Constants.TAB_ONE);
    }

    /**
     * UI方面，设置子视图的显示或隐藏
     * @param visibility
     */
    private void setChildViewVisibility(int visibility) {
        mTabLayout.setVisibility(visibility);
        mDivider.setVisibility(visibility);
        mViewPager.setVisibility(visibility);
    }

    /**
     * 滑动到最顶部
     */
    public void jumpToTheTop() {
        if (mFragments != null) {
            RxBus.getDefault().post(new JumpToTheTopEvent());
        }
    }


    /**
     * 定义视图销毁后的逻辑
     */
    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.setProjectCurrentPage(currentPage);
        }
        super.onDestroyView();
    }
}
