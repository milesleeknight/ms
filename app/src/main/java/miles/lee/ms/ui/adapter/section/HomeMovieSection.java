package miles.lee.ms.ui.adapter.section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import miles.lee.ms.App;
import miles.lee.ms.R;
import miles.lee.ms.component.Config;
import miles.lee.ms.model.CategoryItem;
import miles.lee.ms.model.ContentItem;
import miles.lee.ms.utils.LogUtil;

/**
 * 两行三列布局
 * Created by miles on 2017/6/28 0028.
 */

public class HomeMovieSection extends StatelessSection{
    private Context mContext ;
    private CategoryItem item;
    private  int maxPage;
    private int pageIndex;
    private int pageSize = 6;

    public HomeMovieSection(CategoryItem item, Context context, boolean isFirstChannel){
        super(R.layout.section_movie_header,R.layout.section_movie_footer,R.layout.section_movie_item);
        if(item == null){
            throw new NullPointerException("category is null...");
        }
        this.mContext = context;
        this.item = item;
        maxPage = (int) Math.ceil((double) item.getPage().size() / pageSize);// 算出总共分页，取大的值
    }

    @Override
    public int getSectionType(){
        return SectionedRecyclerViewAdapter.VIEW_TYPE_FILM;
    }

    @Override
    public int getContentItemsTotal(){
        if(item == null){
            LogUtil.d("HomeMovieSection contentItem is null");
            return 0;
        }
        List<ContentItem> page = item.getPage();
        if(page==null|| page.isEmpty() || maxPage == 0){

            return 0;
        }
        if (pageIndex >= maxPage) {
            pageIndex = maxPage - 1;
        }

        int size = page.size();
        int remainder = size - pageSize * pageIndex;
        int count = (remainder > pageSize ? pageSize : remainder);
        return count;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view){
        return new ItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view){
        return new HeaderViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getFooterViewHolder(View view){
        return new FootViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position){
        final int realPosition = position + pageSize * pageIndex;
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        List<ContentItem> list = item.getPage();
        final ContentItem contentBean = list.get(realPosition);

        Glide.with(mContext)
                .load(contentBean.getBlueRayImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontAnimate()
                .into(itemViewHolder.grid_item_img);

        itemViewHolder.grid_item_tv.setText(contentBean.getContentName());
        if(contentBean.getContentClass() == Config.RecommendedType.TYPE_FILM){
            itemViewHolder.grid_item_right_tv.setText(contentBean.getScore());
        }else{
            itemViewHolder.grid_item_right_tv.setText("已更新至" + contentBean.getSubNum() + "集");
        }
//        itemViewHolder.item_container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                VDetailActivity.launch(mContext,contentBean.contentId,0);
//            }
//        });
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder){
        super.onBindFooterViewHolder(holder);
        FootViewHolder footViewHolder = (FootViewHolder) holder;
//        footViewHolder.line.setVisibility(hasRefresh ? View.VISIBLE : View.GONE);
//        footViewHolder.tv_refresh.setVisibility(hasRefresh ? View.VISIBLE : View.GONE);
//        footViewHolder.tv_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                VideoFilterActivity.launch(mContext,category.getCategorySubType(),category.getCategoryType());
//            }
//        });
        footViewHolder.tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maxPage <= 1){
                    return;
                }
                if (pageIndex == maxPage - 1) {
                    pageIndex = 0;
                } else {
                    pageIndex++;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder){
        super.onBindHeaderViewHolder(holder);
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        // 强烈推荐
        final ContentItem keynote = item.getKeynote();

        Glide.with(mContext)
                .load(item.getCategoryImg())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(headerViewHolder.iv_cate);

        headerViewHolder.tv_title.setText(item.getCategoryName());
        if(keynote != null){
            headerViewHolder.tv_link.setText(keynote.getContentName() + ">");
//            headerViewHolder.tv_link.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    VDetailActivity.launch(mContext,keynote.contentId,0);
//                }
//            });
        }
        headerViewHolder.tv_info_1.setVisibility(View.GONE);
        headerViewHolder.tv_info_2.setVisibility(View.GONE);
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_cate)
        ImageView iv_cate;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_link)
        TextView tv_link;
        @BindView(R.id.tv_info_1)
        TextView tv_info_1;
        @BindView(R.id.tv_info_2)
        TextView tv_info_2;


        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_container)
        RelativeLayout item_container;

        @BindView(R.id.grid_item_img)
        ImageView grid_item_img;

        @BindView(R.id.grid_item_right_text)
        TextView grid_item_right_tv;

        @BindView(R.id.grid_item_text)
        TextView grid_item_tv;

        public ItemViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            int padingpx = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.x7);
            int parentPadingpx = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.x9);
            item_container.setPadding(padingpx,padingpx,padingpx,padingpx);
            ViewGroup.LayoutParams layoutParams = grid_item_img.getLayoutParams();
            layoutParams.width = (App.SCREEN_WIDTH - 6*padingpx - 2*parentPadingpx)/3;
            layoutParams.height = layoutParams.width/3*4;
            grid_item_img.setLayoutParams(layoutParams);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_more)
        TextView tv_more;

        @BindView(R.id.line)
        View line;

        @BindView(R.id.tv_other)
        TextView tv_refresh;


        public FootViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
