package jan.jason.wanandroid.core.event;

/**
 * @Description: 选择项目事件
 * @Author: jasonjan
 * @Date: 2018/9/3 18:45
 */
public class SwitchProjectEvent {

    /**
     * 来自于哪里
     */
    String from;

    public SwitchProjectEvent(){}

    public SwitchProjectEvent(String fromWhere){
        this.from=fromWhere;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
