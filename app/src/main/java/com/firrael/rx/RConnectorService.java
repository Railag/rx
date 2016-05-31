package com.firrael.rx;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by firrael on 25.05.2016.
 */
public interface RConnectorService {
    // @POST("/post/<post>")
    //  Observable<JsonObject> putPost(@Query("post") int post);

    @GET("/posts")
    Observable<List<Post>> getPosts();

    @FormUrlEncoded
    @POST("/login")
    Observable<LoginResult> login(@Field("login") String login, @Field("password") String password);
}