package jan.jason.wanandroid.ui.main.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jan.jason.wanandroid.R;

/**
 * @Description: 常用网站碎片
 * @Author: jasonjan
 * @Date: 2018/9/3 20:47
 */
public class UsageDialogFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        return view;
    }
}
