package jan.jason.wanandroid.core.http.exception;

/**
 * @Description: 服务异常类
 * @Author: jasonjan
 * @Date: 2018/9/3 17:51
 */
public class ServerException extends Exception{
    private int code;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
