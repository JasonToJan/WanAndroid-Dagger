package jan.jason.wanandroid.ui.main.fragment;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.base.fragment.BaseDialogFragment;
import jan.jason.wanandroid.contract.main.UsageDialogContract;
import jan.jason.wanandroid.core.bean.main.search.UsefulSiteData;
import jan.jason.wanandroid.presenter.main.UsageDialogPresenter;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.JudgeUtils;
import jan.jason.wanandroid.widget.CircularRevealAnim;

/**
 * @Description: 常用网站碎片
 * @Author: jasonjan
 * @Date: 2018/9/3 20:47
 */
public class UsageDialogFragment extends BaseDialogFragment<UsageDialogPresenter> implements
        UsageDialogContract.View,
        CircularRevealAnim.AnimListener,
        ViewTreeObserver.OnPreDrawListener{

    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.common_toolbar_title_tv)
    TextView mTitleTv;
    @BindView(R.id.useful_sites_flow_layout)
    TagFlowLayout mUsefulSitesFlowLayout;

    /**
     * 常用网站集合
     */
    private List<UsefulSiteData> mUsefulSiteDataList;

    /**
     * 圆弧动画实例
     */
    private CircularRevealAnim mCircularRevealAnim;

    /**
     * 率先执行
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
    }

    /**
     * 开始执行
     */
    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    /**
     * 内部执行，返回布局id
     * @return
     */
    @Override
    protected int getLayout() {
        return R.layout.fragment_usage;
    }

    /**
     * 内部执行，初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
        initCircleAnimation();
        initToolbar();
        mUsefulSiteDataList = new ArrayList<>();
        mPresenter.getUsefulSites();
    }

    /**
     * UI方面，显示常用网站数据
     * @param usefulSiteDataList
     */
    @Override
    public void showUsefulSites(List<UsefulSiteData> usefulSiteDataList) {
        mUsefulSiteDataList = usefulSiteDataList;
        mUsefulSitesFlowLayout.setAdapter(new TagAdapter<UsefulSiteData>(mUsefulSiteDataList) {
            @Override
            public View getView(FlowLayout parent, int position, UsefulSiteData usefulSiteData) {
                assert getActivity() != null;
                TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.flow_layout_tv,
                        parent, false);
                assert usefulSiteData != null;
                String name = usefulSiteData.getName();
                tv.setText(name);
                setItemBackground(tv);
                mUsefulSitesFlowLayout.setOnTagClickListener((view, position1, parent1) -> {
                    startUsefulSitePager(view, position1);
                    return true;
                });
                return tv;
            }
        });
    }

    /**
     * 隐藏动画
     */
    @Override
    public void onHideAnimationEnd() {
        dismissAllowingStateLoss();
    }

    /**
     * 打开动画
     */
    @Override
    public void onShowAnimationEnd() {
    }

    /**
     * 视图将要绘制，但是还没绘制时执行
     * @return
     */
    @Override
    public boolean onPreDraw() {
        mTitleTv.getViewTreeObserver().removeOnPreDrawListener(this);
        mCircularRevealAnim.show(mTitleTv, mRootView);
        return true;
    }

    /**
     * 内部调用，点击了常用网站中某一个后执行跳转
     * @param view
     * @param position1
     */
    private void startUsefulSitePager(View view, int position1) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, getString(R.string.share_view));
        JudgeUtils.startArticleDetailActivity(getActivity(),
                options,
                mUsefulSiteDataList.get(position1).getId(),
                mUsefulSiteDataList.get(position1).getName().trim(),
                mUsefulSiteDataList.get(position1).getLink().trim(),
                false,
                false,
                true);
    }

    /**
     * 设置item的背景颜色
     * @param tv
     */
    private void setItemBackground(TextView tv) {
        tv.setBackgroundColor(CommonUtils.randomTagColor());
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    /**
     * 初始化圆弧动画相关属性
     */
    private void initCircleAnimation() {
        mCircularRevealAnim = new CircularRevealAnim();
        mCircularRevealAnim.setAnimListener(this);
        mTitleTv.getViewTreeObserver().addOnPreDrawListener(this);
    }

    /**
     * 初始化对话框
     */
    private void initDialog() {
        Window window = getDialog().getWindow();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        //DialogSearch的宽
        int width = (int) (metrics.widthPixels * 0.98);
        assert window != null;
        window.setLayout(width, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.TOP);
        //取消过渡动画 , 使DialogSearch的出现更加平滑
        window.setWindowAnimations(R.style.DialogEmptyAnimation);
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        mTitleTv.setText(R.string.useful_sites);
        if (mPresenter.getNightModeState()) {
            setToolbarView(R.color.comment_text, R.color.colorCard, R.drawable.ic_arrow_back_white_24dp);
        } else {
            setToolbarView(R.color.title_black, R.color.white, R.drawable.ic_arrow_back_grey_24dp);
        }
        mToolbar.setNavigationOnClickListener(v -> mCircularRevealAnim.hide(mTitleTv, mRootView));
    }

    /**
     * 内部调用，设置标题栏视图
     * @param textColor
     * @param backgroundColor
     * @param navigationIcon
     */
    private void setToolbarView(@ColorRes int textColor, @ColorRes int backgroundColor, @DrawableRes int navigationIcon) {
        mTitleTv.setTextColor(ContextCompat.getColor(getContext(), textColor));
        mToolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), backgroundColor));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getContext(), navigationIcon));
    }

}
