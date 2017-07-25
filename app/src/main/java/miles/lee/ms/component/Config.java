package miles.lee.ms.component;

import java.io.File;

import miles.lee.ms.App;

/**
 * Created by miles on 2017/5/17 0017.
 */

public class Config{
    //Http请求后返回的code
    public static class HttpType {
        public static final String HTTP_SUCCESS = "N000000";      //成功返回数据
        public static final String HTTP_ERRO = "E000000";         //返回数据失败
        public static final String HTTP_ERRO200 = "E000200";      //返回数据失败
    }

    public static class Path {
        public static final String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "ms_data";
        public static final String PATH_CACHE = PATH_DATA + "/NetCache";
    }
    //推荐类型
    public static class RecommendedType {
        // adapter布局，各个模块需要的标识
        public static final int CATEGORY_NORMAL = 0;               // 一般模块
        public static final int CATEGORY_NEWS = 1;                 // 资讯

        // adapter布局，各个模块内的gridview的item布局标识
        public static final int CHILD_TYPE_NORMAL = 0;               // 一般子布局(两行两列,有summary)
        public static final int CHILD_TYPE_LIVE = 1;                 // 直播
        public static final int CHILD_TYPE_MEDIA = 2;                // 电影、电视剧这类的子布局(两行三列,无summary)

        // 视频类型
        public static final int TYPE_FILM = 1;// 电影
        public static final int TYPE_TV_PLAY = 2;// 电视剧
        public static final int TYPE_MV = 3;// MV
        public static final int TYPE_VR = 4;// VR

        /**
         * 后台返回推荐页数据类型
         * 10：资讯横版两行两列布局
         * 11：电影竖版两行三列布局
         * 12：电视剧竖版两行三列布局
         * 13：直播横版两行两列布局
         * 14：综艺横版两行两列布局
         * 15：体育横版两行两列布局
         * -----------------------------------------------------------------------------
         * 101：横版轮播图
         * 102：五佳片banner
         */
        public static final int POS_NEWS = 10;
        public static final int POS_FILM_OR_TV = 11;
        public static final int POS_LIVE = 12;
        public static final int POS_NORMAL_TWO = 13;

        public static final int POS_BANNER = 101;
        public static final int POS_LOOP_IMG = 102;
    }
    public static class LoginType{
        public static final String ACCOUNT = "0";
        public static final String AUTH = "1";
    }
}
