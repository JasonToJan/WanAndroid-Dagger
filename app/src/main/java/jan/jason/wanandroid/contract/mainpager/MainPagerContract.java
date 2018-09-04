package jan.jason.wanandroid.contract.mainpager;

import java.util.List;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.main.banner.BannerData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;

/**
 * @Description: 主页帮助接口
 * @Author: jasonjan
 * @Date: 2018/9/3 17:08
 */
public class MainPagerContract {

    public interface View extends AbstractView{

        /**
         * 展示自动登录成功
         */
        void showAutoLoginSuccess();

        /**
         * 展示自动登录失败
         */
        void showAutoLoginFail();

        /**
         * 展示文章列表
         * @param feedArticleListData
         * @param isRefresh
         */
        void showArticleList(FeedArticleListData feedArticleListData,boolean isRefresh);

        /**
         * 展示收藏的文章列表
         * @param position
         * @param feedArticleData
         * @param feedArticleListData
         */
        void showCollectArticleData(int position,FeedArticleData feedArticleData,FeedArticleListData feedArticleListData);

        /**
         * 展示取消收藏的文章列表
         * @param position
         * @param feedArticleData
         * @param feedArticleListData
         */
        void showCancelCollectArticleData(int position,FeedArticleData feedArticleData,FeedArticleListData feedArticleListData);

        /**
         * 展示轮播图列表
         * @param bannerDataList
         */
        void showBannerData(List<BannerData> bannerDataList);
    }

    public interface Presenter extends AbstractPresenter<View>{

        /**
         * 获取登录密码
         * @return
         */
        String getLoginPassword();

        /**
         * 加载主页数据
         */
        void loadMainPagerData();

        /**
         * 获取文章列表
         * @param isShowError
         */
        void getFeedArticleList(boolean isShowError);

        /**
         * 加载更多
         */
        void loadMoreData();

        /**
         * 增加收藏的文章
         * @param position
         * @param feedArticleData
         */
        void addCollectArticle(int position,FeedArticleData feedArticleData);

        /**
         * 取消收藏的文章
         * @param position
         * @param feedArticleData
         */
        void cancelCollectArticle(int position,FeedArticleData feedArticleData);

        /**
         * 获取轮播数据
         * @param isShowError
         */
        void getBannerData(boolean isShowError);

        /**
         * 自动刷新（如果出错了的话）
         * @param isShowError
         */
        void autoRefresh(boolean isShowError);

        /**
         * 加载更多
         */
        void loadMore();
    }
}
