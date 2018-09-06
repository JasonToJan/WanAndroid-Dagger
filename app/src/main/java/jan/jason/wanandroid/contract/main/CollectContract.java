package jan.jason.wanandroid.contract.main;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;

/**
 * @Description: 收藏片段 视图和处理器关联接口的实现
 * @Author: jasonjan
 * @Date: 2018/9/5 9:39
 */
public interface CollectContract {

    interface View extends AbstractView{

        /**
         * UI方面，显示收藏列表
         * @param feedArticleListData
         */
        void showCollectList(FeedArticleListData feedArticleListData);

        /**
         * UI方面，显示取消收藏某一条记录后的列表
         * @param position
         * @param feedArticleData
         */
        void showCancelCollectPageArticleData(int position, FeedArticleData feedArticleData,FeedArticleListData feedArticleListData);

        /**
         * UI方面，显示刷新
         */
        void showRefreshEvent();
    }

    interface Presenter extends AbstractPresenter<View> {

        /**
         * 数据方面，获取收藏记录列表
         * @param page
         * @param isShowError
         */
        void getCollectList(int page,boolean isShowError);

        /**
         * 数据方面，取消收藏某一条记录
         * @param position
         * @param feedArticleData
         */
        void cancelCollectPageArticle(int position,FeedArticleData feedArticleData);
    }
}
