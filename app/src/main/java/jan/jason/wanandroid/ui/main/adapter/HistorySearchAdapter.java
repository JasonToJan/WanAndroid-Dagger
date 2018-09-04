package jan.jason.wanandroid.ui.main.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.core.dao.HistoryData;
import jan.jason.wanandroid.ui.main.viewholder.SearchHistoryViewHolder;
import jan.jason.wanandroid.utils.CommonUtils;

/**
 * @Description:
 * @Author: jasonjan
 * @Date: 2018/9/4 22:18
 */
public class HistorySearchAdapter extends BaseQuickAdapter<HistoryData,SearchHistoryViewHolder>{

    /**
     * 构造器，传入布局id和历史数据集合
     * @param layoutResId
     * @param data
     */
    public HistorySearchAdapter(int layoutResId, @Nullable List<HistoryData> data) {
        super(layoutResId, data);
    }

    /**
     * 数据转换为视图
     * @param helper
     * @param historyData
     */
    @Override
    protected void convert(SearchHistoryViewHolder helper, HistoryData historyData) {
        helper.setText(R.id.item_search_history_tv, historyData.getData());
        helper.setTextColor(R.id.item_search_history_tv, CommonUtils.randomColor());

        helper.addOnClickListener(R.id.item_search_history_tv);
    }
}
