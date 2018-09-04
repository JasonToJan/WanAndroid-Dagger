package jan.jason.wanandroid.widget;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;

/**
 * @Description: 圆弧缩放效果的动画
 * @Author: jasonjan
 * @Date: 2018/9/4 20:47
 */
public class CircularRevealAnim {

    /**
     * 持续时间为200ms
     */
    private static final long DURATION = 200;

    /**
     * 内部监听器
     */
    private AnimListener mListener = null;

    /**
     * 动画实例
     */
    private Animator anim;

    /**
     * 内部定义的动画监听器，一个是隐藏，一个是展示
     */
    public interface AnimListener {

        void onHideAnimationEnd();

        void onShowAnimationEnd();
    }

    /**
     * 关键控制视图函数
     * @param isShow
     * @param triggerView
     * @param animView
     */
    @SuppressLint("NewApi")
    private void actionOtherVisible(Boolean isShow, View triggerView, View animView) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (isShow) {
                animView.setVisibility(View.VISIBLE);
                if (mListener != null) {
                    anim.cancel();
                    anim = null;
                    mListener.onShowAnimationEnd();
                }
            } else {
                animView.setVisibility(View.GONE);
                if (mListener != null) {
                    anim.cancel();
                    anim = null;
                    mListener.onHideAnimationEnd();
                }
            }
            return;
        }

        /**
         * 计算 triggerView 的中心位置
         */
        int[] tvLocation = {0, 0};
        triggerView.getLocationInWindow(tvLocation);
        int tvX = (int) (tvLocation[0] + animView.getWidth() * 0.8);
        int tvY = tvLocation[1] + triggerView.getHeight() / 2;

        /**
         * 计算 animView 的中心位置
         */
        int[] avLocation = {0, 0};
        animView.getLocationInWindow(avLocation);
        int avX = avLocation[0] + animView.getWidth() / 2;
        int avY = avLocation[1] + animView.getHeight() / 2;

        int rippleW;
        if (tvX < avX) {
            rippleW = animView.getWidth() - tvX;
        } else {
            rippleW = tvX - avLocation[0];
        }

        int rippleH;
        if (tvY < avY) {
            rippleH = animView.getHeight() - tvY;
        } else {
            rippleH = tvY - avLocation[1];
        }

        float maxRadius = (float) Math.sqrt((double) (rippleW * rippleW + rippleH * rippleH));
        float startRadius;
        float endRadius;

        if (isShow) {
            startRadius = 0f;
            endRadius = maxRadius;
        } else {
            startRadius = maxRadius;
            endRadius = 0f;
        }

        anim = ViewAnimationUtils.createCircularReveal(animView, tvX, tvY, startRadius, endRadius);
        animView.setVisibility(View.VISIBLE);
        anim.setDuration(DURATION);
        anim.setInterpolator(new DecelerateInterpolator());

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isShow) {
                    animView.setVisibility(View.VISIBLE);
                    if (mListener != null) {
                        mListener.onShowAnimationEnd();
                    }
                } else {
                    animView.setVisibility(View.GONE);
                    if (mListener != null) {
                        mListener.onHideAnimationEnd();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        anim.start();
    }

    /**
     * 显示视图
     * @param triggerView
     * @param showView
     */
    public void show(View triggerView, View showView) {
        actionOtherVisible(true, triggerView, showView);
    }

    /**
     * 隐藏目标视图
     * @param triggerView
     * @param hideView
     */
    public void hide(View triggerView, View hideView) {
        actionOtherVisible(false, triggerView, hideView);
    }

    /**
     * 设置监听器
     * @param listener
     */
    public void setAnimListener(AnimListener listener) {
        mListener = listener;
    }
}
