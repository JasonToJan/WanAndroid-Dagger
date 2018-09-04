package jan.jason.wanandroid.contract.main;

import java.util.List;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.main.search.UsefulSiteData;

/**
 * @Description: 常用网站 视图和处理器关联接口
 * @Author: jasonjan
 * @Date: 2018/9/4 20:52
 */
public interface UsageDialogContract {

    interface View extends AbstractView{

        /**
         * UI方面，显示常用网站
         * @param usefulSiteDataList
         */
        void showUsefulSites(List<UsefulSiteData> usefulSiteDataList);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 数据方面，获取常用网站
         */
        void getUsefulSites();
    }
}
