package jan.jason.wanandroid.ui.project.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import jan.jason.wanandroid.R;

/**
 * @Description: 项目列表 视图持有者
 * @Author: jasonjan
 * @Date: 2018/9/4 17:16
 */
public class ProjectListViewHolder extends BaseViewHolder{

    @BindView(R.id.item_project_list_iv)
    ImageView mProjectIv;
    @BindView(R.id.item_project_list_title_tv)
    TextView mTitleTv;
    @BindView(R.id.item_project_list_content_tv)
    TextView mContentTv;
    @BindView(R.id.item_project_list_time_tv)
    TextView mTimeTv;
    @BindView(R.id.item_project_list_author_tv)
    TextView mAuthorTv;
    @BindView(R.id.item_project_list_install_tv)
    TextView mInstallTv;

    public ProjectListViewHolder(View view){
        super(view);
        ButterKnife.bind(this,view);
    }
}
