package jan.jason.wanandroid.component;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * @Description: 模拟的事件发布总线
 * @Author: jasonjan
 * @Date: 2018/9/3 15:21
 */
public class RxBus {

    /**
     * 主题
     */
    private final FlowableProcessor<Object> bus;

    /**
     * 构造器
     * PublishSubject只会把订阅发送的时间点之后来自原始Flowable的数据  发射给  观察者
     */
    private RxBus(){
        bus= PublishProcessor.create().toSerialized();
    }

    /**
     * 返回一个单例
     * @return
     */
    public static RxBus getDefault(){
        return RxBusHolder.INSTANCE;
    }

    /**
     * 单例持有者
     */
    private static class RxBusHolder{
        private static final RxBus INSTANCE=new RxBus();
    }

    /**
     * 发射一个事件
     * @param o
     */
    public void post(Object o){
        bus.onNext(o);
    }

    /**
     * 根据传递的eventType 类型返回特定类型的被观察者
     * @param eventType Event类型
     * @param <T> 对应的Class类型
     * @return
     */
    public <T> Flowable<T> toFlowable(Class<T> eventType){
        return bus.ofType(eventType);
    }
}
