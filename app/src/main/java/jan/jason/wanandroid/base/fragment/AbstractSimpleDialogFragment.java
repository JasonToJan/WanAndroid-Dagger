package jan.jason.wanandroid.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jan.jason.wanandroid.app.WanAndroidApp;

/**
 * @Description: 抽象的简单的Dialog类型碎片
 * @Author: jasonjan
 * @Date: 2018/9/4 20:55
 */
public abstract class AbstractSimpleDialogFragment extends DialogFragment{

    /**
     * 黄油刀实例
     */
    private Unbinder unBinder;

    /**
     * 根视图
     */
    public View mRootView;

    /**
     * 第一步静默执行的onCreateVie
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayout(), container, false);
        unBinder = ButterKnife.bind(this, mRootView);
        initEventAndData();

        return mRootView;
    }

    /**
     * 销毁视图前执行
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
    }

    /**
     * 销毁最后执行
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WanAndroidApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    /**
     * DialogFragment需要实现的关键展示函数
     * @param manager
     * @param tag
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            //防止连续点击add多个fragment
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayout();

    /**
     * 初始化数据，让子类去实现
     */
    protected abstract void initEventAndData();

}
