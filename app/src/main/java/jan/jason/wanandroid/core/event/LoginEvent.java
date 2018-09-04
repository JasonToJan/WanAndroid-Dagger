package jan.jason.wanandroid.core.event;

/**
 * @Description: 登录事件
 * @Author: jasonjan
 * @Date: 2018/9/3 15:29
 */
public class LoginEvent {

    private boolean isLogin;

    public LoginEvent(boolean isLogin) {

        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

}
