package miles.lee.ms.http.response;

import java.io.Serializable;

/**
 * Created by miles on 2017/6/5 0005.
 */

public class BaseCPResponse <T> implements Serializable{

    private CPResponse<T> data;

    public CPResponse<T> getData() {
        return data;
    }

    public void setData(CPResponse<T> data) {
        this.data = data;
    }
}
