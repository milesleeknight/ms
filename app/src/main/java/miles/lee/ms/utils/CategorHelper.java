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

    public static int getCategorPageSize(int type,boolean hasFresh){

        int pageSize;
        if(hasFresh){
            pageSize = 13;
        }else if(type == Config.RecommendedType.POS_LOOP_IMG){
            pageSize = 5;
        }else if(type == Config.RecommendedType.POS_FILM_OR_TV){
            pageSize = 7;
        }else{
            pageSize = 5;
        }
        return pageSize;
    }
}
