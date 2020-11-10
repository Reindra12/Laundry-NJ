package com.example.enjelaundry.grafik;

/**
 * Created by robbydianputra on 5/3/17.
 */

import android.graphics.Typeface;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Baseclass of all Activities of the Demo Application.
 *
 * @author Philipp Jahoda
 */
public abstract class DemoBase extends AppCompatActivity {//FragmentActivity {

    protected String[] mMonths = new String[] {
            "Januari", "Februari", "Maret", "April", "Mei","Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    protected String[] mParties = new String[] {
            "Minggu Pertama", "Minggu Kedua", "Minggu Ketiga", "Minggu Keempat"
    };

    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

}