package miles.lee.ms.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by miles on 2017/6/23 0023.
 */

public class BannerAdapter extends PagerAdapter{
    private ViewPagerOnItemClickListener listener;

    private List<ImageView> mList;

    public BannerAdapter(List<ImageView> list){
        this.mList = list;
    }
    @Override
    public int getCount(){
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return false;
    }

    public void setmViewPagerOnItemClickListener(ViewPagerOnItemClickListener listener){
        this.listener = listener;
    }

    public interface ViewPagerOnItemClickListener{
        void onItemClick();
    }
}
