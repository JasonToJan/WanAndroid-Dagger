package jan.jason.wanandroid.utils.logger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.LogStrategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Description: 磁盘日志策略
 * @Author: jasonjan
 * @Date: 2018/8/26 17:28
 */
public class DiskLogStrategy implements LogStrategy{

    /**
     * 线程切换器
     */
    private final Handler handler;

    DiskLogStrategy(Handler handler){
        this.handler=handler;
    }

    @Override
    public void log(int priority, @Nullable String tag, @NonNull String message) {
        handler.sendMessage(handler.obtainMessage(priority,message));
    }

    /**
     * 自定义静态线程切换器，用于将日志写入本地
     */
    static class WriteHandler extends Handler{

        /**
         * 保留时间 7天的毫秒数
         */
        private static final long SAVE_DAYS=1000*60*60*24*7;

        /**
         * 日志文件名格式
         */
        private SimpleDateFormat fileFormat=new SimpleDateFormat("yyyy-MM-dd_HH", Locale.ENGLISH);

        /**
         * 文件夹
         */
        private final String folder;

        /**
         * app日志字符串
         */
        private String appName="Logger";

        WriteHandler(Looper looper,String folder,String appName){
            super(looper);
            this.folder=folder+new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(new Date());
            this.appName=appName;
            deleteLoggerFile(folder);
        }

        /**
         * 线程接收消息定义
         * @param msg
         */
        @Override
        public void handleMessage(Message msg){
            String content=(String)msg.obj;

            FileWriter fileWriter=null;
            File logFile=getLogFile(folder,appName);

            try{
                fileWriter=new FileWriter(logFile,true);
                writeLog(fileWriter,content);
                fileWriter.flush();
                fileWriter.close();
            }catch (IOException e){
                if(fileWriter!=null){
                    try{
                        fileWriter.flush();
                        fileWriter.close();
                    }catch (IOException e1){

                    }
                }
            }
        }

        /**
         * 写日志到文件
         * @param fileWriter
         * @param content
         * @throws IOException
         */
        private void writeLog(FileWriter fileWriter,String content) throws IOException{
            fileWriter.append(content);
        }

        /**
         * 获取日志文件
         * @param folderName
         * @param fileName
         * @return
         */
        private File getLogFile(String folderName,String fileName){
            File folder=new File(folderName);
            if(!folder.exists()){
                folder.mkdirs();
            }
            return new File(folder,String.format("%s_%s.log",fileName,fileFormat.format(new Date())));
        }

        /**
         * 根据路径删除日志文件
         */
        private synchronized void deleteLoggerFile(String path){
            File file=new File(path);
            if(!file.exists()){
                return;
            }
            File[] files=file.listFiles();
            for(File fil:files){
                if(System.currentTimeMillis()-fil.lastModified()>SAVE_DAYS){
                    deleteDirWhiteFile(fil);
                }
            }
        }

        /**
         * 删除指定文件目录下的所有文件
         * @param dir
         */
        private synchronized void deleteDirWhiteFile(File dir){
            if(dir==null||!dir.exists()||!dir.isDirectory()){
                return;
            }
            for(File file:dir.listFiles()){
                if(file.isFile()){
                    file.delete();
                }else if(file.isDirectory()){
                    deleteDirWhiteFile(file);
                }
            }
            dir.delete();
        }
    }
}
