package miles.lee.ms.ui.adapter.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import miles.lee.ms.R;
import miles.lee.ms.model.BannerItem;
import miles.lee.ms.ui.widget.BannerView;

/**
 * Created by miles on 2017/6/22 0022.
 */

public class HomeBannerSection extends StatelessSection{
    private List<BannerItem> list;

    public HomeBannerSection(List<BannerItem> itemResourceId){
        super(R.layout.section_banner,R.layout.section_empty);
        this.list = itemResourceId;
    }

    @Override
    public int getContentItemsTotal(){
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view){
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder){
        BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
        bannerViewHolder.bannerView.delayTime(5).build(list);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view){
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position){
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        public ItemViewHolder(View itemView){
            super(itemView);
        }
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.banner)
        BannerView bannerView;
        public BannerViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
