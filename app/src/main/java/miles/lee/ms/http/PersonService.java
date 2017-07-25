package miles.lee.ms.http;

import java.util.List;

import io.reactivex.Flowable;
import miles.lee.ms.http.response.BaseResponse;
import miles.lee.ms.model.StbDevice;
import miles.lee.ms.model.UserInfo;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by miles on 2017/7/24 0024.
 */

public interface PersonService{
    /**
     * 登录请求(密码登录)
     *
     * @param userPhone 手机账号
     * @param userPwd   密码
     * @param loginType 登录类型。0，密码登录；1，验证码登录
     * @return
     */
    @Headers("Cache-Control: public, max-age=0")
    @FormUrlEncoded
    @POST("api/phone/login.json")
    Flowable<BaseResponse<UserInfo>> getUserInfoByLogin(
            @Field("userPhone") String userPhone,
            @Field("userPwd") String userPwd,
            @Field("loginType") String loginType
    );
    /**
     * 获取设备列表
     *
     * @param phUserId
     * @return
     */
    @Headers("Cache-Control: public, max-age=0")
    @GET("api/phone/getDeviceList.json")
    Flowable<BaseResponse<List<StbDevice>>> getDeviceList(
            @Query("phUserId") String phUserId
    );
}
