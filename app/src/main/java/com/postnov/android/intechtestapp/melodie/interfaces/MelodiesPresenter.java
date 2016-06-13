package com.postnov.android.intechtestapp.melodie.interfaces;

/**
 * Created by platon on 10.06.2016.
 */
public interface MelodiesPresenter
{
    void fetchMelodies(int limit, int from);
    void bind(MelodiesView view);
    void unbind();
    void unsubscribe();
}
