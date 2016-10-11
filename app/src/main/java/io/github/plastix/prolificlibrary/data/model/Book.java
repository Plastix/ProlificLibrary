package io.github.plastix.prolificlibrary.data.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class Book {

    String author;

    String categories;

    int id;

    @SerializedName("lastCheckedOut")
    Date checkedOutDate;

    @SerializedName("lastCheckedOutBy")
    String checkedOutAuthor;

    String title;

    String url;

}
