package miles.lee.ms.model;

import java.io.Serializable;

/**
 * Created by miles on 2017/5/31 0031.
 */

public class ChannelItem implements Serializable{
    private Object child;
    private int cpId;
    private int operationTagId;
    private String operationTagName;
    private String picUrl;
    private int seq;

    public Object getChild(){
        return child;
    }

    public void setChild(Object child){
        this.child = child;
    }

    public int getCpId(){
        return cpId;
    }

    public void setCpId(int cpId){
        this.cpId = cpId;
    }

    public int getOperationTagId(){
        return operationTagId;
    }

    public void setOperationTagId(int operationTagId){
        this.operationTagId = operationTagId;
    }

    public String getOperationTagName(){
        return operationTagName;
    }

    public void setOperationTagName(String operationTagName){
        this.operationTagName = operationTagName;
    }

    public String getPicUrl(){
        return picUrl;
    }

    public void setPicUrl(String picUrl){
        this.picUrl = picUrl;
    }

    public int getSeq(){
        return seq;
    }

    public void setSeq(int seq){
        this.seq = seq;
    }

    public int compareTo(ChannelItem item){
        if(seq>item.getSeq()){
            return 1;
        }else if(seq == item.getSeq()){
            return 0;
        }else{
            return -1;
        }
    }
}
