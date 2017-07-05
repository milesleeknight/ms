package miles.lee.ms.http.response;


import java.io.Serializable;

/**
 * 基础Response
 * Created by on 2016/7/27.
 */
public class BaseResponse<T> implements Serializable{


    private String code;
    private long longTime;
    private String stringTime;
    private String message;
    private T result;
    private boolean cached;
    private int cachedTime;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getLongTime() {
        return longTime;
    }

    public void setLongTime(long longTime) {
        this.longTime = longTime;
    }

    public String getStringTime() {
        return stringTime;
    }

    public void setStringTime(String stringTime) {
        this.stringTime = stringTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public int getCachedTime() {
        return cachedTime;
    }

    public void setCachedTime(int cachedTime) {
        this.cachedTime = cachedTime;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code='" + code + '\'' +
                ", longTime=" + longTime +
                ", stringTime='" + stringTime + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                ", cached=" + cached +
                ", cachedTime=" + cachedTime +
                '}';
    }
}
