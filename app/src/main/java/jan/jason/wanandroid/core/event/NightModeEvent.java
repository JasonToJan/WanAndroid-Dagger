package jan.jason.wanandroid.core.event;

/**
 * @Description: 夜间模式选择事件
 * @Author: jasonjan
 * @Date: 2018/9/5 14:31
 */
public class NightModeEvent {

    private boolean isNightMode;

    public NightModeEvent(boolean isNightMode) {
        this.isNightMode = isNightMode;
    }

    public boolean isNightMode() {
        return isNightMode;
    }

    public void setNightMode(boolean nightMode) {
        isNightMode = nightMode;
    }
}
