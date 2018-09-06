package jan.jason.wanandroid.app;

import android.graphics.Color;

import java.io.File;

import jan.jason.wanandroid.R;

/**
 * @Description: 定义了一些常量
 * @Author: jasonjan
 * @Date: 2018/8/23 21:30
 */
public class Constants {

    /**
     * 数据库名称
     */
    static final String DB_NAME = "aws_wan_android.db";

    /**
     * 腾讯Bugly统计
     */
    static final String  BUGLY_ID = "a29fb52485";

    /**
     * Tab 颜色值
     */
    public static final int[] TAB_COLORS = new int[]{
            Color.parseColor("#90C5F0"),
            Color.parseColor("#91CED5"),
            Color.parseColor("#F88F55"),
            Color.parseColor("#C0AFD0"),
            Color.parseColor("#E78F8F"),
            Color.parseColor("#67CCB7"),
            Color.parseColor("#F6BC7E")
    };

    /**
     * 标签碎片值
     */
    public static final int TYPE_MAIN_PAGER = 0;

    public static final int TYPE_KNOWLEDGE = 1;

    public static final int TYPE_NAVIGATION = 2;

    public static final int TYPE_PROJECT = 3;

    public static final int TYPE_COLLECT = 4;

    public static final int TYPE_SETTING = 5;

    /**
     * 双击时间限度
     */
    public static final long DOUBLE_INTERVAL_TIME = 2000;

    /**
     * intent 有时候需要用的参数1
     */
    public static final String ARG_PARAM1 = "param1";

    /**
     * intent 有时需要用的参数2
     */
    public static final String ARG_PARAM2 = "param2";

    /**
     * 网络请求返回的登录数据需用的
     */
    public static final String LOGIN_DATA = "login_data";

    /**
     * 网络请求返回的轮播图需要用的
     */
    public static final String BANNER_DATA = "banner_data";

    /**
     * 网络请求返回的文章数据需要用的
     */
    public static final String ARTICLE_DATA = "article_data";

    /**
     * 跳转到文章详情页需要使用的
     */
    public static final String ARTICLE_LINK = "article_link";

    public static final String ARTICLE_TITLE = "article_title";

    public static final String ARTICLE_ID = "article_id";

    public static final String IS_COLLECT = "is_collect";

    public static final String IS_COMMON_SITE = "is_common_site";

    public static final String IS_COLLECT_PAGE = "is_collect_page";

    /**
     * 跳转到章节详情需要使用的
     */
    public static final String CHAPTER_ID = "chapter_id";

    public static final String IS_SINGLE_CHAPTER = "is_single_chapter";

    public static final String CHAPTER_NAME = "is_chapter_name";

    public static final String SUPER_CHAPTER_NAME = "super_chapter_name";

    /**
     * Shared Preference key
     */
    public static final String MY_SHARED_PREFERENCE = "my_shared_preference";

    public static final String ACCOUNT = "account";

    public static final String PASSWORD = "password";

    public static final String LOGIN_STATUS = "login_status";

    public static final String AUTO_CACHE_STATE = "auto_cache_state";

    public static final String NO_IMAGE_STATE = "no_image_state";

    public static final String NIGHT_MODE_STATE = "night_mode_state";

    public static final String CURRENT_PAGE = "current_page";

    public static final String PROJECT_CURRENT_PAGE = "project_current_page";

    /**
     * url
     */
    public static final String COOKIE = "Cookie";

    /**
     * 数据存储路径
     */
    public static final String PATH_DATA = WanAndroidApp.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    /**
     * 网络缓存
     */
    public static final String PATH_CACHE = PATH_DATA + "/NetCache";

    /**
     * 三星手机标志 机型适配中会用到
     */
    public static final String SAMSUNG = "samsung";

    /**
     * 默认刷新的主题颜色，知识体系中的刷新
     */
    public static final int BLUE_THEME = R.color.colorPrimary;

    /**
     * 项目分类，默认选择第一个
     */
    public static final int TAB_ONE = 0;

    /**
     * 避免多次点击，中间的预留区域为1s
     */
    public static final long CLICK_TIME_AREA = 1000;

    /**
     * 搜索文字
     */
    public static final String SEARCH_TEXT = "search_text";

    /**
     * 反射实现菜单显示图标和文字
     */
    public static final String MENU_BUILDER = "MenuBuilder";

}
