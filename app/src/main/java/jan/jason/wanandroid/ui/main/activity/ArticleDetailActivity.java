package jan.jason.wanandroid.ui.main.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Method;

import butterknife.BindView;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.activity.BaseActivity;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.main.ArticleDetailContract;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.event.CollectEvent;
import jan.jason.wanandroid.presenter.main.ArticleDetailPresenter;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.StatusBarUtil;

/**
 * @Description: 文章详情页
 * @Author: jasonjan
 * @Date: 2018/9/3 17:22
 */
public class ArticleDetailActivity extends BaseActivity<ArticleDetailPresenter> implements ArticleDetailContract.View{

    @BindView(R.id.article_detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.article_detail_web_view)
    FrameLayout mWebContent;

    /**
     * 数据包
     */
    private Bundle bundle;

    /**
     * 菜单Item实例
     */
    private MenuItem mCollectItem;

    /**
     * 文章id
     */
    private int articleId;

    /**
     * 文章链接
     */
    private String articleLink;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 是否收藏过
     */
    private boolean isCollect;

    /**
     * 如果是普通站点，则加载没有收藏的标题栏，如果不是，则加载有收藏的标题栏
     */
    private boolean isCommonSite;

    /**
     * 是否收藏page，或者收藏普通的文章
     */
    private boolean isCollectPage;

    /**
     * 一个封装好的WebView
     */
    private AgentWeb mAgentWeb;

    /**
     * 生命周期-暂停
     */
    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    /**
     * 生命周期-继续
     */
    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    /**
     * 生命周期-销毁
     */
    @Override
    public void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    /**
     * 数据方面，首先获取上个页面传递过来的数据
     */
    private void getBundleData() {
        bundle = getIntent().getExtras();
        assert bundle != null;//TODO 这个无法理解
        title = (String) bundle.get(Constants.ARTICLE_TITLE);
        articleLink = (String) bundle.get(Constants.ARTICLE_LINK);
        articleId = ((int) bundle.get(Constants.ARTICLE_ID));
        isCommonSite = ((boolean) bundle.get(Constants.IS_COMMON_SITE));
        isCollect = ((boolean) bundle.get(Constants.IS_COLLECT));
        isCollectPage = ((boolean) bundle.get(Constants.IS_COLLECT_PAGE));
    }


    /**
     * 父类调用-获取布局id
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detail;
    }

    /**
     * 父类调用-初始化标题栏
     */
    @Override
    protected void initToolbar() {
        getBundleData();
        mToolbar.setTitle(Html.fromHtml(title));
        setSupportActionBar(mToolbar);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, mToolbar);
        //toolbar返回后才发送事件哦
        mToolbar.setNavigationOnClickListener(v -> {
            if (isCollect) {
                RxBus.getDefault().post(new CollectEvent(false));
            } else {
                RxBus.getDefault().post(new CollectEvent(true));
            }
            onBackPressedSupport();
        });
    }

    /**
     * 父类调用，初始化事件和数据
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initEventAndData() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mWebContent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setMainFrameErrorView(R.layout.webview_error_view, -1)
                .createAgentWeb()
                .ready()
                .go(articleLink);

        WebView mWebView = mAgentWeb.getWebCreator().getWebView();
        WebSettings mSettings = mWebView.getSettings();
        if (mPresenter.getNoImageState()) {
            mSettings.setBlockNetworkImage(true);
        } else {
            mSettings.setBlockNetworkImage(false);
        }
        if (mPresenter.getAutoCacheState()) {
            mSettings.setAppCacheEnabled(true);
            mSettings.setDomStorageEnabled(true);
            mSettings.setDatabaseEnabled(true);
            if (CommonUtils.isNetworkConnected()) {
                mSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                mSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
        } else {
            mSettings.setAppCacheEnabled(false);
            mSettings.setDomStorageEnabled(false);
            mSettings.setDatabaseEnabled(false);
            mSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        mSettings.setJavaScriptEnabled(true);
        mSettings.setSupportZoom(true);
        mSettings.setBuiltInZoomControls(true);
        //不显示缩放按钮
        mSettings.setDisplayZoomControls(false);
        //设置自适应屏幕，两者合用
        //将图片调整到适合WebView的大小
        mSettings.setUseWideViewPort(true);
        //缩放至屏幕的大小
        mSettings.setLoadWithOverviewMode(true);
        //自适应屏幕
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    /**
     * 重写返回按钮
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * 加载标题栏中的菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        bundle = getIntent().getExtras();
        assert bundle != null;
        isCommonSite = (boolean) bundle.get(Constants.IS_COMMON_SITE);
        if (!isCommonSite) {
            unCommonSiteEvent(menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_article_common, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 加载可以收藏的菜单
     * @param menu
     */
    private void unCommonSiteEvent(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acticle, menu);
        mCollectItem = menu.findItem(R.id.item_collect);
        if (isCollect) {
            mCollectItem.setTitle(getString(R.string.cancel_collect));
            mCollectItem.setIcon(R.mipmap.ic_toolbar_like_p);
        } else {
            mCollectItem.setTitle(getString(R.string.collect));
            mCollectItem.setIcon(R.mipmap.ic_toolbar_like_n);
        }
    }

    /**
     * 定义标题栏中的菜单事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_share:
                mPresenter.shareEventPermissionVerify(new RxPermissions(this));
                break;
            case R.id.item_collect:
                collectEvent();
                break;
            case R.id.item_system_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(articleLink)));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 反射实现，让菜单同时显示图标和文字
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (Constants.MENU_BUILDER.equalsIgnoreCase(menu.getClass().getSimpleName())) {
                try {
                    @SuppressLint("PrivateApi")
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 定义分享的实现
     */
    @Override
    public void shareEvent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_type_url, getString(R.string.app_name), title, articleLink));
        intent.setType("text/plain");
        startActivity(intent);
    }

    /**
     * 定义分享时的错误
     */
    @Override
    public void shareError() {
        CommonUtils.showSnackMessage(this, getString(R.string.write_permission_not_allowed));
    }

    /**
     * 返回监听
     */
    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            supportFinishAfterTransition();
        }
    }

    /**
     * UI方面，显示收藏文章后的画面
     * @param feedArticleListData
     */
    @Override
    public void showCollectArticleData(FeedArticleListData feedArticleListData) {
        isCollect = true;
        mCollectItem.setTitle(R.string.cancel_collect);
        mCollectItem.setIcon(R.mipmap.ic_toolbar_like_p);
        CommonUtils.showSnackMessage(this, getString(R.string.collect_success));
    }

    /**
     * UI方面，显示取消收藏的画面
     * @param feedArticleListData
     */
    @Override
    public void showCancelCollectArticleData(FeedArticleListData feedArticleListData) {
        isCollect = false;
        if (!isCollectPage) {
            mCollectItem.setTitle(R.string.collect);
        }
        mCollectItem.setIcon(R.mipmap.ic_toolbar_like_n);
        CommonUtils.showSnackMessage(this, getString(R.string.cancel_collect_success));
    }

    /**
     * 定义点击了收藏之后的逻辑
     */
    private void collectEvent() {
        if (!mPresenter.getLoginStatus()) {
            CommonUtils.showMessage(this, getString(R.string.login_tint));
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            if (mCollectItem.getTitle().equals(getString(R.string.collect))) {
                mPresenter.addCollectArticle(articleId);
            } else {
                if (isCollectPage) {
                    mPresenter.cancelCollectPageArticle(articleId);
                } else {
                    mPresenter.cancelCollectArticle(articleId);
                }
            }
        }
    }

}
