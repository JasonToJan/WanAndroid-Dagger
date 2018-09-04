package jan.jason.wanandroid.contract.project;

import java.util.List;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.project.ProjectClassifyData;

/**
 * @Description: 项目栏目的 处理器和视图关联接口
 * @Author: jasonjan
 * @Date: 2018/9/4 16:16
 */
public interface ProjectContract {

    interface View extends AbstractView{

        /**
         * UI方面，显示项目分类
         * @param projectClassifyDataList
         */
        void showProjectClassifyData(List<ProjectClassifyData> projectClassifyDataList);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 数据方面，获取项目分类数据，但不返回
         */
        void getProjectClassifyData();

        /**
         * 获取项目页面当前页码，返回整型
         * @return
         */
        int getProjectCurrentPage();

        /**
         * 设置当前页码，传入整型
         * @param page
         */
        void setProjectCurrentPage(int page);
    }
}
