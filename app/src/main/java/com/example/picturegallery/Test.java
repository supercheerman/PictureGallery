package com.example.picturegallery;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

public class Test extends AppCompatActivity
{
    private LargeImageView mLargeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);

        mLargeImageView = (LargeImageView) findViewById(R.id.id_largetImageview);
        try
        {

            InputStream inputStream = getAssets().open("mypic.jpg");
            mLargeImageView.setInputStream(inputStream);

        } catch (IOException e)
        {
            Log.e("LargeImageView","@______@:exception",e);
        }


    }

}
