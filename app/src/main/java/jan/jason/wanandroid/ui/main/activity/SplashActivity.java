package jan.jason.wanandroid.ui.main.activity;

import android.content.Intent;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.BindView;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.WanAndroidApp;
import jan.jason.wanandroid.base.activity.BaseActivity;
import jan.jason.wanandroid.contract.main.SplashContract;
import jan.jason.wanandroid.presenter.main.SplashPresenter;
import jan.jason.wanandroid.utils.StatusBarUtil;

/**
 * @Description: 启动页
 * @Author: jasonjan
 * @Date: 2018/9/3 10:49
 */
public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {

    @BindView(R.id.one_animation)
    LottieAnimationView mOneAnimation;
    @BindView(R.id.two_animation)
    LottieAnimationView mTwoAnimation;
    @BindView(R.id.three_animation)
    LottieAnimationView mThreeAnimation;
    @BindView(R.id.four_animation)
    LottieAnimationView mFourAnimation;
    @BindView(R.id.five_animation)
    LottieAnimationView mFiveAnimation;
    @BindView(R.id.six_animation)
    LottieAnimationView mSixAnimation;
    @BindView(R.id.seven_animation)
    LottieAnimationView mSevenAnimation;
    @BindView(R.id.eight_animation)
    LottieAnimationView mEightAnimation;
    @BindView(R.id.nine_animation)
    LottieAnimationView mNineAnimation;
    @BindView(R.id.ten_animation)
    LottieAnimationView mTenAnimation;

    @Override
    protected void onDestroy(){
        cancelAnimation();
        super.onDestroy();
    }

    /**
     * 获取布局id
     * @return
     */
    @Override
    protected int getLayoutId(){
        return R.layout.activity_splash;
    }

    @Override
    protected void initToolbar(){
        if(!WanAndroidApp.isFirstRun){
            jumpToMain();
            return;
        }
        //第一次运行的时候需要沉浸一下
        WanAndroidApp.isFirstRun=false;
        StatusBarUtil.immersive(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initEventAndData() {
        startAnimation(mOneAnimation, "W.json");
        startAnimation(mTwoAnimation, "A.json");
        startAnimation(mThreeAnimation, "N.json");
        startAnimation(mFourAnimation, "A.json");
        startAnimation(mFiveAnimation, "N.json");
        startAnimation(mSixAnimation, "D.json");
        startAnimation(mSevenAnimation, "R.json");
        startAnimation(mEightAnimation, "I.json");
        startAnimation(mNineAnimation, "O.json");
        startAnimation(mTenAnimation, "D.json");
    }

    /**
     * 取消所有动画
     */
    private void cancelAnimation() {
        cancelAnimation(mOneAnimation);
        cancelAnimation(mTwoAnimation);
        cancelAnimation(mThreeAnimation);
        cancelAnimation(mFourAnimation);
        cancelAnimation(mFiveAnimation);
        cancelAnimation(mSixAnimation);
        cancelAnimation(mSevenAnimation);
        cancelAnimation(mEightAnimation);
        cancelAnimation(mNineAnimation);
        cancelAnimation(mTenAnimation);
    }

    /**
     * 开启动画
     * @param mLottieAnimationView
     * @param animationName
     */
    private void startAnimation(LottieAnimationView mLottieAnimationView, String animationName) {
        mLottieAnimationView.setAnimation(animationName);
        mLottieAnimationView.playAnimation();
    }

    /**
     * 取消动画
     * @param mLottieAnimationView
     */
    private void cancelAnimation(LottieAnimationView mLottieAnimationView) {
        if (mLottieAnimationView != null) {
            mLottieAnimationView.cancelAnimation();
        }
    }

    /**
     * 跳转到主页
     */
    @Override
    public void jumpToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
