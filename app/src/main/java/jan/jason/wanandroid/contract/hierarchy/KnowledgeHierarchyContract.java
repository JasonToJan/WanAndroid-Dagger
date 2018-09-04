package jan.jason.wanandroid.contract.hierarchy;

import java.util.List;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.hierarchy.KnowledgeHierarchyData;

/**
 * @Description: 知识体系页面相关的接口
 * @Author: jasonjan
 * @Date: 2018/9/4 14:03
 */
public interface KnowledgeHierarchyContract {

    interface View extends AbstractView{

        /**
         * 知识体系界面展示数据 接口定义
         * @param knowledgeHierarchyDataList
         */
        void showKnowledgeHierarchyData(List<KnowledgeHierarchyData> knowledgeHierarchyDataList);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 获取到知识体系数据，但不返回
         * @param isShowError
         */
        void getKnowledgeHierarchyData(boolean isShowError);
    }
}
