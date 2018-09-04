package jan.jason.wanandroid.ui.main.viewholder;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import jan.jason.wanandroid.R;

/**
 * @Description: 搜索历史记录视图持有者
 * @Author: jasonjan
 * @Date: 2018/9/4 22:18
 */
public class SearchHistoryViewHolder extends BaseViewHolder{

    @BindView(R.id.item_search_history_tv)
    TextView mSearchHistoryTv;

    public SearchHistoryViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
