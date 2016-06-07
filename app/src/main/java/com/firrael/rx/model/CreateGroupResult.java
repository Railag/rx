package com.firrael.rx.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Railag on 02.06.2016.
 */
public class CreateGroupResult extends Result {
    public long id;
    public long creator;
    public String title;
    @SerializedName("image_url")
    public String imageUrl;
}