package miles.lee.ms.ui.adapter.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import miles.lee.ms.R;
import miles.lee.ms.model.ContentItem;

/**
 * Created by miles on 2017/6/28 0028.
 */

public class HomeLoopImgSection extends StatelessSection{

    public HomeLoopImgSection(List<ContentItem> list){
        super(R.layout.section_loop_img,R.layout.section_empty);
    }

    @Override
    public int getContentItemsTotal(){
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view){
        return null;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position){
    }
}
