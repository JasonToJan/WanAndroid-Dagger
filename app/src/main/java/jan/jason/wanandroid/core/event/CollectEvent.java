package jan.jason.wanandroid.core.event;

/**
 * @Description: 收集事件
 * @Author: jasonjan
 * @Date: 2018/9/3 15:59
 */
public class CollectEvent {
    private boolean isCancelCollectSuccess;

    public CollectEvent(boolean isCancelCollectSuccess) {
        this.isCancelCollectSuccess = isCancelCollectSuccess;
    }

    public boolean isCancelCollectSuccess() {
        return isCancelCollectSuccess;
    }

    public void setCancelCollectSuccess(boolean cancelCollectSuccess) {
        isCancelCollectSuccess = cancelCollectSuccess;
    }
}
