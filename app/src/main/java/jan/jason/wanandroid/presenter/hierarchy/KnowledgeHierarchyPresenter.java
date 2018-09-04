package jan.jason.wanandroid.presenter.hierarchy;

import java.util.List;

import javax.inject.Inject;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.contract.hierarchy.KnowledgeHierarchyContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.bean.hierarchy.KnowledgeHierarchyData;
import jan.jason.wanandroid.utils.RxUtils;
import jan.jason.wanandroid.widget.BaseObserver;

/**
 * @Description: 知识体系处理器
 * @Author: jasonjan
 * @Date: 2018/9/4 14:08
 */
public class KnowledgeHierarchyPresenter extends BasePresenter<KnowledgeHierarchyContract.View> implements KnowledgeHierarchyContract.Presenter{

    /**
     * 数据来源，在构造函数中传入，而且涉及到注入
     */
    private DataManager mDataManager;

    /**
     * 注入一个数据实体，和dagger挂钩之处
     */
    @Inject
    KnowledgeHierarchyPresenter(DataManager dataManager){
        super(dataManager);
        this.mDataManager=dataManager;
    }

    /**
     * 获取到知识体系相关数据，并调用视图的显示函数
     * @param isShowError
     */
    @Override
    public void getKnowledgeHierarchyData(boolean isShowError){
        addSubscribe(mDataManager.getKnowledgeHierarchyData()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(RxUtils.handleResult())
                .subscribeWith(new BaseObserver<List<KnowledgeHierarchyData>>(mView,
                        WanAndroidApp.getInstance().getString(R.string.failed_to_obtain_knowledge_data),isShowError){
                    @Override
                    public void onNext(List<KnowledgeHierarchyData> knowledgeHierarchyDataList) {
                        mView.showKnowledgeHierarchyData(knowledgeHierarchyDataList);//视图显示
                    }
                }));
    }
}
