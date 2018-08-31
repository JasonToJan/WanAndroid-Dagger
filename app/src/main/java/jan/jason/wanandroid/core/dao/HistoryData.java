package jan.jason.wanandroid.core.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Description: 历史数据，通过数据库存储
 * @Author: jasonjan
 * @Date: 2018/8/26 17:11
 */
@Entity
public class HistoryData {

    /**
     * 唯一表示id
     */
    @Id(autoincrement = true)
    private Long id;

    /**
     * 时间
     */
    private long date;

    /**
     * 数据
     */
    private String data;

    @Generated(hash = 1371145256)
    public HistoryData(Long id, long date, String data) {
        this.id = id;
        this.date = date;
        this.data = data;
    }

    @Generated(hash = 422767273)
    public HistoryData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
