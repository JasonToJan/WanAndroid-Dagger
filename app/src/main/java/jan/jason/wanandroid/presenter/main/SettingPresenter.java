package jan.jason.wanandroid.presenter.main;

import javax.inject.Inject;

import jan.jason.wanandroid.base.presenter.BasePresenter;
import jan.jason.wanandroid.contract.main.SettingContract;
import jan.jason.wanandroid.core.DataManager;

/**
 * @Description: 设置页面处理器
 * @Author: jasonjan
 * @Date: 2018/9/5 14:10
 */
public class SettingPresenter extends BasePresenter<SettingContract.View> implements SettingContract.Presenter{

    /**
     * 数据中心
     */
    private DataManager mDataManager;

    /**
     * 注入活力
     * @param dataManager
     */
    @Inject
    SettingPresenter(DataManager dataManager){
        super(dataManager);
        mDataManager=dataManager;
    }

    /**
     * 获取自动缓存 状态（true or false）
     * @return
     */
    @Override
    public boolean getAutoCacheState() {
        return mDataManager.getAutoCacheState();
    }

    /**
     * 获取无图模式 状态（true or false）
     * @return
     */
    @Override
    public boolean getNoImageState() {
        return mDataManager.getNoImageState();
    }

    /**
     * 获取夜间模式 状态（true or false）
     * @return
     */
    @Override
    public boolean getNightModeState() {
        return mDataManager.getNightModeState();
    }

    /**
     * 设置夜间模式 状态（true or false）
     * @param b
     */
    @Override
    public void setNightModeState(boolean b) {
        mDataManager.setNightModeState(b);
    }

    /**
     * 设置无图模式 状态（true or false）
     * @param b
     */
    @Override
    public void setNoImageState(boolean b) {
        mDataManager.setNoImageState(b);
    }

    /**
     * 设置自动缓存 状态（true or false）
     * @param b
     */
    @Override
    public void setAutoCacheState(boolean b) {
        mDataManager.setAutoCacheState(b);
    }
}
