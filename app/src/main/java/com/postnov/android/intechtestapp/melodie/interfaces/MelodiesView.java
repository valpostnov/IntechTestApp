package com.postnov.android.intechtestapp.melodie.interfaces;

import com.postnov.android.intechtestapp.data.entity.Melodie;

import java.util.List;

/**
 * Created by platon on 10.06.2016.
 */
public interface MelodiesView
{
    void showMelodies(List<Melodie> melodies);
    void showProgressDialog();
    void hideProgressDialog();
    void showError(String error);
}
