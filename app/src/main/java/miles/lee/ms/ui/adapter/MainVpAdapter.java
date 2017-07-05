package miles.lee.ms.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by miles on 2017/5/27 0027.
 */

public class MainVpAdapter extends FragmentPagerAdapter{

    private Fragment[] fragments;
    private Context context;
    private String[] fnames;

    public MainVpAdapter(FragmentManager fm, Context context, String[] fnames){
        super(fm);
        this.context = context;
        this.fnames = fnames;
        if(fnames != null){
            fragments = new Fragment[fnames.length];
        }
    }

    @Override
    public Fragment getItem(int position){
        if(fragments[position] ==null){
            fragments[position] = Fragment.instantiate(context,fnames[position]);
        }
        return fragments[position];
    }

    @Override
    public int getCount(){
        return fnames == null ? 0 : fnames.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
//        super.destroyItem(container, position, object);
    }
}
