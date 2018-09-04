package jan.jason.wanandroid.core.db;

import java.util.List;

import jan.jason.wanandroid.core.dao.HistoryData;

/**
 * @Description: 数据库帮助接口
 * @Author: jasonjan
 * @Date: 2018/9/3 18:05
 */
public interface DbHelper {

    /**
     * 增加历史数据
     *
     * @param data  added string
     * @return  List<HistoryData>
     */
    List<HistoryData> addHistoryData(String data);

    /**
     * 清除历史记录
     * Clear search history data
     */
    void clearHistoryData();

    /**
     * 加载所有历史数据
     * Load all history data
     *
     * @return List<HistoryData>
     */
    List<HistoryData> loadAllHistoryData();
}
