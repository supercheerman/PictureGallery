package com.example.picturegallery;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

        try{
            InputStream is = getAssets().open("mypic.jpg");
            mLargeImageView.setInputStream(is);
        }catch (IOException e){

        }



    }

}
