package jan.jason.wanandroid.contract.main;

import jan.jason.wanandroid.base.presenter.AbstractPresenter;
import jan.jason.wanandroid.base.view.AbstractView;

/**
 * @Description: 设置部分 视图和处理器关联接口
 * @Author: jasonjan
 * @Date: 2018/9/5 14:05
 */
public interface SettingContract {

    interface View extends AbstractView{

    }

    interface Presenter extends AbstractPresenter<SettingContract.View>{

        /**
         * 获取自动缓存状态，true或者false
         * @return
         */
        boolean getAutoCacheState();

        /**
         * 获取是否是无图模式，true或者false
         * @return
         */
        boolean getNoImageState();

        /**
         * 设置是否是夜间模式
         * @param b
         */
        void setNightModeState(boolean b);

        /**
         * 设置是否是无图模式
         * @param b
         */
        void setNoImageState(boolean b);

        /**
         * 设置是否自动缓存模式
         * @param b
         */
        void setAutoCacheState(boolean b);
    }
}
