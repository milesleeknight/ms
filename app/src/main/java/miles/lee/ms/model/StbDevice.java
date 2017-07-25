package miles.lee.ms.model;

import java.io.Serializable;

/**
 * Created by miles on 2017/7/25 0025.
 */

public class StbDevice implements Serializable{
    private long id;
    private long phUserId;
    private long tbUserId;
    private String tbUserName;
    private int bindingStatus;
    private int loginStatus;
    private String createTime;
    private String updateTime;
    private String mac;
    private String sn;
    private String ca;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public long getPhUserId(){
        return phUserId;
    }

    public void setPhUserId(long phUserId){
        this.phUserId = phUserId;
    }

    public long getTbUserId(){
        return tbUserId;
    }

    public void setTbUserId(long tbUserId){
        this.tbUserId = tbUserId;
    }

    public String getTbUserName(){
        return tbUserName;
    }

    public void setTbUserName(String tbUserName){
        this.tbUserName = tbUserName;
    }

    public int getBindingStatus(){
        return bindingStatus;
    }

    public void setBindingStatus(int bindingStatus){
        this.bindingStatus = bindingStatus;
    }

    public int getLoginStatus(){
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus){
        this.loginStatus = loginStatus;
    }

    public String getCreateTime(){
        return createTime;
    }

    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }

    public String getUpdateTime(){
        return updateTime;
    }

    public void setUpdateTime(String updateTime){
        this.updateTime = updateTime;
    }

    public String getMac(){
        return mac;
    }

    public void setMac(String mac){
        this.mac = mac;
    }

    public String getSn(){
        return sn;
    }

    public void setSn(String sn){
        this.sn = sn;
    }

    public String getCa(){
        return ca;
    }

    public void setCa(String ca){
        this.ca = ca;
    }
}
