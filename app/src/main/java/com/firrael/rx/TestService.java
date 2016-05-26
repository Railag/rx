package com.firrael.rx;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by firrael on 25.05.2016.
 */
public interface TestService {
    @POST("/post/<post>")
    Observable<JsonObject> putPost(@Query("post") int post);

    @GET("/posts")
    Observable<List<Post>> getPosts();
}