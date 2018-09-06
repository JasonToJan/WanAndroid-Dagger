package jan.jason.wanandroid.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.fragment.BaseFragment;
import jan.jason.wanandroid.component.ACache;
import jan.jason.wanandroid.component.RxBus;
import jan.jason.wanandroid.contract.main.SettingContract;
import jan.jason.wanandroid.core.event.NightModeEvent;
import jan.jason.wanandroid.presenter.main.SettingPresenter;
import jan.jason.wanandroid.utils.ShareUtil;

/**
 * @Description: 设置页面 碎片
 * @Author: jasonjan
 * @Date: 2018/9/5 14:04
 */
public class SettingFragment extends BaseFragment<SettingPresenter> implements
        SettingContract.View,CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.cb_setting_cache)
    AppCompatCheckBox mCbSettingCache;
    @BindView(R.id.cb_setting_image)
    AppCompatCheckBox mCbSettingImage;
    @BindView(R.id.cb_setting_night)
    AppCompatCheckBox mCbSettingNight;
    @BindView(R.id.ll_setting_feedback)
    TextView mLlSettingFeedback;
    @BindView(R.id.ll_setting_clear)
    LinearLayout mLlSettingClear;
    @BindView(R.id.tv_setting_clear)
    TextView mTvSettingClear;

    /**
     * 缓存文件
     */
    private File cacheFile;

    /**
     * 外部调用，生成出设置碎片实例
     * @param param1
     * @param param2
     * @return
     */
    public static SettingFragment getInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 父类调用，获取布局id
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    /**
     * 父类调用，初始化事件和数据
     */
    @Override
    protected void initEventAndData() {
        cacheFile = new File(Constants.PATH_CACHE);
        mTvSettingClear.setText(ACache.getCacheSize(cacheFile));
        mCbSettingCache.setChecked(mPresenter.getAutoCacheState());
        mCbSettingImage.setChecked(mPresenter.getNoImageState());
        mCbSettingNight.setChecked(mPresenter.getNightModeState());
        mCbSettingCache.setOnCheckedChangeListener(this);
        mCbSettingImage.setOnCheckedChangeListener(this);
        mCbSettingNight.setOnCheckedChangeListener(this);
    }

    /**
     * 控件点击事件
     * @param view
     */
    @OnClick({R.id.ll_setting_feedback, R.id.ll_setting_clear})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_feedback:
                ShareUtil.sendEmail(_mActivity, getString(R.string.send_email));
                break;
            case R.id.ll_setting_clear:
                clearCache();
                break;
            default:
                break;
        }
    }

    /**
     * 定义组件选择事件
     * @param compoundButton
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_setting_night:
                mPresenter.setNightModeState(b);
                RxBus.getDefault().post(new NightModeEvent(b));
                break;
            case R.id.cb_setting_image:
                mPresenter.setNoImageState(b);
                break;
            case R.id.cb_setting_cache:
                mPresenter.setAutoCacheState(b);
                break;
            default:
                break;
        }
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        ACache.deleteDir(cacheFile);
        mTvSettingClear.setText(ACache.getCacheSize(cacheFile));
    }
}
