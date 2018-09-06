package jan.jason.wanandroid.contract.main;

import com.tbruyelle.rxpermissions2.RxPermissions;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.main.collect.FeedArticleListData;

/**
 * @Description: 文章详情 视图和处理器关联接口
 * @Author: jasonjan
 * @Date: 2018/9/5 16:34
 */
public interface ArticleDetailContract {

    interface View extends AbstractView{

        /**
         * UI方面，显示收藏过后，文章列表
         * @param feedArticleListData
         */
        void showCollectArticleData(FeedArticleListData feedArticleListData);

        /**
         * UI方面，显示取消收藏过后，文章列表
         * @param feedArticleListData
         */
        void showCancelCollectArticleData(FeedArticleListData feedArticleListData);

        /**
         * UI方面，点击分享后的视图方面的响应
         */
        void shareEvent();

        /**
         * UI方面，点击分享后，发生错误后的响应
         */
        void shareError();
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 数据方面，获取自动缓存状态（true or false）
         * @return
         */
        boolean getAutoCacheState();

        /**
         * 数据方面，获取无图模式状态
         * @return
         */
        boolean getNoImageState();

        /**
         * 逻辑方面，定义增加收藏记录的逻辑
         * @param id
         */
        void addCollectArticle(int id);

        /**
         * 逻辑方面，定义取消收藏记录
         * @param id
         */
        void cancelCollectArticle(int id);

        /**
         * 逻辑方面，定义取消收藏记录的逻辑
         * @param id
         */
        void cancelCollectPageArticle(int id);

        /**
         * 逻辑方面，定义分享事件的权限验证
         * @param rxPermissions
         */
        void shareEventPermissionVerify(RxPermissions rxPermissions);
    }
}
