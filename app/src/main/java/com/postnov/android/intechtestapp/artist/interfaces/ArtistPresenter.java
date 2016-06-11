package com.postnov.android.intechtestapp.artist.interfaces;

/**
 * Created by platon on 10.06.2016.
 */
public interface ArtistPresenter
{
    void fetchArtists(String limit, String from);
    void bind(ArtistView view);
    void unbind();
    void unsubscribe();
}
