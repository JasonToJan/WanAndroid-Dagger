package jan.jason.wanandroid.ui.main.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jan.jason.wanandroid.R;

/**
 * @Description: 搜索片段
 * @Author: jasonjan
 * @Date: 2018/9/3 20:50
 */
public class SearchDialogFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }
}
