package jan.jason.wanandroid.ui.main.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import jan.jason.wanandroid.R;
import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.base.activity.BaseActivity;
import jan.jason.wanandroid.contract.main.LoginContract;
import jan.jason.wanandroid.presenter.main.LoginPresenter;
import jan.jason.wanandroid.utils.CommonUtils;
import jan.jason.wanandroid.utils.StatusBarUtil;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.login_group)
    RelativeLayout mLoginGroup;
    @BindView(R.id.login_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.login_account_edit)
    EditText mAccountEdit;
    @BindView(R.id.login_password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.login_register_btn)
    Button mRegisterBtn;

    /**
     * 父类调用,定义布局id
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    /**
     * 父类调用，定义标题栏
     */
    @Override
    protected void initToolbar() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, mToolbar);
        mToolbar.setNavigationOnClickListener(v -> onBackPressedSupport());
    }

    /**
     * 父类调用，初始化事件和数据，注入登录点击事件
     */
    @Override
    protected void initEventAndData() {
        subscribeLoginClickEvent();
    }

    /**
     * UI方面，展示登录成功的逻辑
     */
    @Override
    public void showLoginSuccess() {
        CommonUtils.showMessage(this, getString(R.string.login_success));
        onBackPressedSupport();
    }

    /**
     * 定义点击事件，只有一个注册事件
     * @param v
     */
    @OnClick({R.id.login_register_btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register_btn:
                startRegisterPager();
                break;
            default:
                break;
        }
    }

    /**
     * 逻辑方面，跳转到注册页面
     */
    private void startRegisterPager() {
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(mRegisterBtn,
                mRegisterBtn.getWidth() / 2,
                mRegisterBtn.getHeight() / 2,
                0 ,
                0);
        startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
    }

    /**
     * 定义登录按钮事件
     */
    private void subscribeLoginClickEvent() {
        mPresenter.addRxBindingSubscribe(RxView.clicks(mLoginBtn)
                .throttleFirst(Constants.CLICK_TIME_AREA, TimeUnit.MILLISECONDS)
                .filter(o -> mPresenter != null)
                .subscribe(o -> mPresenter.getLoginData(
                        mAccountEdit.getText().toString().trim(),
                        mPasswordEdit.getText().toString().trim())));
    }


}
