package jan.jason.wanandroid.core.event;

/**
 * @Description: 选择导航栏事件
 * @Author: jasonjan
 * @Date: 2018/9/3 18:45
 */
public class SwitchNavigationEvent {

    String from;

    public SwitchNavigationEvent(){

    }

    public SwitchNavigationEvent(String fromWhere){
        this.from=fromWhere;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
