package jan.jason.wanandroid.core.bean.main.login;

import java.util.List;

/**
 * @Description: 登录相关实体
 * @Author: jasonjan
 * @Date: 2018/9/3 17:35
 */
public class LoginData {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 图标
     */
    private String icon;

    /**
     * 类型
     */
    private int type;

    /**
     * 收藏记录
     */
    private List<Integer> collectIds;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<Integer> collectIds) {
        this.collectIds = collectIds;
    }
}
