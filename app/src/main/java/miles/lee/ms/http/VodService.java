package miles.lee.ms.http;

import java.util.List;

import io.reactivex.Flowable;
import miles.lee.ms.http.response.BaseCPResponse;
import miles.lee.ms.model.BannerItem;
import miles.lee.ms.model.ChannelItem;
import miles.lee.ms.model.ContentItem;
import miles.lee.ms.model.PageBean;
import miles.lee.ms.model.SubTypeBean;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by miles on 2017/6/5 0005.
 */

public interface VodService{
    /**
     * retrofit注解：
     * 方法注解，包含@GET、@POST、@PUT、@DELETE、@PATH、@HEAD、@OPTIONS、@HTTP
     * 标记注解，包含@FormUrlEncoded、@Multipart、@Streaming
     * 参数注解，包含@Query,@QueryMap、@Body、@Field，@FieldMap、@Part，@PartMap
     * 其他注解，@Path、@Header,@Headers、@Url
     */

    @GET("video-operation/api/r/channel/{col_contentId}-{platform}.json")
    Flowable<BaseCPResponse<List<ChannelItem>>> getChannelList(
            @Path("col_contentId") int cpID,
            @Path("platform") int platform
    );

    @GET("video-operation/api/r/focus/{col_contentId}-{platform}.json")
    Flowable<BaseCPResponse<List<BannerItem>>> getBanner(
            @Path("col_contentId") int cpID,
            @Path("platform") int platform
    );

    /**
     * 获取频道下的所有子类型
     *
     * @param channelId
     * @param platform
     * @return
     */
    @GET("video-operation/api/r/classify/{channelId}-{platform}.json")
    Flowable<BaseCPResponse<List<SubTypeBean>>> getChannelSubType(
            @Path("channelId") int channelId,
            @Path("platform") int platform
    );

    /**
     * 获取子类型下的数据
     *
     * @param pageNo
     * @param pageSize
     * @param classifyTags
     * @param platform
     * @return
     */
    @GET("video-operation/api/r/classify_content/{pageNo}-{pageSize}-{classifyTags}-{platform}.json")
    Flowable<BaseCPResponse<PageBean<ContentItem>>> getChannelSubContent(
            @Path("pageNo") int pageNo,
            @Path("pageSize") int pageSize,
            @Path("classifyTags") String classifyTags,
            @Path("platform") int platform
    );
}
