package jan.jason.wanandroid.base.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.base.presenter.BasePresenter;

/**
 * @Description: 继承BaseFragment的高级别的另一个基类
 * @Author: jasonjan
 * @Date: 2018/9/3 16:16
 */
public abstract class BaseRootFragment<T extends BasePresenter> extends BaseFragment<T> {

    /**
     * 正常状态
     */
    private static final int NORMAL_STATE = 0;

    /**
     * 加载状态
     */
    private static final int LOADING_STATE = 1;

    /**
     * 错误状态
     */
    public static final int ERROR_STATE = 2;

    /**
     * 动画相关类
     */
    private LottieAnimationView mLoadingAnimation;

    /**
     * 错误显示视图
     */
    private View mErrorView;

    /**
     * 加载显示视图
     */
    private View mLoadingView;

    /**
     * 正常的视图组
     */
    private ViewGroup mNormalView;

    /**
     * 默认当前状态为正常状态
     */
    private int currentState = NORMAL_STATE;

    /**
     * 初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
        if (getView() == null) {
            return;
        }
        mNormalView = getView().findViewById(R.id.normal_view);
        if (mNormalView == null) {
            throw new IllegalStateException(
                    "The subclass of RootActivity must contain a View named 'mNormalView'.");
        }
        if (!(mNormalView.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException(
                    "mNormalView's ParentView should be a ViewGroup.");
        }
        ViewGroup parent = (ViewGroup) mNormalView.getParent();
        View.inflate(_mActivity, R.layout.loading_view, parent);
        View.inflate(_mActivity, R.layout.error_view, parent);
        mLoadingView = parent.findViewById(R.id.loading_group);
        mErrorView = parent.findViewById(R.id.error_group);
        TextView reloadTv = mErrorView.findViewById(R.id.error_reload_tv);
        reloadTv.setOnClickListener(v -> reload());
        mLoadingAnimation = mLoadingView.findViewById(R.id.loading_animation);
        mErrorView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mNormalView.setVisibility(View.VISIBLE);
    }

    /**
     * 销毁视图时调用动画的取消函数
     */
    @Override
    public void onDestroyView() {
        if (mLoadingAnimation != null) {
            mLoadingAnimation.cancelAnimation();
        }
        super.onDestroyView();
    }

    @Override
    public void showLoading() {
        if (currentState == LOADING_STATE) {
            return;
        }
        hideCurrentView();
        currentState = LOADING_STATE;
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingAnimation.setAnimation("loading_bus.json");
        mLoadingAnimation.loop(true);
        mLoadingAnimation.playAnimation();
    }

    @Override
    public void showError() {
        if (currentState == ERROR_STATE) {
            return;
        }
        hideCurrentView();
        currentState = ERROR_STATE;
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNormal() {
        if (currentState == NORMAL_STATE) {
            return;
        }
        hideCurrentView();
        currentState = NORMAL_STATE;
        mNormalView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏当前视图
     */
    private void hideCurrentView() {
        switch (currentState) {
            case NORMAL_STATE:
                if (mNormalView == null) {
                    return;
                }
                mNormalView.setVisibility(View.INVISIBLE);
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
