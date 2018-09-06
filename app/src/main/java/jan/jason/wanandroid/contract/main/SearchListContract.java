package jan.jason.wanandroid.contract.main;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;

/**
 * @Description: 搜索结果 视图和处理器关联接口
 * @Author: jasonjan
 * @Date: 2018/9/6 9:36
 */
public interface SearchListContract {

    interface View extends AbstractView{

        /**
         * UI方面，展示搜索列表的界面
         * @param feedArticleListData
         */
        void showSearchList(FeedArticleListData feedArticleListData);

        /**
         * UI方面，展示收藏一篇文章的界面
         * @param position
         * @param feedArticleData
         * @param feedArticleListData
         */
        void showCollectArticleData(int position, FeedArticleData feedArticleData,FeedArticleListData feedArticleListData);

        /**
         * UI方面，展示取消收藏一篇文章的界面
         * @param position
         * @param feedArticleData
         * @param feedArticleListData
         */
        void showCancelCollectArticleData(int position,FeedArticleData feedArticleData,FeedArticleListData feedArticleListData);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 数据方面，获取搜索列表
         * @param page
         * @param k
         * @param isShowError
         */
        void getSearchList(int page,String k,boolean isShowError);

        /**
         * 逻辑方面，增加一篇收藏的文章
         * @param position
         * @param feedArticleData
         */
        void addCollectArticle(int position,FeedArticleData feedArticleData);

        /**
         * 逻辑方面，取消收藏一篇文章
         * @param position
         * @param feedArticleData
         */
        void cancelCollectArticle(int position,FeedArticleData feedArticleData);
    }
}
