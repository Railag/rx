package com.firrael.rx;

import android.graphics.Bitmap;

import com.firrael.rx.model.CreateAccountResult;
import com.firrael.rx.model.CreateGroupResult;
import com.firrael.rx.model.Group;
import com.firrael.rx.model.ImageResult;
import com.firrael.rx.model.LoginResult;
import com.firrael.rx.model.Post;

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
    @GET("/posts")
    Observable<List<Post>> getPosts();

    @FormUrlEncoded
    @POST("/login")
    Observable<LoginResult> login(@Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("/createAccount")
    Observable<CreateAccountResult> createAccount(@Field("login") String login, @Field("email") String email, @Field("password") String password);

    @POST("user_load_user_photo")
    Observable<ImageResult> loadImage();

    @FormUrlEncoded
    @POST("/user_save_user_photo")
    Observable<ImageResult> saveImage(@Field("image") Bitmap imageBitmap);

    @FormUrlEncoded
    @POST("/group_create")
    Observable<CreateGroupResult> createGroup(@Field("groupName") String groupName);

    @POST
    Observable<List<Group>> getGroups();
}