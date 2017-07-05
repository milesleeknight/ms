package miles.lee.ms.http.response;

/**
 * Created by miles on 2017/5/18 0018.
 */

public class AppUpdateResponse{
    //svn版本号
    private String svnVersion;
    //文件大小
    private String size;
    //更新日志
    private String des;
    //更新时间
    private String updateTime;
    //新apk名
    private String apkName;

    public String getSvnVersion(){
        return svnVersion;
    }

    public void setSvnVersion(String svnVersion){
        this.svnVersion = svnVersion;
    }

    public String getSize(){
        return size;
    }

    public void setSize(String size){
        this.size = size;
    }

    public String getDes(){
        return des;
    }

    public void setDes(String des){
        this.des = des;
    }

    public String getUpdateTime(){
        return updateTime;
    }

    public void setUpdateTime(String updateTime){
        this.updateTime = updateTime;
    }

    public String getApkName(){
        return apkName;
    }

    public void setApkName(String apkName){
        this.apkName = apkName;
    }
}
