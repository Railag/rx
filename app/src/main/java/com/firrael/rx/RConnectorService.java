package com.firrael.rx;

import android.graphics.Bitmap;

import com.firrael.rx.model.AddUserResult;
import com.firrael.rx.model.CreateGroupResult;
import com.firrael.rx.model.GroupFetchResult;
import com.firrael.rx.model.ImageResult;
import com.firrael.rx.model.Message;
import com.firrael.rx.model.RemoveUserResult;
import com.firrael.rx.model.SendFCMTokenResult;
import com.firrael.rx.model.SendMessageResult;
import com.firrael.rx.model.SendPNResult;
import com.firrael.rx.model.UserResult;
import com.firrael.rx.view.ChatUser;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by firrael on 25.05.2016.
 */
public interface RConnectorService {
    //String API_ENDPOINT = "http://127.0.0.1:3000";
    String API_ENDPOINT = "http://10.0.3.2:3000";

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
    Observable<CreateGroupResult> createGroup(@Field("title") String groupName, @Field("user_id") long creatorId);

    @FormUrlEncoded
    @POST("/group/fetch")
    Observable<GroupFetchResult> getGroups(@Field("user_id") long creatorId);

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
    @POST("/group/remove_user")
    Observable<RemoveUserResult> removeUser(@Field("login") String removeLogin, @Field("group_id") long groupId);

    @FormUrlEncoded
    @POST("/group/fetch_users")
    Observable<List<ChatUser>> fetchUsers(@Field("group_id") long groupId);

    @FormUrlEncoded
    @POST("/user/fcm_token")
    Observable<SendFCMTokenResult> sendFCMToken(@Field("user_id") long userId, @Field("fcm_token") String fcmToken);

    // TODO for testing
    @FormUrlEncoded
    @POST("/user/send_pns_to_everyone")
    Observable<SendPNResult> sendPNToEveryone(@Field("user_id") long userId, @Field("title") String title, @Field("text") String text);

    @FormUrlEncoded
    @POST("/user/send_pns_to_group")
    Observable<SendPNResult> sendPNToGroup(@Field("group_id") long groupId, @Field("title") String title, @Field("text") String text);
}