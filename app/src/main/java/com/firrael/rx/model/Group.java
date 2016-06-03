package com.firrael.rx.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Railag on 02.06.2016.
 */
public class Group implements Parcelable {
    private long id;
    private String title;
    private String imageUrl;

    protected Group(Parcel in) {
        id = in.readLong();
        title = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(imageUrl);
    }
}
