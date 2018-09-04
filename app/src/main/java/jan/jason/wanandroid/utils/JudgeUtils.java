package jan.jason.wanandroid.utils;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import jan.jason.wanandroid.app.Constants;
import jan.jason.wanandroid.ui.hierarchy.activity.KnowledgeHierarchyDetailActivity;
import jan.jason.wanandroid.ui.main.activity.ArticleDetailActivity;

/**
 * @Description: 跳转到详情页工具类
 * @Author: jasonjan
 * @Date: 2018/9/3 18:37
 */
public class JudgeUtils {
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

    public static void startKnowledgeHierarchyDetailActivity(Context mActivity, boolean isSingleChapter,
                                                             String superChapterName, String chapterName, int chapterId) {
        Intent intent = new Intent(mActivity, KnowledgeHierarchyDetailActivity.class);
        intent.putExtra(Constants.IS_SINGLE_CHAPTER, isSingleChapter);
        intent.putExtra(Constants.SUPER_CHAPTER_NAME, superChapterName);
        intent.putExtra(Constants.CHAPTER_NAME, chapterName);
        intent.putExtra(Constants.CHAPTER_ID, chapterId);
        mActivity.startActivity(intent);
    }

}
