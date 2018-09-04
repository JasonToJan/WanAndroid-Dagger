package jan.jason.wanandroid.utils;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.ui.hierarchy.activity.KnowledgeHierarchyDetailActivity;
import jan.jason.wanandroid.ui.main.activity.ArticleDetailActivity;
import jan.jason.wanandroid.ui.main.activity.SearchListActivity;

/**
 * @Description: 跳转到详情页工具类
 * @Author: jasonjan
 * @Date: 2018/9/3 18:37
 */
public class JudgeUtils {

    /**
     * 跳转到文章详情
     * @param mActivity
     * @param activityOptions
     * @param id
     * @param articleTitle
     * @param articleLink
     * @param isCollect
     * @param isCollectPage
     * @param isCommonSite
     */
    public static void startArticleDetailActivity(Context mActivity, ActivityOptions activityOptions, int id, String articleTitle,
                                                  String articleLink, boolean isCollect,
                                                  boolean isCollectPage, boolean isCommonSite) {
        Intent intent = new Intent(mActivity, ArticleDetailActivity.class);
        intent.putExtra(Constants.ARTICLE_ID, id);
        intent.putExtra(Constants.ARTICLE_TITLE, articleTitle);
        intent.putExtra(Constants.ARTICLE_LINK, articleLink);
        intent.putExtra(Constants.IS_COLLECT, isCollect);
        intent.putExtra(Constants.IS_COLLECT_PAGE, isCollectPage);
        intent.putExtra(Constants.IS_COMMON_SITE, isCommonSite);
        if (activityOptions != null && !Build.MANUFACTURER.contains("samsung") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.startActivity(intent, activityOptions.toBundle());
        } else {
            mActivity.startActivity(intent);
        }
    }

    /**
     * 跳转到知识体系详情页
     * @param mActivity
     * @param isSingleChapter
     * @param superChapterName
     * @param chapterName
     * @param chapterId
     */
    public static void startKnowledgeHierarchyDetailActivity(Context mActivity, boolean isSingleChapter,
                                                             String superChapterName, String chapterName, int chapterId) {
        Intent intent = new Intent(mActivity, KnowledgeHierarchyDetailActivity.class);
        intent.putExtra(Constants.IS_SINGLE_CHAPTER, isSingleChapter);
        intent.putExtra(Constants.SUPER_CHAPTER_NAME, superChapterName);
        intent.putExtra(Constants.CHAPTER_NAME, chapterName);
        intent.putExtra(Constants.CHAPTER_ID, chapterId);
        mActivity.startActivity(intent);
    }

    /**
     * 跳转到搜索列表
     * @param mActivity
     * @param searchText
     */
    public static void startSearchListActivity(Context mActivity, String searchText) {
        Intent intent = new Intent(mActivity, SearchListActivity.class);
        intent.putExtra(Constants.SEARCH_TEXT, searchText);
        mActivity.startActivity(intent);
    }

}
