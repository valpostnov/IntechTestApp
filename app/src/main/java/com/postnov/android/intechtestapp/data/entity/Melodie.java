package com.postnov.android.intechtestapp.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by platon on 10.06.2016.
 */
public class Melodie implements Parcelable
{
    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("artistId")
    private long artistId;

    @SerializedName("artist")
    private String artist;

    @SerializedName("picUrl")
    private String picUrl;

    @SerializedName("demoUrl")
    private String demoUrl;

    public Melodie() {}

    protected Melodie(Parcel in)
    {
        id = in.readLong();
        title = in.readString();
        artistId = in.readLong();
        artist = in.readString();
        picUrl = in.readString();
        demoUrl = in.readString();
    }

    public static final Creator<Melodie> CREATOR = new Creator<Melodie>()
    {
        @Override
        public Melodie createFromParcel(Parcel in)
        {
            return new Melodie(in);
        }

        @Override
        public Melodie[] newArray(int size)
        {
            return new Melodie[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public void setDemoUrl(String demoUrl) {
        this.demoUrl = demoUrl;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeLong(artistId);
        dest.writeString(artist);
        dest.writeString(picUrl);
        dest.writeString(demoUrl);
    }
}
