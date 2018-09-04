package jan.jason.wanandroid.core.bean.navigation;

import java.util.List;

import jan.jason.wanandroid.core.bean.main.collect.FeedArticleData;

/**
 * @Description: 导航列表实体
 * @Author: jasonjan
 * @Date: 2018/9/3 22:37
 */
public class NavigationListData {
    /**
     * "articles": [],
     * "cid": 272,
     * "name": "常用网站"
     */

    private List<FeedArticleData> articles;
    private int cid;
    private String name;

    public List<FeedArticleData> getArticles() {
        return articles;
    }

    public void setArticles(List<FeedArticleData> articles) {
        this.articles = articles;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
