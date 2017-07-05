package miles.lee.ms.model;

import java.util.List;

/**
 * Created by miles on 2017/6/26 0026.
 */

public class PageBean<T>{
    private List<T> pageContent;

    private int pageCount;

    private int pageNo;

    private int pageSize;

    private int recCount;

    public List<T> getPageContent(){
        return pageContent;
    }

    public void setPageContent(List<T> pageContent){
        this.pageContent = pageContent;
    }

    public int getPageCount(){
        return pageCount;
    }

    public void setPageCount(int pageCount){
        this.pageCount = pageCount;
    }

    public int getPageNo(){
        return pageNo;
    }

    public void setPageNo(int pageNo){
        this.pageNo = pageNo;
    }

    public int getPageSize(){
        return pageSize;
    }

    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }

    public int getRecCount(){
        return recCount;
    }

    public void setRecCount(int recCount){
        this.recCount = recCount;
    }
}
