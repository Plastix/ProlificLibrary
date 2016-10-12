package io.github.plastix.prolificlibrary.data.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class Book {

    public String author;

    public String categories;

    public int id;

    @SerializedName("lastCheckedOut")
    public Date checkedOutDate;

    @SerializedName("lastCheckedOutBy")
    public String checkedOutAuthor;

    public String title;

    public String publisher;

    public String url;

}
