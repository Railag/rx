package com.firrael.rx.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by firrael on 25.05.2016.
 */
public class Post extends RealmObject {
    public long userId;

    @PrimaryKey
    public long id;
    public String title;

    @SerializedName("body")
    public String text;
}

/*
{
        "userId": 1,
        "id": 1,
        "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
}*/
