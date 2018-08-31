package jan.jason.wanandroid.core;

import jan.jason.wanandroid.core.prefs.PreferenceHelper;

/**
 * @Description: 最核心的数据管理类
 * //TODO 待补充
 * @Author: jasonjan
 * @Date: 2018/8/28 7:58
 */
public class DataManager implements PreferenceHelper{

    public DataManager(){

    }

    @Override
    public void setLoginAccount(String account) {
        //mPreferenceHelper.setLoginAccount(account);
    }

    @Override
    public void setLoginPassword(String password) {
        //mPreferenceHelper.setLoginPassword(password);
    }

    @Override
    public String getLoginAccount() {
        //return //mPreferenceHelper.getLoginAccount();
        return "";
    }

    @Override
    public String getLoginPassword() {
        //return //mPreferenceHelper.getLoginPassword();
        return "";
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        //mPreferenceHelper.setLoginStatus(isLogin);
    }

    @Override
    public boolean getLoginStatus() {
       // return //mPreferenceHelper.getLoginStatus();
        return false;
    }

    @Override
    public void setCurrentPage(int position) {
        //mPreferenceHelper.setCurrentPage(position);
    }

    @Override
    public int getCurrentPage() {
        //return //mPreferenceHelper.getCurrentPage();
        return 0;
    }

    @Override
    public boolean getAutoCacheState() {
       // return mPreferenceHelper.getAutoCacheState();
        return false;
    }

    @Override
    public boolean getNoImageState() {
        //return mPreferenceHelper.getNoImageState();
        return false;
    }

    @Override
    public boolean getNightModeState() {
        //return mPreferenceHelper.getNightModeState();
        return false;
    }

    @Override
    public void setNightModeState(boolean b) {
        //mPreferenceHelper.setNightModeState(b);
    }

    @Override
    public void setNoImageState(boolean b) {
        //mPreferenceHelper.setNoImageState(b);
    }

    @Override
    public void setAutoCacheState(boolean b) {
        // mPreferenceHelper.setAutoCacheState(b);
    }

    @Override
    public void setCookie(String domain, String cookie) {
       // mPreferenceHelper.setCookie(domain, cookie);
    }

    @Override
    public String getCookie(String domain) {
       // return mPreferenceHelper.getCookie(domain);
        return "";
    }

    @Override
    public void setProjectCurrentPage(int position) {
       // mPreferenceHelper.setProjectCurrentPage(position);
    }

    @Override
    public int getProjectCurrentPage() {
        return 0;
    }


}
