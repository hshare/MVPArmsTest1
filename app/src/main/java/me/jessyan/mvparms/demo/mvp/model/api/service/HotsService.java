package me.jessyan.mvparms.demo.mvp.model.api.service;

import java.util.List;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.HotBean;
import me.jessyan.mvparms.demo.mvp.model.entity.TabsBean;
import me.jessyan.mvparms.demo.mvp.model.entity.User;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * 存放关于用户的一些api
 * Created by jess on 8/5/16 12:05
 * contact with jess.yan.effort@gmail.com
 */
public interface HotsService {

//    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    //    @Headers({HEADER_API_VERSION})
    @GET("/?m=api&c=info&a=content_list&num=20")
    Observable<HotBean> getHots(@Query("p") int pageNo,@Query("category_id") String category_id);

    @GET("/?m=api&c=info&a=category_list")
    Observable<List<TabsBean>> getTabs();
}
