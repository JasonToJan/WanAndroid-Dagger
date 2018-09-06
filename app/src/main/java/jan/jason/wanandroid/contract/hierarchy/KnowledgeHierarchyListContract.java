package jan.jason.wanandroid.contract.hierarchy;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;

/**
 * @Description: 知识体系中列表 需要使用视图和处理器相关接口类
 * @Author: jasonjan
 * @Date: 2018/9/5 21:34
 */
public interface KnowledgeHierarchyListContract {

    interface View extends AbstractView{

        /**
         * 展示知识体系中的列表
         * @param feedArticleListData
         */
        void showKnowledgeHierarchyDetailData(FeedArticleListData feedArticleListData);

        /**
         * 展示收藏某一篇文章后的界面
         * @param position
         * @param feedArticleData
         * @param feedArticleListData
         */
        void showCollectArticleData(int position, FeedArticleData feedArticleData,FeedArticleListData feedArticleListData);

        /**
         * UI方面，展示取消收藏一篇文章后的界面效果
         * @param position
         * @param feedArticleData
         * @param feedArticleListData
         */
        void showCancelCollectArticleData(int position,FeedArticleData feedArticleData,FeedArticleListData feedArticleListData);

        /**
         * UI方面，展示滑动到最顶端的效果
         */
        void showJumpTheTop();

        /**
         * UI方面，展示重新加载的效果
         */
        void showReloadDetailEvent();
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 数据方面，获取到知识体系中的列表数据，然后进行UI展示
         * @param page
         * @param cid
         * @param isShowError
         */
        void getKnowledgeHierarchyDetailData(int page,int cid,boolean isShowError);

        /**
         * 逻辑方面，执行增加一个收藏记录的逻辑
         * @param position
         * @param feedArticleData
         */
        void addCollectArticle(int position,FeedArticleData feedArticleData);

        /**
         * 逻辑方面，执行取消收藏一个记录的逻辑
         * @param position
         * @param feedArticleData
         */
        void cancelCollectArticle(int position,FeedArticleData feedArticleData);
    }
}
