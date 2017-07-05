package miles.lee.ms.model;

import java.io.Serializable;

/**
 * Created by miles on 2017/6/22 0022.
 */

public class BannerItem implements Serializable{

    /**
     * activityPic : null
     * bigPic : http://192.168.1.24:8060/media_asset/2016/08/01/15/13/58/1470035638132.png
     * contentId : 8004
     * contentType : 0
     * cpId : 37
     * focusContent : null
     * focusId : null
     * focusName : 热门电视剧
     * isActivity : null
     * isAlbum : 0
     * isEffective : 1
     * seq : 1
     * smallPic : http://192.168.1.24:8060/media_asset/2016/08/01/15/14/09/1470035649125.png
     */

    private Object activityPic;
    private String bigPic;
    private int contentId;
    private String contentType;
    private int cpId;
    private Object focusContent;
    private Object focusId;
    private String focusName;
    private Object isActivity;
    private int isAlbum;
    private int isEffective;
    private int seq;
    private String smallPic;

    public Object getActivityPic(){
        return activityPic;
    }

    public void setActivityPic(Object activityPic){
        this.activityPic = activityPic;
    }

    public String getBigPic(){
        return bigPic;
    }

    public void setBigPic(String bigPic){
        this.bigPic = bigPic;
    }

    public int getContentId(){
        return contentId;
    }

    public void setContentId(int contentId){
        this.contentId = contentId;
    }

    public String getContentType(){
        return contentType;
    }

    public void setContentType(String contentType){
        this.contentType = contentType;
    }

    public int getCpId(){
        return cpId;
    }

    public void setCpId(int cpId){
        this.cpId = cpId;
    }

    public Object getFocusContent(){
        return focusContent;
    }

    public void setFocusContent(Object focusContent){
        this.focusContent = focusContent;
    }

    public Object getFocusId(){
        return focusId;
    }

    public void setFocusId(Object focusId){
        this.focusId = focusId;
    }

    public String getFocusName(){
        return focusName;
    }

    public void setFocusName(String focusName){
        this.focusName = focusName;
    }

    public Object getIsActivity(){
        return isActivity;
    }

    public void setIsActivity(Object isActivity){
        this.isActivity = isActivity;
    }

    public int getIsAlbum(){
        return isAlbum;
    }

    public void setIsAlbum(int isAlbum){
        this.isAlbum = isAlbum;
    }

    public int getIsEffective(){
        return isEffective;
    }

    public void setIsEffective(int isEffective){
        this.isEffective = isEffective;
    }

    public int getSeq(){
        return seq;
    }

    public void setSeq(int seq){
        this.seq = seq;
    }

    public String getSmallPic(){
        return smallPic;
    }

    public void setSmallPic(String smallPic){
        this.smallPic = smallPic;
    }
}
