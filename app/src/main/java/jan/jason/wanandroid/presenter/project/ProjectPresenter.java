package jan.jason.wanandroid.presenter.project;

import java.util.List;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.contract.project.ProjectContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.project.ProjectClassifyData;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 定义项目处理器
 * @Author: jasonjan
 * @Date: 2018/9/4 16:21
 */
public class ProjectPresenter extends BasePresenter<ProjectContract.View> implements ProjectContract.Presenter{

    /**
     * 数据管理员
     */
    private DataManager mDataManager;

    /**
     * 注入活力
     * @param dataManager
     */
    @Inject
    ProjectPresenter(DataManager dataManager){
        super(dataManager);
        this.mDataManager=dataManager;
    }

    /**
     * 逻辑方面，获取到数据，显示到UI
     */
    @Override
    public void getProjectClassifyData() {
        addSubscribe(mDataManager.getProjectClassifyData()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<ProjectClassifyData>>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_project_classify_data)) {
                    @Override
                    public void onNext(List<ProjectClassifyData> projectClassifyDataList) {
                        mView.showProjectClassifyData(projectClassifyDataList);
                    }
                }));
    }

    /**
     * 获取项目选择项
     * @return
     */
    @Override
    public int getProjectCurrentPage() {
        return mDataManager.getProjectCurrentPage();
    }

    /**
     * 设置项目选择项
     * @param page
     */
    @Override
    public void setProjectCurrentPage(int page) {
        mDataManager.setProjectCurrentPage(page);
    }
}
