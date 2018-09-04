package jan.jason.wanandroid.core.db;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.core.dao.DaoSession;
import jan.jason.wanandroid.core.dao.HistoryData;
import jan.jason.wanandroid.core.dao.HistoryDataDao;

/**
 * @Description: 数据库帮助类的实现
 * @Author: jasonjan
 * @Date: 2018/9/3 22:46
 */
public class DbHelperImpl implements DbHelper{

    /**
     * 历史记录条数
     */
    private static final int HISTORY_LIST_SIZE = 10;

    /**
     * 数据库Session
     */
    private DaoSession daoSession;

    /**
     * 历史数据集合
     */
    private List<HistoryData> historyDataList;

    /**
     * 数据字符串
     */
    private String data;

    /**
     * 历史数据实体
     */
    private HistoryData historyData;

    @Inject
    DbHelperImpl() {
        daoSession = WanAndroidApp.getInstance().getDaoSession();
    }

    @Override
    public List<HistoryData> addHistoryData(String data) {
        this.data = data;
        getHistoryDataList();
        createHistoryData();
        if (historyDataForward()) {
            return historyDataList;
        }

        if (historyDataList.size() < HISTORY_LIST_SIZE) {
            getHistoryDataDao().insert(historyData);
        } else {
            historyDataList.remove(0);
            historyDataList.add(historyData);
            getHistoryDataDao().deleteAll();
            getHistoryDataDao().insertInTx(historyDataList);
        }
        return historyDataList;
    }

    @Override
    public void clearHistoryData() {
        daoSession.getHistoryDataDao().deleteAll();
    }

    @Override
    public List<HistoryData> loadAllHistoryData() {
        return daoSession.getHistoryDataDao().loadAll();
    }

    /**
     * 历史数据前移
     *
     * @return 返回true表示查询的数据已存在，只需将其前移到第一项历史记录，否则需要增加新的历史记录
     */
    private boolean historyDataForward() {
        //重复搜索时进行历史记录前移
        Iterator<HistoryData> iterator = historyDataList.iterator();
        //不要在foreach循环中进行元素的remove、add操作，使用Iterator模式
        while (iterator.hasNext()) {
            HistoryData historyData1 = iterator.next();
            if (historyData1.getData().equals(data)) {
                historyDataList.remove(historyData1);
                historyDataList.add(historyData);
                getHistoryDataDao().deleteAll();
                getHistoryDataDao().insertInTx(historyDataList);
                return true;
            }
        }
        return false;
    }

    private void getHistoryDataList() {
        historyDataList = getHistoryDataDao().loadAll();
    }

    private void createHistoryData() {
        historyData = new HistoryData();
        historyData.setDate(System.currentTimeMillis());
        historyData.setData(data);
    }

    private HistoryDataDao getHistoryDataDao() {
        return daoSession.getHistoryDataDao();
    }

}
