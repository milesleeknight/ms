package miles.lee.ms.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by miles on 2017/6/23 0023.
 */

public class BannerAdapter extends PagerAdapter{
    private ViewPagerOnItemClickListener listener;
    private int pos;
    private List<ImageView> mList;

    public BannerAdapter(List<ImageView> list){
        this.mList = list;
    }
    @Override
    public int getCount(){
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view == object;
    }

    public void setmViewPagerOnItemClickListener(ViewPagerOnItemClickListener listener){
        this.listener = listener;
    }

    public interface ViewPagerOnItemClickListener{
        void onItemClick();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        //对ViewPager页号求模取出View列表中要显示的项
        final int finalPosition = position % mList.size();
        ImageView v = mList.get(finalPosition);
        pos = finalPosition;
        v.setScaleType(ImageView.ScaleType.FIT_XY);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = v.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(v);
        }
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(listener!=null)
                listener.onItemClick();
            }
        });


        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
//        super.destroyItem(container, position, object);
    }


}
