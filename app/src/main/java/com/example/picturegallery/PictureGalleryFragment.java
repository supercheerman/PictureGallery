package com.example.picturegallery;

import android.graphics.Bitmap;
import android.graphics.PostProcessor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PictureGalleryFragment extends Fragment {

    private static final String TAG = "PictureGalleryFragment";
    private List<PictureItem> mPictures;
    private RecyclerView mRecyclerView;


    public static PictureGalleryFragment newInstance() {

        Bundle args = new Bundle();
        PictureGalleryFragment fragment = new PictureGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        InitFresco();
        new FetchItemsTask().execute();


    }
    private void InitFresco(){//
        ImagePipelineConfig  config = ImagePipelineConfig.newBuilder(getActivity())
                .setDownsampleEnabled(true)// 对图片进行自动缩放
                .setResizeAndRotateEnabledForNetwork(true)// 对网络图片进行resize处理，减少内存消耗
                .setBitmapsConfig(Bitmap.Config.RGB_565) //图片设置RGB_565，减小内存开销  fresco默认情况下是RGB_8888
                .build();
        Fresco.initialize(getActivity(),config);
    }


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,Bundle savedInstanceState){
        View view = layoutInflater.inflate(R.layout.fragment_picture_gallery,container,false);
        //mPictures =new UrlFetcher().fetchItems();
        mRecyclerView = view.findViewById(R.id.picture_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        return view;
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,List<PictureItem>> {
        //第一个类型参数可指定excute方法接受参数然后传给doInBackground的参数类型
        @Override
        protected List<PictureItem> doInBackground(Void... params){
            return new UrlFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<PictureItem> items){//在doInBackground方法之后调用，而且是在主线程调用
            mPictures =items;
            mRecyclerView.setAdapter(new PictureAdapter(mPictures));

        }
    }
    private class PictureHolder extends RecyclerView.ViewHolder{
        //类负责绑定图片和ImageView
        private SubsamplingScaleImageView mImageView;

        public PictureHolder(View view){
            super(view);
            mImageView= view.findViewById(R.id.item_picture_view);
        }
        public void bindPicture(String url){
            mImageView.setImage(ImageSource.uri(url));
        }
        public void bindText(String string){

        }
    }

    private class PictureAdapter extends RecyclerView.Adapter<PictureHolder>{
        private List<PictureItem> mPictureItems;

        public PictureAdapter(List<PictureItem> items){mPictureItems=items;}
        @NonNull
        @Override
        public PictureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //创建View用来创建ViewHolder
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.picture_lsit_item,parent,false);
            return new PictureHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PictureHolder holder, int position) {
            PictureItem bindItem = mPictureItems.get(position);
            holder.bindPicture(bindItem.getUrl());
        }

        @Override
        public int getItemCount() {
            return mPictureItems.size();
        }
    }


}
