package jan.jason.wanandroid.core;

import java.util.List;

import io.reactivex.Observable;
import jan.jason.wanandroid.core.bean.BaseResponse;
import jan.jason.wanandroid.core.bean.hierarchy.KnowledgeHierarchyData;
import jan.jason.wanandroid.core.bean.main.banner.BannerData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.bean.main.login.LoginData;
import jan.jason.wanandroid.core.bean.main.search.TopSearchData;
import jan.jason.wanandroid.core.bean.main.search.UsefulSiteData;
import jan.jason.wanandroid.core.bean.navigation.NavigationListData;
import jan.jason.wanandroid.core.bean.project.ProjectClassifyData;
import jan.jason.wanandroid.core.bean.project.ProjectListData;
import jan.jason.wanandroid.core.dao.HistoryData;
import jan.jason.wanandroid.core.db.DbHelper;
import jan.jason.wanandroid.core.http.HttpHelper;
import jan.jason.wanandroid.core.prefs.PreferenceHelper;

/**
 * @Description: 最核心的数据管理类
 *
 * @Author: jasonjan
 * @Date: 2018/8/28 7:58
 */
public class DataManager implements HttpHelper,DbHelper,PreferenceHelper{

    /**
     * 网络帮助接口类
     */
    private HttpHelper mHttpHelper;

    /**
     * 数据库帮助接口类
     */
    private DbHelper mDbHelper;

    /**
     * 文件存储接口帮助类
     */
    private PreferenceHelper mPreferenceHelper;

    public DataManager(HttpHelper httpHelper, DbHelper dbHelper, PreferenceHelper preferencesHelper) {
        mHttpHelper = httpHelper;
        mDbHelper = dbHelper;
        mPreferenceHelper = preferencesHelper;
    }

    /**
     * 获取文章列表
     * @param pageNum 页数
     * @return
     */
    @Override
    public Observable<BaseResponse<FeedArticleListData>> getFeedArticleList(int pageNum) {
        return mHttpHelper.getFeedArticleList(pageNum);
    }

    /**
     * 获取登录数据
     * @param username
     * @param password
     * @return
     */
    @Override
    public Observable<BaseResponse<LoginData>> getLoginData(String username, String password) {
        return mHttpHelper.getLoginData(username, password);
    }

    /**
     * 获取轮播图数据
     * @return
     */
    @Override
    public Observable<BaseResponse<List<BannerData>>> getBannerData() {
        return mHttpHelper.getBannerData();
    }

    /**
     * 增加收藏文章
     * @param id article id
     * @return
     */
    @Override
    public Observable<BaseResponse<FeedArticleListData>> addCollectArticle(int id) {
        return mHttpHelper.addCollectArticle(id);
    }

    /**
     * 取消收藏文章
     * @param id article id
     * @return
     */
    @Override
    public Observable<BaseResponse<FeedArticleListData>> cancelCollectArticle(int id) {
        return mHttpHelper.cancelCollectArticle(id);
    }

    /**
     * 生产出一个Observable包裹的文章列表
     * @param id article id
     * @return
     */
    @Override
    public Observable<BaseResponse<FeedArticleListData>> cancelCollectPageArticle(int id) {
        return mHttpHelper.cancelCollectPageArticle(id);
    }

    @Override
    public Observable<BaseResponse<FeedArticleListData>> getSearchList(int pageNum, String k) {
        return mHttpHelper.getSearchList(pageNum, k);
    }

    @Override
    public Observable<BaseResponse<List<TopSearchData>>> getTopSearchData() {
        return mHttpHelper.getTopSearchData();
    }

    @Override
    public Observable<BaseResponse<List<UsefulSiteData>>> getUsefulSites() {
        return mHttpHelper.getUsefulSites();
    }

    @Override
    public Observable<BaseResponse<List<KnowledgeHierarchyData>>> getKnowledgeHierarchyData() {
        return mHttpHelper.getKnowledgeHierarchyData();
    }

    @Override
    public Observable<BaseResponse<FeedArticleListData>> getKnowledgeHierarchyDetailData(int page, int cid) {
        return mHttpHelper.getKnowledgeHierarchyDetailData(page, cid);
    }

    @Override
    public Observable<BaseResponse<List<NavigationListData>>> getNavigationListData() {
        return mHttpHelper.getNavigationListData();
    }

    @Override
    public Observable<BaseResponse<List<ProjectClassifyData>>> getProjectClassifyData() {
        return mHttpHelper.getProjectClassifyData();
    }

    @Override
    public Observable<BaseResponse<ProjectListData>> getProjectListData(int page, int cid) {
        return mHttpHelper.getProjectListData(page, cid);
    }

    @Override
    public Observable<BaseResponse<LoginData>> getRegisterData(String username, String password, String repassword) {
        return mHttpHelper.getRegisterData(username, password, repassword);
    }

    @Override
    public Observable<BaseResponse<FeedArticleListData>> addCollectOutsideArticle(String title, String author, String link) {
        return mHttpHelper.addCollectOutsideArticle(title, author, link);
    }

    @Override
    public Observable<BaseResponse<FeedArticleListData>> getCollectList(int page) {
        return mHttpHelper.getCollectList(page);
    }



    //================================DbHelper或PreferenceHelper实现的方法===========================
    //==============================================================================================
    @Override
    public void setLoginAccount(String account) {
        mPreferenceHelper.setLoginAccount(account);
    }

    @Override
    public void setLoginPassword(String password) {
        mPreferenceHelper.setLoginPassword(password);
    }

    @Override
    public String getLoginAccount() {
        return mPreferenceHelper.getLoginAccount();
    }

    @Override
    public String getLoginPassword() {
        return mPreferenceHelper.getLoginPassword();
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        mPreferenceHelper.setLoginStatus(isLogin);
    }

    @Override
    public boolean getLoginStatus() {
        return mPreferenceHelper.getLoginStatus();
    }

    @Override
    public void setCookie(String domain, String cookie) {
        mPreferenceHelper.setCookie(domain, cookie);
    }

    @Override
    public String getCookie(String domain) {
        return mPreferenceHelper.getCookie(domain);
    }

    @Override
    public void setCurrentPage(int position) {
        mPreferenceHelper.setCurrentPage(position);
    }

    @Override
    public int getCurrentPage() {
        return mPreferenceHelper.getCurrentPage();
    }

    @Override
    public void setProjectCurrentPage(int position) {
        mPreferenceHelper.setProjectCurrentPage(position);
    }

    @Override
    public int getProjectCurrentPage() {
        return mPreferenceHelper.getProjectCurrentPage();
    }

    @Override
    public boolean getAutoCacheState() {
        return mPreferenceHelper.getAutoCacheState();
    }

    @Override
    public boolean getNoImageState() {
        return mPreferenceHelper.getNoImageState();
    }

    @Override
    public boolean getNightModeState() {
        return mPreferenceHelper.getNightModeState();
    }

    @Override
    public void setNightModeState(boolean b) {
        mPreferenceHelper.setNightModeState(b);
    }

    @Override
    public void setNoImageState(boolean b) {
        mPreferenceHelper.setNoImageState(b);
    }

    @Override
    public void setAutoCacheState(boolean b) {
        mPreferenceHelper.setAutoCacheState(b);
    }

    @Override
    public List<HistoryData> addHistoryData(String data) {
        return mDbHelper.addHistoryData(data);
    }

    @Override
    public void clearHistoryData() {
        mDbHelper.clearHistoryData();
    }

    @Override
    public List<HistoryData> loadAllHistoryData() {
        return mDbHelper.loadAllHistoryData();
    }


}
