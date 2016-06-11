package com.postnov.android.intechtestapp.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by platon on 10.06.2016.
 */
public class Response
{
    @SerializedName("melodies")
    private List<Melodie> melodies;

    public List<Melodie> getMelodies()
    {
        return melodies;
    }

    public void setMelodies(List<Melodie> melodies)
    {
        this.melodies = melodies;
    }
}
