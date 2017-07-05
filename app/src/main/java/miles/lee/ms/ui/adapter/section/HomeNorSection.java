package miles.lee.ms.ui.adapter.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by miles on 2017/6/28 0028.
 */

public class HomeNorSection extends StatelessSection{
    public HomeNorSection(int itemResourceId){
        super(itemResourceId);
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
