package jan.jason.wanandroid.widget.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description: 浮动按钮的行为控制器
 * @Author: jasonjan
 * @Date: 2018/9/3 10:34
 */
public class ScaleDownShowBehavior extends FloatingActionButton.Behavior{

    /**
     * 是否正在动画
     */
    private boolean isAnimateIng = false;

    /**
     * 是否已经显示
     */
    private boolean isShow = true;

    public ScaleDownShowBehavior(Context context, AttributeSet attrs){
        super();
    }

    /**
     * 该方法决定了当前控件是否能接收到其内部View(非并非是直接子View)滑动时的参数
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes
     * @return
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,FloatingActionButton child,View directTargetChild,View target,int nestedScrollAxes){
        return nestedScrollAxes== ViewCompat.SCROLL_AXIS_VERTICAL||super.onStartNestedScroll(coordinatorLayout,child,directTargetChild,target,nestedScrollAxes);
    }

    /**
     * 定义滑动行为-最关键部分
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,FloatingActionButton child,View target,int dxConsumed,int dyConsumed,int dxUnconsumed,int dyUnconsumed){

        //手指上滑，隐藏FAB
        if((dyConsumed>0||dyUnconsumed>0)&&!isAnimateIng&&isShow){
            AnimatorUtil.translateHide(child, new StateListener() {
                @Override
                public void onAnimationStart(View view) {
                    super.onAnimationStart(view);
                    isShow = false;
                }
            });
        }else if((dyConsumed<0||dyUnconsumed<0)&&!isAnimateIng&&!isShow){
            //手指下滑，显示FAB
            AnimatorUtil.translateShow(child, new StateListener() {
                @Override
                public void onAnimationStart(View view) {
                    super.onAnimationStart(view);
                    isShow = true;
                }
            });
        }
    }

    /**
     * 状态监听器
     */
    class StateListener implements ViewPropertyAnimatorListener{

        @Override
        public void onAnimationStart(View view){
            isAnimateIng=true;
        }

        @Override
        public void onAnimationEnd(View view){
            isAnimateIng=false;
        }

        @Override
        public void onAnimationCancel(View view){
            isAnimateIng=false;
        }
    }
}
