package jan.jason.wanandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @Description: 分享工具
 * @Author: jasonjan
 * @Date: 2018/9/5 14:34
 */
public class ShareUtil {

    private static final String EMAIL_ADDRESS = "1211241203@qq.com";

    /**
     * 发送文本
     * @param context
     * @param text
     * @param title
     */
    public static void shareText(Context context, String text, String title){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        context.startActivity(Intent.createChooser(intent,title));
    }

    /**
     * 发送邮件
     * @param context
     * @param title
     */
    public static void sendEmail(Context context, String title) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(
                "mailto:" + EMAIL_ADDRESS));
        context.startActivity(Intent.createChooser(intent, title));
    }
}
