package jan.jason.wanandroid.contract.navigation;

import java.util.List;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.navigation.NavigationListData;

/**
 * @Description: 导航页面视图和处理器的关联接口
 * @Author: jasonjan
 * @Date: 2018/9/4 15:08
 */
public interface NavigationContract {

    interface View extends AbstractView{

        /**
         * UI方面，显示导航页面数据
         * @param navigationDataList
         */
        void showNavigationListData(List<NavigationListData> navigationDataList);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 逻辑方面，先获取数据，后显示视图
         * @param isShowError
         */
        void getNavigationListData(boolean isShowError);
    }
}
