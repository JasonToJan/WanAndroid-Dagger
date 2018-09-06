package jan.jason.wanandroid.presenter.hierarchy;

import javax.inject.Inject;

import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.hierarchy.KnowledgeHierarchyDetailContract;
import jan.jason.wanandroid.core.DataManager;
import jan.jason.wanandroid.core.event.SwitchNavigationEvent;
import jan.jason.wanandroid.core.event.SwitchProjectEvent;

/**
 * @Description: 知识体系详情页 处理器
 * @Author: jasonjan
 * @Date: 2018/9/5 21:44
 */
public class KnowledgeHierarchyDetailPresenter extends BasePresenter<KnowledgeHierarchyDetailContract.View>
        implements KnowledgeHierarchyDetailContract.Presenter{

    /**
     * 注入活力
     * @param dataManager
     */
    @Inject
    KnowledgeHierarchyDetailPresenter(DataManager dataManager){
        super(dataManager);
    }

    /**
     * 视图未创建前执行
     * @param view
     */
    @Override
    public void attachView(KnowledgeHierarchyDetailContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    /**
     * 定义两个事件，如果别人发送了事件，则执行以下逻辑
     */
    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(SwitchProjectEvent.class)
                .subscribe(switchProjectEvent -> mView.showSwitchProject()));

        addSubscribe(RxBus.getDefault().toFlowable(SwitchNavigationEvent.class)
                .subscribe(switchNavigationEvent -> mView.showSwitchNavigation()));
    }
}
