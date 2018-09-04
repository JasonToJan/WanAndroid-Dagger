package jan.jason.wanandroid.presenter.main;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.contract.main.SearchContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.main.search.TopSearchData;
import jan.jason.wanandroid.core.dao.HistoryData;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 搜索处理器
 * @Author: jasonjan
 * @Date: 2018/9/4 21:46
 */
public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter{

    /**
     * 数据管理员
     */
    private DataManager mDataManager;

    /**
     * 注入生命力
     * @param dataManager
     */
    @Inject
    SearchPresenter(DataManager dataManager){
        super(dataManager);
        this.mDataManager=dataManager;
    }

    /**
     * 数据方面，加载所有的历史记录
     * @return
     */
    @Override
    public List<HistoryData> loadAllHistoryData() {
        return mDataManager.loadAllHistoryData();
    }

    /**
     * 逻辑方面，添加搜索记录到本地，然后UI跳转到搜索列表
     * @param data
     */
    @Override
    public void addHistoryData(String data) {
        addSubscribe(Observable.create((ObservableOnSubscribe<List<HistoryData>>) e -> {
            List<HistoryData> historyDataList = mDataManager.addHistoryData(data);
            e.onNext(historyDataList);
        })
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(historyDataList ->
                        mView.judgeToTheSearchListActivity()));
    }

    /**
     * 清除历史记录
     */
    @Override
    public void clearHistoryData() {
        mDataManager.clearHistoryData();
    }

    /**
     * 获取热搜记录，然后UI进行显示
     */
    @Override
    public void getTopSearchData() {
        addSubscribe(mDataManager.getTopSearchData()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<TopSearchData>>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_top_data)) {
                    @Override
                    public void onNext(List<TopSearchData> topSearchDataList) {
                        mView.showTopSearchData(topSearchDataList);
                    }
                }));
    }

}
