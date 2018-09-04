package jan.jason.wanandroid.ui.main.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.fragment.BaseDialogFragment;
import jan.jason.wanandroid.contract.main.SearchContract;
import jan.jason.wanandroid.core.bean.main.search.TopSearchData;
import jan.jason.wanandroid.core.dao.HistoryData;
import jan.jason.wanandroid.presenter.main.SearchPresenter;
import jan.jason.wanandroid.ui.main.adapter.HistorySearchAdapter;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.JudgeUtils;
import jan.jason.wanandroid.utils.KeyBoardUtils;
import jan.jason.wanandroid.widget.CircularRevealAnim;

/**
 * @Description: 搜索片段
 * @Author: jasonjan
 * @Date: 2018/9/3 20:50
 */
public class SearchDialogFragment extends BaseDialogFragment<SearchPresenter> implements
        SearchContract.View,
        CircularRevealAnim.AnimListener,
        ViewTreeObserver.OnPreDrawListener{

    @BindView(R.id.search_back_ib)
    ImageButton mBackIb;
    @BindView(R.id.search_tint_tv)
    TextView mTintTv;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.search_tv)
    TextView mSearchTv;
    @BindView(R.id.search_history_clear_all_tv)
    TextView mClearAllHistoryTv;
    @BindView(R.id.search_scroll_view)
    NestedScrollView mSearchScrollView;
    @BindView(R.id.search_history_null_tint_tv)
    TextView mHistoryNullTintTv;
    @BindView(R.id.search_history_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.top_search_flow_layout)
    TagFlowLayout mTopSearchFlowLayout;
    @BindView(R.id.search_floating_action_btn)
    FloatingActionButton mFloatingActionButton;

    /**
     * 热搜数据集合
     */
    private List<TopSearchData> mTopSearchDataList;

    /**
     * 动画实例
     */
    private CircularRevealAnim mCircularRevealAnim;

    /**
     * 历史搜索记录适配器
     */
    private HistorySearchAdapter historySearchAdapter;

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
     * 开始执行，初始化对话框
     */
    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    /**
     * 父类调用，定义布局id
     * @return
     */
    @Override
    protected int getLayout() {
        return R.layout.fragment_search;
    }

    /**
     * 初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
        initCircleAnimation();
        mTopSearchDataList = new ArrayList<>();
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mSearchEdit.getText().toString())) {
                    mTintTv.setText(R.string.search_tint);
                } else {
                    mTintTv.setText("");
                }
            }
        });

        //监听搜索按钮点击事件
        mPresenter.addRxBindingSubscribe(RxView.clicks(mSearchTv)
                .throttleFirst(Constants.CLICK_TIME_AREA, TimeUnit.MILLISECONDS)
                .filter(o -> !TextUtils.isEmpty(mSearchEdit.getText().toString().trim()))
                .subscribe(o -> {
                    mPresenter.addHistoryData(mSearchEdit.getText().toString().trim());
                    setHistoryTvStatus(false);
                }));

        //UI方面显示历史记录
        showHistoryData(mPresenter.loadAllHistoryData());
        mPresenter.getTopSearchData();
    }

    /**
     * UI方面，显示历史记录
     * @param historyDataList
     */
    @Override
    public void showHistoryData(List<HistoryData> historyDataList) {
        if (historyDataList == null || historyDataList.size() <= 0) {
            setHistoryTvStatus(true);
            return;
        }
        setHistoryTvStatus(false);
        Collections.reverse(historyDataList);
        initRecyclerView(historyDataList);
    }

    /**
     * UI方面，显示热搜数据
     * @param topSearchDataList
     */
    @Override
    public void showTopSearchData(List<TopSearchData> topSearchDataList) {
        mTopSearchDataList = topSearchDataList;
        mTopSearchFlowLayout.setAdapter(new TagAdapter<TopSearchData>(mTopSearchDataList) {
            @Override
            public View getView(FlowLayout parent, int position, TopSearchData topSearchData) {
                assert getActivity() != null;
                TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.flow_layout_tv,
                        parent, false);
                assert topSearchData != null;
                String name = topSearchData.getName();
                tv.setText(name);
                setItemBackground(tv);
                mTopSearchFlowLayout.setOnTagClickListener((view, position1, parent1) -> {
                    showTopSearchView(position1);
                    return true;
                });
                return tv;
            }
        });
    }

    /**
     * UI方面，点击了item跳转到搜索列表
     */
    @Override
    public void judgeToTheSearchListActivity() {
        backEvent();
        JudgeUtils.startSearchListActivity(getActivity(), mSearchEdit.getText().toString().trim());
    }

    /**
     * UI方面，隐藏动画到结束
     */
    @Override
    public void onHideAnimationEnd() {
        mSearchEdit.setText("");
        dismissAllowingStateLoss();
    }

    /**
     * UI方面，显示动画到结束，打开软键盘
     */
    @Override
    public void onShowAnimationEnd() {
        KeyBoardUtils.openKeyboard(getActivity(), mSearchEdit);
    }

    /**
     * 开始绘制前执行
     * @return
     */
    @Override
    public boolean onPreDraw() {
        mSearchEdit.getViewTreeObserver().removeOnPreDrawListener(this);
        mCircularRevealAnim.show(mSearchEdit, mRootView);
        return true;
    }

    /**
     * 设置点击事件，一个是返回，一个是浮动按钮，一个是清除历史记录
     * @param view
     */
    @OnClick({R.id.search_back_ib, R.id.search_floating_action_btn, R.id.search_history_clear_all_tv})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back_ib:
                backEvent();
                break;
            case R.id.search_floating_action_btn:
                mSearchScrollView.smoothScrollTo(0, 0);
                break;
            case R.id.search_history_clear_all_tv:
                clearHistoryData();
                break;
            default:
                break;
        }
    }

    /**
     * 数据方面，清除历史记录
     */
    private void clearHistoryData() {
        mPresenter.clearHistoryData();
        historySearchAdapter.replaceData(new ArrayList<>());
        setHistoryTvStatus(true);
    }

    /**
     * UI方面，显示热搜
     * @param position1
     */
    private void showTopSearchView(int position1) {
        mPresenter.addHistoryData(mTopSearchDataList.get(position1).getName().trim());
        setHistoryTvStatus(false);
        mSearchEdit.setText(mTopSearchDataList.get(position1).getName().trim());
        mSearchEdit.setSelection(mSearchEdit.getText().length());
    }

    /**
     * 初始化RecyclerView，显示历史记录
     * @param historyDataList
     */
    private void initRecyclerView(List<HistoryData> historyDataList) {
        historySearchAdapter = new HistorySearchAdapter(R.layout.item_search_history, historyDataList);
        historySearchAdapter.setOnItemChildClickListener((adapter, view, position) -> searchHistoryData(adapter, position));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(historySearchAdapter);
    }

    /**
     * 搜索历史记录
     * @param adapter
     * @param position
     */
    private void searchHistoryData(BaseQuickAdapter adapter, int position) {
        HistoryData historyData = (HistoryData) adapter.getData().get(position);
        mPresenter.addHistoryData(historyData.getData());
        mSearchEdit.setText(historyData.getData());
        mSearchEdit.setSelection(mSearchEdit.getText().length());
        setHistoryTvStatus(false);
    }

    /**
     * UI方面，设置item背景颜色
     * @param tv
     */
    private void setItemBackground(TextView tv) {
        tv.setBackgroundColor(CommonUtils.randomTagColor());
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    /**
     * 初始化圆弧动画
     */
    private void initCircleAnimation() {
        mCircularRevealAnim = new CircularRevealAnim();
        mCircularRevealAnim.setAnimListener(this);
        mSearchEdit.getViewTreeObserver().addOnPreDrawListener(this);
    }

    /**
     * UI方面，初始化对话框
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
     * 定义返回事件
     */
    public void backEvent() {
        KeyBoardUtils.closeKeyboard(getActivity(), mSearchEdit);
        mCircularRevealAnim.hide(mSearchEdit, mRootView);
    }

    /**
     * UI方面，设置垃圾桶颜色
     * @param isClearAll
     */
    private void setHistoryTvStatus(boolean isClearAll) {
        mClearAllHistoryTv.setEnabled(!isClearAll);
        if (isClearAll) {
            setHistoryTvStatus(View.VISIBLE, R.color.search_grey_gone, R.drawable.ic_clear_all_gone);
        } else {
            setHistoryTvStatus(View.GONE, R.color.search_grey, R.drawable.ic_clear_all);
        }
    }

    /**
     * UI方面，设置垃圾桶颜色
     * @param visibility
     * @param textColor
     * @param clearDrawable
     */
    private void setHistoryTvStatus(int visibility, @ColorRes int textColor, @DrawableRes int clearDrawable) {
        Drawable drawable;
        mHistoryNullTintTv.setVisibility(visibility);
        mClearAllHistoryTv.setTextColor(ContextCompat.getColor(getActivity(), textColor));
        drawable = ContextCompat.getDrawable(getActivity(), clearDrawable);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mClearAllHistoryTv.setCompoundDrawables(drawable, null, null, null);
        mClearAllHistoryTv.setCompoundDrawablePadding(CommonUtils.dp2px(6));
    }

}
