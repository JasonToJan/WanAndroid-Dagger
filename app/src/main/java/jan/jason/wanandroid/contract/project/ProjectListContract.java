package jan.jason.wanandroid.contract.project;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;
import jan.jason.wanandroid.core.bean.project.ProjectListData;

/**
 * @Description: 项目分页展示列表 处理器和视图的关联接口
 * @Author: jasonjan
 * @Date: 2018/9/4 16:55
 */
public interface ProjectListContract {

    interface View extends AbstractView {

        /**
         * UI方面，显示项目列表
         * @param projectListData
         */
        void showProjectListData(ProjectListData projectListData);


        /**
         * UI方面，显示收藏的文章
         */
        void showCollectOutsideArticle(int position, FeedArticleData feedArticleData, FeedArticleListData feedArticleListData);

        /**
         * UI方面，显示取消收藏的文章
         * @param position
         * @param feedArticleData
         * @param feedArticleListData
         */
        void showCancelCollectArticleData(int position,FeedArticleData feedArticleData,FeedArticleListData feedArticleListData);

        /**
         * Ui方面，滑动到顶部
         */
        void showJumpToTheTop();
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 数据方面，获取项目列表数据
         * @param page
         * @param cid
         * @param isShowError
         */
        void getProjectListData(int page,int cid,boolean isShowError);

        /**
         * 数据方面，增加收藏的文章
         * @param position
         * @param feedArticleData
         */
        void addCollectOutsideArticle(int position,FeedArticleData feedArticleData);

        /**
         * 数据方面，取消收藏文章
         * @param position
         * @param feedArticleData
         */
        void cancelCollectArticle(int position,FeedArticleData feedArticleData);
    }
}
