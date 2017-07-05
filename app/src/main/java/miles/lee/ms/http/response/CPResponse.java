package miles.lee.ms.http.response;

import java.io.Serializable;

/**
 * Created by miles on 2017/6/5 0005.
 */

public class CPResponse <T> implements Serializable{
    private String code;
    private String codeV;
    private String count;
    private long longTime;
    private String message;
    private String msg;
    private T result;
    private String resultex;
    private String stringTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeV() {
        return codeV;
    }

    public void setCodeV(String codeV) {
        this.codeV = codeV;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public long getLongTime() {
        return longTime;
    }

    public void setLongTime(long longTime) {
        this.longTime = longTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getResultex() {
        return resultex;
    }

    public void setResultex(String resultex) {
        this.resultex = resultex;
    }

    public String getStringTime() {
        return stringTime;
    }

    public void setStringTime(String stringTime) {
        this.stringTime = stringTime;
    }
}
