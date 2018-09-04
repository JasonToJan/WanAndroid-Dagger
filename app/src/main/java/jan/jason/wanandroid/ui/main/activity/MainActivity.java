package jan.jason.wanandroid.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.activity.BaseActivity;
import jan.jason.wanandroid.base.fragment.BaseFragment;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.main.MainContract;
import jan.jason.wanandroid.core.event.LoginEvent;
import jan.jason.wanandroid.core.http.cookies.CookiesManager;
import jan.jason.wanandroid.presenter.main.MainPresenter;
import jan.jason.wanandroid.ui.hierarchy.fragment.KnowledgeHierarchyFragment;
import jan.jason.wanandroid.ui.main.fragment.SearchDialogFragment;
import jan.jason.wanandroid.ui.main.fragment.UsageDialogFragment;
import jan.jason.wanandroid.ui.mainpager.fragment.MainPagerFragment;
import jan.jason.wanandroid.ui.navigation.fragment.NavigationFragment;
import jan.jason.wanandroid.ui.project.fragment.ProjectFragment;
import jan.jason.wanandroid.utils.BottomNavigationViewHelper;
import jan.jason.wanandroid.utils.CommonAlertDialog;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.StatusBarUtil;

/**
 * @Description: 主页
 * @Author: jasonjan
 * @Date: 2018/9/3 10:49
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.common_toolbar_title_tv)
    TextView mTitleTv;
    @BindView(R.id.main_floating_action_btn)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.fragment_group)
    FrameLayout mFrameGroup;

    /**
     * 碎片集合
     */
    private ArrayList<BaseFragment> mFragments;

    /**
     * 左侧抽屉登录名
     */
    private TextView mUsTv;

    /**
     * 首页碎片
     */
    private MainPagerFragment mMainPagerFragment;

    /**
     * 知识体系碎片
     */
    private KnowledgeHierarchyFragment mKnowledgeHierarchyFragment;

    /**
     * 导航碎片
     */
    private NavigationFragment mNavigationFragment;

    /**
     * 项目碎片
     */
    private ProjectFragment mProjectFragment;

    /**
     * 记录最后一次打开的碎片索引
     */
    private int mLastFgIndex;

    /**
     * 有用的网址碎片
     */
    private UsageDialogFragment usageDialogFragment;

    /**
     * 搜索碎片
     */
    private SearchDialogFragment searchDialogFragment;

    /**
     * 第一步 活动开始的地方
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFragments=new ArrayList<BaseFragment>();
        if(savedInstanceState==null){
            mPresenter.setNightModeState(false);
            initPager(false, Constants.TYPE_MAIN_PAGER);
        }else{
            mBottomNavigationView.setSelectedItemId(R.id.tab_main_pager);
            initPager(true,Constants.TYPE_SETTING);
        }
    }

    /**
     * 第二步 加载布局文件
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 第三步 初始化标题栏
     */
    @Override
    protected void initToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar!=null;
        actionBar.setDisplayShowTitleEnabled(false);
        mTitleTv.setText(getString(R.string.home_pager));
        StatusBarUtil.setStatusColor(getWindow(), ContextCompat.getColor(this,R.color.main_status_bar_blue),1f);
        mToolbar.setNavigationOnClickListener(v -> onBackPressedSupport());
    }

    /**
     * 第四步 初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
    }

    /**
     * 第五步 创建一个标题菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_activity_main,menu);
        return true;
    }

    /**
     * 第六步 标题菜单点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_usage:
                if(usageDialogFragment==null){
                    usageDialogFragment=new UsageDialogFragment();
                }
                if(!isDestroyed()&&usageDialogFragment.isAdded()){
                   usageDialogFragment.dismiss();
                }
                usageDialogFragment.show(getSupportFragmentManager(),"UsageDialogFragment");
                break;
            case R.id.action_search:
                if(searchDialogFragment==null){
                    searchDialogFragment=new SearchDialogFragment();
                }
                if(!isDestroyed()&&searchDialogFragment.isAdded()){
                    searchDialogFragment.dismiss();
                }
                searchDialogFragment.show(getSupportFragmentManager(),"SearchDialogFragment");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 第七步 设置各种点击事件
     * @param view
     */
    @OnClick({R.id.main_floating_action_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.main_floating_action_btn:
                jumpToTheTop();
                break;
            default:
                break;
        }
    }

    /**
     * 第八步 设置选择项目栏目
     */
    @Override
    public void showSwitchProject(){
        if(mBottomNavigationView!=null){
            mBottomNavigationView.setSelectedItemId(R.id.tab_project);
        }
    }

    /**
     * 第九步 设置选择导航栏目
     */
    @Override
    public void showSwitchNavigation(){
        if(mBottomNavigationView!=null){
            mBottomNavigationView.setSelectedItemId(R.id.tab_navigation);
        }
    }

    /**
     * 第十步 设置展示登录视图
     */
    @Override
    public void showAutoLoginView(){
        showLoginView();
    }

    /**
     * 第十一步 真正设置抽屉部分登录的视图
     */
    @Override
    public void showLoginView(){
        if(mNavigationView==null){
            return;
        }
        mUsTv=mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_login_tv);
        mUsTv.setText(mPresenter.getLoginAccount());
        mUsTv.setOnClickListener(null);
        mNavigationView.getMenu().findItem(R.id.nav_item_logout).setVisible(true);
    }

    /**
     * 展示未登录的情况视图
     */
    @Override
    public void showLogoutView(){
        mUsTv.setText(R.string.login_in);
        mUsTv.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        if (mNavigationView == null) {
            return;
        }
        mNavigationView.getMenu().findItem(R.id.nav_item_logout).setVisible(false);
    }

    /**
     * 初始化页面
     * @param isRecreate 是否之前创建过
     * @param position 位置
     */
    private void initPager(boolean isRecreate,int position){
        mMainPagerFragment=MainPagerFragment.getInstance(isRecreate,null);
        mFragments.add(mMainPagerFragment);
        initFragments();
        init();
        switchFragment(position);
    }

    /**
     * 初始化页面的具体函数
     */
    private void init(){
        mPresenter.setCurrentPage(Constants.TYPE_MAIN_PAGER);
        initNavigationView();//抽屉
        initBottomNavigationView();//底部
        initDrawerLayout();//抽屉整体
    }

    /**
     * 初始化整体
     */
    private void initDrawerLayout(){
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView,float slideOffset){
                //获取mDrawerLayout中的第一个子布局，也就是布局中的RelativeLayout
                //获取抽屉的view
                View mContent = mDrawerLayout.getChildAt(0);
                float scale = 1 - slideOffset;
                float endScale = 0.8f + scale * 0.2f;
                float startScale = 1 - 0.3f * scale;

                //设置左边菜单滑动后的占据屏幕大小
                drawerView.setScaleX(startScale);
                drawerView.setScaleY(startScale);
                //设置菜单透明度
                drawerView.setAlpha(0.6f + 0.4f * (1 - scale));

                //设置内容界面水平和垂直方向偏转量
                //在滑动时内容界面的宽度为 屏幕宽度减去菜单界面所占宽度
                mContent.setTranslationX(drawerView.getMeasuredWidth() * (1 - scale));
                //设置内容界面操作无效（比如有button就会点击无效）
                mContent.invalidate();
                //设置右边菜单滑动后的占据屏幕大小
                mContent.setScaleX(endScale);
                mContent.setScaleY(endScale);
            }
        };
        toggle.syncState();
        mDrawerLayout.addDrawerListener(toggle);//设置抽屉监听
    }

    /**
     * 初始化底部视图以及点击事件
     */
    private void initBottomNavigationView(){
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);//文字图标一起出现
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.tab_main_pager:
                    loadPager(getString(R.string.home_pager),0,mMainPagerFragment,Constants.TYPE_MAIN_PAGER);
                    break;
                case R.id.tab_knowledge_hierarchy:
                    loadPager(getString(R.string.knowledge_hierarchy),1,mKnowledgeHierarchyFragment,Constants.TYPE_KNOWLEDGE);
                    break;
                case R.id.tab_navigation:
                    loadPager(getString(R.string.navigation),2,mNavigationFragment,Constants.TYPE_NAVIGATION);
                    break;
                case R.id.tab_project:
                    loadPager(getString(R.string.project),3,mProjectFragment, Constants.TYPE_PROJECT);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /**
     * 加载不同栏目
     * @param title
     * @param position
     * @param mFragment
     * @param pagerType
     */
    private void loadPager(String title,int position,BaseFragment mFragment,int pagerType){
        mTitleTv.setText(title);
        switchFragment(position);
        mFragment.reload();
        mPresenter.setCurrentPage(pagerType);
    }

    /**
     * 跳转到栏目顶部
     */
    private void jumpToTheTop() {
        switch (mPresenter.getCurrentPage()) {
            case Constants.TYPE_MAIN_PAGER:
                if (mMainPagerFragment != null) {
                    mMainPagerFragment.jumpToTheTop();
                }
                break;
            case Constants.TYPE_KNOWLEDGE:
                if (mKnowledgeHierarchyFragment != null) {
                    mKnowledgeHierarchyFragment.jumpToTheTop();
                }
                break;
            case Constants.TYPE_NAVIGATION:
                if (mNavigationFragment != null) {
                    mNavigationFragment.jumpToTheTop();
                }
                break;
            case Constants.TYPE_PROJECT:
                if (mProjectFragment != null) {
                    mProjectFragment.jumpToTheTop();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化碎片
     */
    private void initFragments() {
        mKnowledgeHierarchyFragment = KnowledgeHierarchyFragment.getInstance(null, null);
        mNavigationFragment = NavigationFragment.getInstance(null, null);
        mProjectFragment = ProjectFragment.getInstance(null, null);
       // CollectFragment collectFragment = CollectFragment.getInstance(null, null);
       // SettingFragment settingFragment = SettingFragment.getInstance(null, null);

       mFragments.add(mKnowledgeHierarchyFragment);
       mFragments.add(mNavigationFragment);
       mFragments.add(mProjectFragment);
       // mFragments.add(collectFragment);
       // mFragments.add(settingFragment);
    }

    /**
     * 切换碎片
     * @param position 需要显示的fragment的下标
     */
    private void switchFragment(int position){
        if (position >= Constants.TYPE_COLLECT) {
            mFloatingActionButton.setVisibility(View.INVISIBLE);
            mBottomNavigationView.setVisibility(View.INVISIBLE);
        } else {
            mFloatingActionButton.setVisibility(View.VISIBLE);
            mBottomNavigationView.setVisibility(View.VISIBLE);
        }
        if (position >= mFragments.size()) {
            return;
        }
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        Fragment targetFg=mFragments.get(position);//获取目标碎片
        Fragment lastFg=mFragments.get(mLastFgIndex);
        mLastFgIndex=position;
        ft.hide(lastFg);
        if(!targetFg.isAdded()){
            getSupportFragmentManager().beginTransaction().remove(targetFg).commit();
            ft.add(R.id.fragment_group,targetFg);
        }
        ft.show(targetFg);
        ft.commitAllowingStateLoss();//用户可以意外地更改UI,允许状态丢失
    }

    /**
     * 初始化抽屉业务逻辑
     */
    private void initNavigationView(){
        mUsTv=mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_login_tv);
        if(mPresenter.getLoginStatus()){
            showLoginView();
        }else{
            showLogoutView();
        }
        mNavigationView.getMenu().findItem(R.id.nav_item_wan_android)
                .setOnMenuItemClickListener(item -> {
                    startMainPager();//打开主页
                    return true;
                });
        mNavigationView.getMenu().findItem(R.id.nav_item_my_collect)
                .setOnMenuItemClickListener(item -> {
                    if(mPresenter.getLoginStatus()){
                        startCollectFragment();//打开收集碎片
                        return true;
                    }else{
                        //TODO 打开登录页
                       // startActivity(new Intent(this, LoginActivity.class));
                        CommonUtils.showMessage(this,getString(R.string.login_tint));
                        return true;
                    }
                });
        mNavigationView.getMenu().findItem(R.id.nav_item_about_us)
                .setOnMenuItemClickListener(item -> {
                    //TODO 跳转到关于我页面
                    //startActivity(new Intent(this, AboutUsActivity.class));
                    return true;
                });
        mNavigationView.getMenu().findItem(R.id.nav_item_logout)
                .setOnMenuItemClickListener(item -> {
                    logout();//退出登录
                    return true;
                });
        mNavigationView.getMenu().findItem(R.id.nav_item_setting)
                .setOnMenuItemClickListener(item -> {
                    startSettingFragment();//跳转到设置碎片
                    return true;
                });
    }

    /**
     * 跳转到设置片段
     */
    private void startSettingFragment(){
        mTitleTv.setText(getString(R.string.setting));
        switchFragment(Constants.TYPE_SETTING);
        mDrawerLayout.closeDrawers();
    }

    /**
     * 跳转到主页面
     */
    private void startMainPager(){
        mTitleTv.setText(getString(R.string.home_pager));
        mBottomNavigationView.setVisibility(View.VISIBLE);
        mBottomNavigationView.setSelectedItemId(R.id.tab_main_pager);
        mDrawerLayout.closeDrawers();
    }

    /**
     * 跳转到收集片段
     */
    private void startCollectFragment(){
        mTitleTv.setText(getString(R.string.collect));
        switchFragment(Constants.TYPE_COLLECT);
        mDrawerLayout.closeDrawers();
    }

    /**
     * 退出登录的具体逻辑
     */
    private void logout(){
        CommonAlertDialog.newInstance().showDialog(this,getString(R.string.logout_tint),getString(R.string.ok),getString(R.string.no),
                v -> confirmLogout(),
                v -> CommonAlertDialog.newInstance().cancelDialog(true));
    }

    /**
     * 确定退出登录的具体逻辑
     */
    private void confirmLogout(){
        CommonAlertDialog.newInstance().cancelDialog(true);
        mNavigationView.getMenu().findItem(R.id.nav_item_logout).setVisible(false);
        mPresenter.setLoginStatus(false);
        CookiesManager.clearAllCookies();//清除所有登录缓存的数据
        RxBus.getDefault().post(new LoginEvent(false));
        //TODO 跳转到登录页
        //startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * 返回键监听
     */
    @Override
    public void onBackPressedSupport(){
        if(getSupportFragmentManager().getBackStackEntryCount()>1){
            pop();//任务栈中弹出
        }else{
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //如果有弹出框，则最后进行销毁
        CommonAlertDialog.newInstance().cancelDialog(true);
    }


}
