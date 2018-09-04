package jan.jason.wanandroid.ui.hierarchy.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import jan.jason.wanandroid.R;
import jan.jason.wanandroid.core.bean.hierarchy.KnowledgeHierarchyData;
import jan.jason.wanandroid.ui.hierarchy.viewholder.KnowledgeHierarchyViewHolder;
import jan.jason.wanandroid.utils.CommonUtils;

/**
 * @Description: 知识体系需要的适配器
 * @Author: jasonjan
 * @Date: 2018/9/4 14:22
 */
public class KnowledgeHierarchyAdapter extends BaseQuickAdapter<KnowledgeHierarchyData,KnowledgeHierarchyViewHolder>{

    /**
     * 在适配器的构造函数中，将数据和item的布局资源id作为参数传入
     * @param layoutResId
     * @param data
     */
    public KnowledgeHierarchyAdapter(int layoutResId, @Nullable List<KnowledgeHierarchyData> data) {
        super(layoutResId, data);
    }

    /**
     * 将数据转为为视图上的显示
     * @param helper
     * @param item
     */
    @Override
    protected void convert(KnowledgeHierarchyViewHolder helper,KnowledgeHierarchyData item){
        if(item.getName() == null) {
            return;
        }
        helper.setText(R.id.item_knowledge_hierarchy_title, item.getName());
        helper.setTextColor(R.id.item_knowledge_hierarchy_title, CommonUtils.randomColor());
        if (item.getChildren() == null) {
            return;
        }
        StringBuilder content = new StringBuilder();
        for (KnowledgeHierarchyData data: item.getChildren()) {
            content.append(data.getName()).append("   ");
        }
        helper.setText(R.id.item_knowledge_hierarchy_content, content.toString());
    }
}
