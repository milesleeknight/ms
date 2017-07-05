package miles.lee.ms.utils;

import miles.lee.ms.component.Config;

/**
 * Created by miles on 2017/6/27 0027.
 */

public class CategorHelper{
    public static int getCategoryViewType(int pos){
        int type = 0;
        switch(pos){
            case Config.RecommendedType.POS_NEWS:
                type =Config.RecommendedType.CATEGORY_NEWS;
                break;
            case Config.RecommendedType.POS_FILM_OR_TV:
                type = Config.RecommendedType.CATEGORY_NORMAL;
                break;

        }
        return type;
    }

    public static int getCategoryPagesize(int type){
        int count = 0;
        switch(type){
            case Config.RecommendedType.POS_LOOP_IMG:
                count = 5;
                break;
            case Config.RecommendedType.POS_FILM_OR_TV:
                count = 7;
                break;
            default:
                count = 13;
                break;
        }
        return count;
    }
}
