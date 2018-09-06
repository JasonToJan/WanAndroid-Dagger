package jan.jason.wanandroid.ui.hierarchy.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.activity.BaseActivity;
import jan.jason.wanandroid.base.fragment.BaseFragment;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.hierarchy.KnowledgeHierarchyDetailContract;
import jan.jason.wanandroid.core.bean.hierarchy.KnowledgeHierarchyData;
import jan.jason.wanandroid.core.event.KnowledgeJumpTopEvent;
import jan.jason.wanandroid.presenter.hierarchy.KnowledgeHierarchyDetailPresenter;
import jan.jason.wanandroid.ui.hierarchy.fragment.KnowledgeHierarchyListFragment;
import jan.jason.wanandroid.utils.StatusBarUtil;

/**
 * @Description: 文章章节详情页
 * @Author: jasonjan
 * @Date: 2018/9/3 17:22
 */
public class KnowledgeHierarchyDetailActivity extends BaseActivity<KnowledgeHierarchyDetailPresenter>
        implements KnowledgeHierarchyDetailContract.View {


    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.common_toolbar_title_tv)
    TextView mTitleTv;
    @BindView(R.id.knowledge_hierarchy_detail_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.knowledge_hierarchy_detail_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.knowledge_floating_action_btn)
    FloatingActionButton mFloatingActionButton;

    /**
     * 知识体系集合，用作标题
     */
    private List<KnowledgeHierarchyData> knowledgeHierarchyDataList;

    /**
     * 碎片集合，用作分页
     */
    private List<BaseFragment> mFragments = new ArrayList<>();

    /**
     * 章节名字，用作单独的标题
     */
    private String chapterName;

    /**
     * 父类调用，定义布局id
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_knowledge_hierarchy_detail;
    }

    /**
     * 父类调用，定义标题栏
     */
    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);
        StatusBarUtil.setStatusColor(getWindow(), ContextCompat.getColor(this, R.color.main_status_bar_blue), 1f);
        mToolbar.setNavigationOnClickListener(v -> onBackPressedSupport());
        if (getIntent().getBooleanExtra(Constants.IS_SINGLE_CHAPTER, false)) {
            startSingleChapterPager();
        } else {
            startNormalKnowledgeListPager();
        }
    }

    /**
     * 父类调用，初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
        initViewPagerAndTabLayout();
    }

    /**
     * 定义浮动按钮的逻辑
     * @param view
     */
    @OnClick({R.id.knowledge_floating_action_btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.knowledge_floating_action_btn:
                RxBus.getDefault().post(new KnowledgeJumpTopEvent());
                break;
            default:
                break;
        }
    }

    /**
     * 展示项目-这里逻辑不清楚
     */
    @Override
    public void showSwitchProject() {
        onBackPressedSupport();
    }

    /**
     * 展示导航-这里逻辑不清楚
     */
    @Override
    public void showSwitchNavigation() {
        onBackPressedSupport();
    }

    /**
     * 初始化分页和Tablayout关联
     */
    private void initViewPagerAndTabLayout() {
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments == null? 0 : mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (getIntent().getBooleanExtra(Constants.IS_SINGLE_CHAPTER, false)) {
                    return chapterName;
                } else {
                    return knowledgeHierarchyDataList.get(position).getName();
                }
            }
        });
        mTabLayout.setViewPager(mViewPager);
    }

    /**
     * 装载多个列表的知识体系页面（knowledge进入）
     */
    private void startNormalKnowledgeListPager() {
        KnowledgeHierarchyData knowledgeHierarchyData = (KnowledgeHierarchyData) getIntent().getSerializableExtra(Constants.ARG_PARAM1);
        if (knowledgeHierarchyData == null || knowledgeHierarchyData.getName() == null) {
            return;
        }
        mTitleTv.setText(knowledgeHierarchyData.getName().trim());
        knowledgeHierarchyDataList = knowledgeHierarchyData.getChildren();
        if (knowledgeHierarchyDataList == null) {
            return;
        }
        for (KnowledgeHierarchyData data : knowledgeHierarchyDataList) {
            mFragments.add(KnowledgeHierarchyListFragment.getInstance(data.getId(), null));
        }
    }

    /**
     * 装载单个列表的知识体系页面（tag进入）
     */
    private void startSingleChapterPager() {
        String superChapterName = getIntent().getStringExtra(Constants.SUPER_CHAPTER_NAME);
        chapterName = getIntent().getStringExtra(Constants.CHAPTER_NAME);
        int chapterId = getIntent().getIntExtra(Constants.CHAPTER_ID, 0);
        mTitleTv.setText(superChapterName);
        mFragments.add(KnowledgeHierarchyListFragment.getInstance(chapterId, null));
    }
}
