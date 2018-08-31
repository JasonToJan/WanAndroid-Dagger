package jan.jason.wanandroid.base.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.base.presenter.BasePresenter;

/**
 * @Description: 在BaseActivity之外再增加了一些东西的基类
 * @Author: jasonjan
 * @Date: 2018/8/28 7:50
 */
public abstract class BaseRootActivity<T extends BasePresenter> extends BaseActivity<T>{

    /**
     * 正常的状态
     */
    private static final int NORMAL_STATE=0;

    /**
     * 加载状态
     */
    private static final int LOADING_STATE=1;

    /**
     * 错误的状态
     */
    private static final int ERROR_STATE=2;

    /**
     * 动画库实例
     */
    private LottieAnimationView mLoadingAnimation;

    /**
     * 错误发生时展示的视图
     */
    private View mErrorView;

    /**
     * 加载时展示的视图
     */
    private View mLoadingView;

    /**
     * 正常时展示的视图组
     */
    private ViewGroup mNormalView;

    /**
     * 当前状态，默认为正常的状态
     */
    private int currentState=NORMAL_STATE;

    /**
     * 初始化事件和数据，实现的是AbstractSimpleActivity中定义的方法
     */
    @Override
    protected void initEventAndData(){
        mNormalView=(ViewGroup) findViewById(R.id.normal_view);//找到某一个正常的view(一个SmartRefreshLayout)
        if(mNormalView==null){
            throw new IllegalStateException("can't find mNormalView");
        }
        if((mNormalView.getParent() instanceof ViewGroup)){
            throw new IllegalStateException("mNormalView should be a ViewGroup");
        }
        ViewGroup mParent=(ViewGroup)mNormalView.getParent();
        View.inflate(this,R.layout.loading_view,mParent);//将loading视图加入视图组
        View.inflate(this,R.layout.error_view,mParent);
        mLoadingView=mParent.findViewById(R.id.loading_group);
        mErrorView=mParent.findViewById(R.id.error_group);
        TextView reloadTv=mErrorView.findViewById(R.id.error_reload_tv);//异常时重新加载
        reloadTv.setOnClickListener(v->reload());
        mLoadingAnimation=mLoadingAnimation.findViewById(R.id.loading_animation);

        mErrorView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mNormalView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy(){
        if(mLoadingAnimation!=null){
            mLoadingAnimation.cancelAnimation();//取消动画
        }
        super.onDestroy();
    }

    @Override
    public void showLoading(){
        if(currentState==LOADING_STATE){
            return ;
        }
        hideCurrentView();
        currentState=LOADING_STATE;
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingAnimation.setAnimation("loading_bus.json");
        mLoadingAnimation.loop(true);
        mLoadingAnimation.playAnimation();
    }

    @Override
    public void showError(){
        if(currentState==ERROR_STATE){
            return;
        }
        hideCurrentView();
        currentState=ERROR_STATE;
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNormal(){
        if(currentState==NORMAL_STATE){
            return;
        }
        hideCurrentView();
        currentState=NORMAL_STATE;
        mNormalView.setVisibility(View.VISIBLE);
    }

    private void hideCurrentView() {
        switch (currentState) {
            case NORMAL_STATE:
                mNormalView.setVisibility(View.GONE);
                break;
            case LOADING_STATE:
                mLoadingAnimation.cancelAnimation();
                mLoadingView.setVisibility(View.GONE);
                break;
            case ERROR_STATE:
                mErrorView.setVisibility(View.GONE);
            default:
                break;
        }
    }
}
