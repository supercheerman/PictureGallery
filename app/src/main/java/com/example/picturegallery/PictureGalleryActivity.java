package com.example.picturegallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class PictureGalleryActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return PictureGalleryFragment.newInstance();
    }
}
