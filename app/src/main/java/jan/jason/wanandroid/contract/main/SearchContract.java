package jan.jason.wanandroid.contract.main;

import java.util.List;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;
import jan.jason.wanandroid.core.bean.main.search.TopSearchData;
import jan.jason.wanandroid.core.dao.HistoryData;

/**
 * @Description: 搜索部分 视图和处理器关联接口
 * @Author: jasonjan
 * @Date: 2018/9/4 21:39
 */
public interface SearchContract {

    interface View extends AbstractView{

        /**
         * UI方面，显示历史数据
         */
        void showHistoryData(List<HistoryData> historyDataList);

        /**
         * UI方面，显示热搜
         * @param topSearchDataList
         */
        void showTopSearchData(List<TopSearchData> topSearchDataList);

        /**
         * UI方面，跳转到搜索列表
         */
        void judgeToTheSearchListActivity();
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 数据方面，获取所有的历史记录
         * @return
         */
        List<HistoryData> loadAllHistoryData();

        /**
         * 数据方面，添加一个string到历史记录
         * @param data
         */
        void addHistoryData(String data);

        /**
         * 数据方面，获取到热搜数据
         */
        void getTopSearchData();

        /**
         * 数据方面，清除历史搜索记录
         */
        void clearHistoryData();
    }
}
