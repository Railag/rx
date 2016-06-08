package com.firrael.rx;

import android.graphics.Bitmap;

import com.firrael.rx.model.AddUserResult;
import com.firrael.rx.model.CreateGroupResult;
import com.firrael.rx.model.Group;
import com.firrael.rx.model.ImageResult;
import com.firrael.rx.model.Message;
import com.firrael.rx.model.Post;
import com.firrael.rx.model.RemoveUserResult;
import com.firrael.rx.model.SendMessageResult;
import com.firrael.rx.model.UserResult;
import com.firrael.rx.view.ChatUser;

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
    @POST("/user/login")
    Observable<UserResult> login(@Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("/user/startup_login")
    Observable<UserResult> startupLogin(@Field("login") String login, @Field("token") String token);

    @FormUrlEncoded
    @POST("/user")
    Observable<UserResult> createAccount(@Field("login") String login, @Field("email") String email, @Field("password") String password);

    @POST("/user_load_user_photo")
    Observable<ImageResult> loadImage();

    @FormUrlEncoded
    @POST("/user_save_user_photo")
    Observable<ImageResult> saveImage(@Field("image") Bitmap imageBitmap);

    @FormUrlEncoded
    @POST("/group")
    Observable<CreateGroupResult> createGroup(@Field("title") String groupName, @Field("creator") long creatorId);

    @FormUrlEncoded
    @POST("/group/fetch")
    Observable<List<Group>> getGroups(@Field("creator") long creatorId);

    @FormUrlEncoded
    @POST("/group/send_message")
    Observable<SendMessageResult> sendMessage(@Field("group_id") long groupId, @Field("user_id") long userId, @Field("text") String message);

    @FormUrlEncoded
    @POST("/group/fetch_messages")
    Observable<List<Message>> fetchMessages(@Field("group_id") long groupId);

    @FormUrlEncoded
    @POST("/group/add_user")
    Observable<AddUserResult> addUser(@Field("user_login_or_email") String loginOrEmail, @Field("group_id") long groupId);

    @FormUrlEncoded
    @POST("/group_remove_user")
    Observable<RemoveUserResult> removeUser(@Field("login") String removeLogin);

    @FormUrlEncoded
    @POST("/group/fetch_users")
    Observable<List<ChatUser>> fetchUsers(@Field("group_id") long groupId);
}