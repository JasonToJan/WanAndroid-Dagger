package jan.jason.wanandroid.utils.logger;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jan.jason.wanandroid.utils.CommonUtils;

import static com.orhanobut.logger.Logger.ASSERT;
import static com.orhanobut.logger.Logger.ERROR;
import static com.orhanobut.logger.Logger.INFO;
import static com.orhanobut.logger.Logger.VERBOSE;
import static com.orhanobut.logger.Logger.WARN;
import static org.greenrobot.greendao.DaoLog.DEBUG;

/**
 * @Description: 文本转换日志策略
 * @Author: jasonjan
 * @Date: 2018/8/26 20:37
 */
public class TxtFormatStrategy implements FormatStrategy{

    /**
     * 获取行分隔符
     */
    private static final String NEW_LINE=System.getProperty("line.separator");

    /**
     * 空格
     */
    private static final String SEPARATOR=" ";

    /**
     * 日期
     */
    private final Date date;

    /**
     * 日期格式转换类
     */
    private final SimpleDateFormat dateFormat;

    /**
     * 日志策略
     */
    private final LogStrategy logStrategy;

    /**
     * tag
     */
    private final String tag;

    private TxtFormatStrategy(Builder builder){
        date=builder.date;
        dateFormat=builder.dateFormat;
        logStrategy=builder.logStrategy;
        tag=builder.tag;
    }

    /**
     * 外部调用
     * @return
     */
    public static Builder newBuilder(){
        return new Builder();
    }

    @Override
    public void log(int priority, @Nullable String onceOnlyTag, @NonNull String message) {
        String tag=formatTag(onceOnlyTag);

        date.setTime(System.currentTimeMillis());
        StringBuilder header=new StringBuilder();

        header.append(Long.toString(date.getTime()));//时间
        header.append(SEPARATOR);
        header.append(dateFormat.format(date));//时间转换
        header.append(SEPARATOR);
        header.append(logLevel(priority));//日志等级
        header.append(SEPARATOR);
        header.append(tag);//tag
        header.append(SEPARATOR);

        StringBuffer buffer=new StringBuffer();
        buffer.append(header);

        if(message.contains(NEW_LINE)){
            message=message.replaceAll(NEW_LINE,NEW_LINE+header.toString());//换行符替换
        }

        buffer.append(message);
        buffer.append(NEW_LINE);

        logStrategy.log(priority,tag,buffer.toString());
    }

    private String formatTag(String tag) {
        if (!TextUtils.isEmpty(tag) && !CommonUtils.isEquals(this.tag, tag)) {
            return this.tag + "-" + tag;
        }
        return this.tag;
    }

    public static final class Builder{
        /**
         * 日期类
         */
        Date date;

        /**
         * 简易日期转换类
         */
        SimpleDateFormat dateFormat;

        /**
         * 日志策略
         */
        LogStrategy logStrategy;

        /**
         * tag
         */
        String tag="PRETTY_LOGGER";

        private Builder(){

        }

        /**
         * 日期
         * @param val
         * @return
         */
        public Builder date(Date val){
            date=val;
            return this;
        }

        /**
         * 日期转换
         * @param val
         * @return
         */
        public Builder dateFormat(SimpleDateFormat val){
            dateFormat=val;
            return this;
        }

        /**
         * 日志策略
         * @param val
         * @return
         */
        public Builder logStrategy(LogStrategy val){
            logStrategy=val;
            return this;
        }

        /**
         * tag
         * @param tag
         * @return
         */
        public Builder tag(String tag){
            this.tag=tag;
            return this;
        }

        /**
         * 外部调用方法
         * @return
         */
        public TxtFormatStrategy build(String pkgName,String appName){
            if(date==null){
                date=new Date();
            }
            if(dateFormat==null){
                dateFormat=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK);
            }
            if(logStrategy==null){
                String diskPath= Environment.getExternalStorageDirectory().getAbsolutePath();
                String folder=diskPath+ File.separator+"Android"+File.separatorChar+"data"
                        +File.separatorChar+pkgName+File.separatorChar+"log"+File.separatorChar;

                //线程切换调用磁盘日志策略
                HandlerThread ht=new HandlerThread("AndroidFileLogger."+folder);
                ht.start();
                Handler handler=new DiskLogStrategy.WriteHandler(ht.getLooper(),folder,appName);
                logStrategy=new DiskLogStrategy(handler);
            }
            return new TxtFormatStrategy(this);
        }
    }

    /**
     * 日志等级
     * @return
     */
    private String logLevel(int value){
        switch (value){
            case VERBOSE:
                return "VERBOSE";
            case DEBUG:
                return "DEBUG";
            case INFO:
                return "INFO";
            case WARN:
                return "WARN";
            case ERROR:
                return "ERROR";
            case ASSERT:
                return "ASSERT";
            default:
                return "UNKNOWN";
        }
    }
}
