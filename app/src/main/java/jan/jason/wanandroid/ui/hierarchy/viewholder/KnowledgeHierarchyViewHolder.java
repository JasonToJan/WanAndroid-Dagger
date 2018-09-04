package jan.jason.wanandroid.ui.hierarchy.viewholder;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import jan.jason.wanandroid.R;

/**
 * @Description: 知识体系页面的适配器需要用的ViewHolder
 * @Author: jasonjan
 * @Date: 2018/9/4 14:24
 */
public class KnowledgeHierarchyViewHolder extends BaseViewHolder{

    @BindView(R.id.item_knowledge_hierarchy_title)
    TextView mTitle;
    @BindView(R.id.item_knowledge_hierarchy_content)
    TextView mContent;

    public KnowledgeHierarchyViewHolder(View view){
        super(view);
        ButterKnife.bind(this,view);
    }
}
