package miles.lee.ms.model;

import java.util.List;

/**
 * @classname: CategoryItem
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2016/8/4 17:35
 */
public class CategoryItem{
    private String categoryImg;
    private String categoryName;
    private int categoryType;
    private int categorySubType;
    private int parentViewType;
    private int childViewType;
    private ContentItem keynote;
    private List<ContentItem> page;

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public void setCategorySubType(int categorySubType) {
        this.categorySubType = categorySubType;
    }

    public void setParentViewType(int parentViewType) {
        this.parentViewType = parentViewType;
    }

    public void setChildViewType(int childViewType) {
        this.childViewType = childViewType;
    }

    public void setKeynote(ContentItem keynote) {
        this.keynote = keynote;
    }

    public void setPage(List<ContentItem> page) {
        this.page = page;
    }

    public String getCategoryImg() {
        return categoryImg;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public int getCategorySubType() {
        return categorySubType;
    }

    public int getParentViewType() {
        return parentViewType;
    }

    public int getChildViewType() {
        return childViewType;
    }

    public ContentItem getKeynote() {
        return keynote;
    }

    public List<ContentItem> getPage() {
        return page;
    }
}
