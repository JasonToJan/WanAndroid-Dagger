package jan.jason.wanandroid.component;

import android.app.Activity;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description: 活动收集器，支持一键退出APP
 * @Author: jasonjan
 * @Date: 2018/8/27 21:19
 */
public class ActivityCollector {

    /**
     * 本类的一个实例
     */
    private static ActivityCollector activityCollector;

    /**
     * 活动集合
     */
    private Set<Activity> allActivities;

    /**
     * 获取单例
     * @return
     */
    public synchronized static ActivityCollector getInstance(){
        if(activityCollector==null){
            activityCollector=new ActivityCollector();
        }
        return activityCollector;
    }

    /**
     * 添加活动
     * @param act
     */
    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    /**
     * 移除活动
     * @param act
     */
    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    /**
     * 退出APP
     */
    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
