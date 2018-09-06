package jan.jason.wanandroid.contract.hierarchy;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;

/**
 * @Description: 知识体系 视图和处理器关联接口+
 *
 * @Author: jasonjan
 * @Date: 2018/9/5 21:27
 */
public interface KnowledgeHierarchyDetailContract {

    interface View extends AbstractView{

        /**
         * UI方面，展示选择的Project
         */
        void showSwitchProject();

        /**
         * UI方面，展示选择的Navigation
         */
        void showSwitchNavigation();
    }

    interface Presenter extends AbstractPresenter<View>{

    }
}
