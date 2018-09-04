package jan.jason.wanandroid.ui.navigation.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jan.jason.wanandroid.R;

/**
 * @Description: 导航碎片
 * @Author: jasonjan
 * @Date: 2018/9/3 20:35
 */
public class NavigationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        return view;
    }
}
