package jan.jason.wanandroid.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import javax.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.utils.CommonUtils;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Description: 抽象的简单片段
 * @Author: jasonjan
 * @Date: 2018/9/3 15:34
 */
public abstract class AbstractSimpleFragment extends SupportFragment{

    /**
     * 视图绑定相关类
     */
    private Unbinder unbinder;

    /**
     * 点击时间相关数据
     */
    private long clickTime;

    /**
     * 是否是内部片段
     */
    public boolean isInnerFragment;

    /**
     * 创建视图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view =inflater.inflate(getLayoutId(),container,false);
        unbinder= ButterKnife.bind(this,view);

        return view;
    }

    /**
     * 销毁视图
     */
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(unbinder!=null&&unbinder!=Unbinder.EMPTY){
            unbinder.unbind();
            unbinder=null;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        RefWatcher refWatcher= WanAndroidApp.getRefWatcher(_mActivity);
        refWatcher.watch(this);
    }

    /**
     * 懒加载回调方法
     * @param savedInstanceState
     */
    @Override
    public void onLazyInitView(Bundle savedInstanceState){
        super.onLazyInitView(savedInstanceState);
        initEventAndData();//没有这个什么数据也没有的
    }

    /**
     * 返回键监听
     * @return
     */
    @Override
    public boolean onBackPressedSupport(){
        if(getChildFragmentManager().getBackStackEntryCount()>1){
            popChild();
        }else {
            if(isInnerFragment){
                _mActivity.finish();
                return true;
            }
            long current=System.currentTimeMillis();
            if((current-clickTime)> Constants.DOUBLE_INTERVAL_TIME){
                CommonUtils.showSnackMessage(_mActivity,getString(R.string.double_click_exit_tint));
                clickTime=System.currentTimeMillis();
            }else{
                _mActivity.finish();
            }
        }
        return true;
    }

    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initEventAndData();
}
