package jan.jason.wanandroid.di.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description: 设置本项目的Url注解
 * @Author: jasonjan
 * @Date: 2018/9/4 9:29
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface WanAndroidUrl {
}
