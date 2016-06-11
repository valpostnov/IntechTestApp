package com.postnov.android.intechtestapp.artist.interfaces;

import com.postnov.android.intechtestapp.data.entity.Melodie;

import java.util.List;

/**
 * Created by platon on 10.06.2016.
 */
public interface ArtistView
{
    void showArtists(List<Melodie> melodies);
    void showProgressDialog();
    void hideProgressDialog();
    void showError(String error);
}
