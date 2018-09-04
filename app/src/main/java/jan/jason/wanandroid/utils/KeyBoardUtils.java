package jan.jason.wanandroid.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @Description: 控制软键盘显示或隐藏
 * @Author: jasonjan
 * @Date: 2018/9/4 22:06
 */
public class KeyBoardUtils {

    /**
     * 打开软键盘
     * @param context
     * @param editText
     */
    public static void openKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     * @param context
     * @param editText
     */
    public static void closeKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
