package miles.lee.ms.ui.adapter.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Section基类
 */
public abstract class Section{

    public enum State {
        LOADING, LOADED, FAILED
    }

    private State state = State.LOADED;

    boolean visible = true;

    boolean hasHeader = false;

    boolean hasFooter = false;

    Integer headerResourceId;

    Integer footerResourceId;

    int itemResourceId;

    private Integer loadingResourceId;

    private Integer failedResourceId;

    protected RecyclerView.Adapter adapter;

    /**
     * Package-level constructor
     */
    Section() {

    }

    /**
     * Create a TimeSection object with loading/failed states but no header and footer
     *
     * @param itemResourceId    layout resource for its items
     * @param loadingResourceId layout resource for its loading state
     * @param failedResourceId  layout resource for its failed state
     */
    public Section(int itemResourceId, int loadingResourceId, int failedResourceId) {

        this.itemResourceId = itemResourceId;
        this.loadingResourceId = loadingResourceId;
        this.failedResourceId = failedResourceId;
    }

    /**
     * Create a TimeSection object with loading/failed states, a custom header but no footer
     *
     * @param headerResourceId  layout resource for its header
     * @param itemResourceId    layout resource for its items
     * @param loadingResourceId layout resource for its loading state
     * @param failedResourceId  layout resource for its failed state
     */
    public Section(int headerResourceId, int itemResourceId, int loadingResourceId, int failedResourceId) {

        this(itemResourceId, loadingResourceId, failedResourceId);
        this.headerResourceId = headerResourceId;
        hasHeader = true;
    }

    /**
     * Create a TimeSection object with loading/failed states, a custom header and footer
     *
     * @param headerResourceId  layout resource for its header
     * @param footerResourceId  layout resource for its footer
     * @param itemResourceId    layout resource for its items
     * @param loadingResourceId layout resource for its loading state
     * @param failedResourceId  layout resource for its failed state
     */
    public Section(int headerResourceId, int footerResourceId, int itemResourceId, int loadingResourceId, int failedResourceId) {

        this(headerResourceId, itemResourceId, loadingResourceId, failedResourceId);
        this.footerResourceId = footerResourceId;
        hasFooter = true;
    }

    /**
     * 绑定adapter（必要是做刷新）
     * @param adapter
     */
    public void bindAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public int getSectionType(){
        return SectionedRecyclerViewAdapter.VIEW_TYPE_NOR;
    }

    /**
     * Set the State of this TimeSection
     *
     * @param state state of this section
     */
    public final void setState(State state) {

        this.state = state;
    }

    /**
     * Return the current State of this TimeSection
     *
     * @return current state of this section
     */
    public final State getState() {

        return state;
    }

    /**
     * Check if this TimeSection is visible
     *
     * @return true if this TimeSection is vibisle
     */
    public final boolean isVisible() {

        return visible;
    }

    /**
     * Set if this TimeSection is visible
     *
     * @param visible true if this TimeSection is visible
     */
    public final void setVisible(boolean visible) {

        this.visible = visible;
    }

    /**
     * Check if this TimeSection has a header
     *
     * @return true if this TimeSection has a header
     */
    public final boolean hasHeader() {

        return hasHeader;
    }

    /**
     * Set if this TimeSection has header
     *
     * @param hasHeader true if this TimeSection has a header
     */
    public final void setHasHeader(boolean hasHeader) {

        this.hasHeader = hasHeader;
    }

    /**
     * Check if this TimeSection has a footer
     *
     * @return true if this TimeSection has a footer
     */
    public final boolean hasFooter() {

        return hasFooter;
    }

    /**
     * Set if this TimeSection has footer
     *
     * @param hasFooter true if this TimeSection has a footer
     */
    public final void setHasFooter(boolean hasFooter) {

        this.hasFooter = hasFooter;
    }

    /**
     * Return the layout resource id of the header
     *
     * @return layout resource id of the header
     */
    public final Integer getHeaderResourceId() {

        return headerResourceId;
    }

    /**
     * Return the layout resource id of the footer
     *
     * @return layout resource id of the footer
     */
    public final Integer getFooterResourceId() {

        return footerResourceId;
    }

    /**
     * Return the layout resource id of the item
     *
     * @return layout resource id of the item
     */
    public final int getItemResourceId() {

        return itemResourceId;
    }

    /**
     * Return the layout resource id of the loading view
     *
     * @return layout resource id of the loading view
     */
    public final Integer getLoadingResourceId() {

        return loadingResourceId;
    }

    /**
     * Return the layout resource id of the failed view
     *
     * @return layout resource id of the failed view
     */
    public final Integer getFailedResourceId() {

        return failedResourceId;
    }

    /**
     * Bind the data to the ViewHolder for the Content of this TimeSection, that can be the Items,
     * Loading view or Failed view, depending on the current state of the section
     *
     * @param holder   ViewHolder for the Content of this TimeSection
     * @param position position of the item in the TimeSection, not in the RecyclerView
     */
    public final void onBindContentViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (state) {
            case LOADING:
                onBindLoadingViewHolder(holder);
                break;
            case LOADED:
                onBindItemViewHolder(holder, position);
                break;
            case FAILED:
                onBindFailedViewHolder(holder);
                break;
            default:
                throw new IllegalStateException("Invalid state");
        }
    }

    /**
     * Return the total of items of this TimeSection, including content items (according to the section
     * state) plus header and footer
     *
     * @return total of items of this section
     */
    public final int getSectionItemsTotal() {

        int contentItemsTotal;

        switch (state) {
            case LOADING:
                contentItemsTotal = 1;
                break;
            case LOADED:
                contentItemsTotal = getContentItemsTotal();
                break;
            case FAILED:
                contentItemsTotal = 1;
                break;
            default:
                throw new IllegalStateException("Invalid state");
        }

        return contentItemsTotal + (hasHeader ? 1 : 0) + (hasFooter ? 1 : 0);
    }

    /**
     * Return the total of items of this TimeSection
     *
     * @return total of items of this TimeSection
     */
    public abstract int getContentItemsTotal();

    /**
     * Return the ViewHolder for the Header of this TimeSection
     *
     * @param view View inflated by resource returned by getHeaderResourceId
     * @return ViewHolder for the Header of this TimeSection
     */
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {

        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    /**
     * Bind the data to the ViewHolder for the Header of this TimeSection
     *
     * @param holder ViewHolder for the Header of this TimeSection
     */
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        // Nothing to bind here.
    }

    /**
     * Return the ViewHolder for the Footer of this TimeSection
     *
     * @param view View inflated by resource returned by getFooterResourceId
     * @return ViewHolder for the Footer of this TimeSection
     */
    public RecyclerView.ViewHolder getFooterViewHolder(View view) {

        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    /**
     * Bind the data to the ViewHolder for the Footer of this TimeSection
     *
     * @param holder ViewHolder for the Footer of this TimeSection
     */
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder) {
        // Nothing to bind here.
    }

    /**
     * Return the ViewHolder for a single Item of this TimeSection
     *
     * @param view View inflated by resource returned by getItemResourceId
     * @return ViewHolder for the Item of this TimeSection
     */
    public abstract RecyclerView.ViewHolder getItemViewHolder(View view);

    /**
     * Bind the data to the ViewHolder for an Item of this TimeSection
     *
     * @param holder   ViewHolder for the Item of this TimeSection
     * @param position position of the item in the TimeSection, not in the RecyclerView
     */
    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * Return the ViewHolder for the Loading state of this TimeSection
     *
     * @param view View inflated by resource returned by getItemResourceId
     * @return ViewHolder for the Loading state of this TimeSection
     */
    public RecyclerView.ViewHolder getLoadingViewHolder(View view) {

        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    /**
     * Bind the data to the ViewHolder for Loading state of this TimeSection
     *
     * @param holder ViewHolder for the Loading state of this TimeSection
     */
    public void onBindLoadingViewHolder(RecyclerView.ViewHolder holder) {
        // Nothing to bind here.
    }

    /**
     * Return the ViewHolder for the Failed state of this TimeSection
     *
     * @param view View inflated by resource returned by getItemResourceId
     * @return ViewHolder for the Failed of this TimeSection
     */
    public RecyclerView.ViewHolder getFailedViewHolder(View view) {

        return new SectionedRecyclerViewAdapter.EmptyViewHolder(view);
    }

    /**
     * Bind the data to the ViewHolder for the Failed state of this TimeSection
     *
     * @param holder ViewHolder for the Failed state of this TimeSection
     */
    public void onBindFailedViewHolder(RecyclerView.ViewHolder holder) {
        // Nothing to bind here.
    }
}
