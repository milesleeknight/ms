package miles.lee.ms.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import miles.lee.ms.R;
import miles.lee.ms.component.ImageLoader;
import miles.lee.ms.ui.BaseActivity;
import miles.lee.ms.utils.PreferenceUtils;

/**
 * Created by miles on 2017/5/17 0017.
 */

public class SplashActivity extends BaseActivity{
    @BindView(R.id.view_stub)
    ViewStub viewStub;
    private boolean isFirst = false;

//    public static void launch(Context context, boolean fromAbout){
//        Intent intent = new Intent(context,SplashActivity.class);
//        intent.putExtra("from_about",fromAbout);
//    }
    @Override
    protected int getContentView(){
        return R.layout.activity_splash;
    }

    @Override
    protected void init(){
        boolean fromAbout = getIntent().getBooleanExtra("from_about",false);
        if(fromAbout){
            initBanner(fromAbout);
        }else{
            boolean isFirst = PreferenceUtils.getBoolean("is_first",true);
            if(isFirst){
                initBanner(fromAbout);
            }else{
                initAnimate();
            }
        }
    }

    private void initAnimate() {
        viewStub.setLayoutResource(R.layout.layout_welcom_view);
        viewStub.inflate();
        ImageView welcomView = (ImageView) findViewById(R.id.splash_iv);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(welcomView, "scaleX", 1f, 1.13f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(welcomView, "scaleY", 1f, 1.13f);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(2000).play(animatorX).with(animatorY);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
    }

    private void initBanner(final boolean fromAbout){
        viewStub.setLayoutResource(R.layout.layout_splash_banner);
        viewStub.inflate();
        BGABanner banner = (BGABanner) findViewById(R.id.banner_guide_content);
        // 初始化方式2：通过直接传入视图集合的方式初始化
        final List<Integer> images = Arrays.asList(R.mipmap.ic_welcome1, R.mipmap.ic_welcome2, R.mipmap.ic_welcome3, R.mipmap.ic_welcome4);
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                if(position == (images.size() - 1)){
                    PreferenceUtils.putBoolean("is_first",false);
                    if(fromAbout){
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
        banner.setOverScrollMode(View.OVER_SCROLL_NEVER);
        banner.setData(images, null);
        banner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
                ImageView iv = (ImageView) itemView;
                ImageLoader.load(SplashActivity.this,(int)model,iv);
            }
        });
    }
}
