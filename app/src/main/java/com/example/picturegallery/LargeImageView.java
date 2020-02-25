package com.example.picturegallery;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

import static android.graphics.BitmapFactory.*;

public class LargeImageView extends View {

    private static final String TAG ="LargeImageView";

    private BitmapRegionDecoder mDecoder;

    private int mImageWidth,mImageHeight;

    private volatile Rect mRect;

    private MoveGestureDetector mDetector;

    private static final Options options = new Options();

    public LargeImageView(Context context, AttributeSet attrs) {
        super(context,attrs);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public void setInputStream(InputStream is){
        try{
            mDecoder = BitmapRegionDecoder.newInstance(is,false);
            Options tmpOptions =new Options();

            tmpOptions.inJustDecodeBounds = true;//对于较大图片不载入内容而获取对象的信息，处理图像前改为false
            BitmapFactory.decodeStream(is,null,tmpOptions);
            mImageWidth = tmpOptions.outWidth;
            mImageHeight = tmpOptions.outHeight;
            requestLayout();
            invalidate();
        }catch (IOException e){
            Log.e(TAG,"@_____@:",e);
        }finally {
            try{
                if(is!=null){
                    is.close();
                }
            }catch (Exception e){

            }
        }
    }

    public void init(){
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.OnMoveGestureListener() {
            @Override
            public boolean onMoveBegin(MoveGestureDetector detector) {
                int moveX =(int)detector.getMoveX();
                int moveY =(int)detector.getMoveY();
                if(mImageWidth>getWidth()){

                    mRect.offset(-moveX,0);
                    checkWidth();
                    invalidate();

                }if(mImageHeight>getHeight()){

                    mRect.offset(0,-moveY);
                    checkHeight();
                    invalidate();
                }
                return true;

            }

            @Override
            public boolean onMove(MoveGestureDetector detector) {
                return false;
            }

            @Override
            public void onMoveEnd(MoveGestureDetector detector) {

            }
        });
    }
    private void checkWidth() {

        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;
        if(rect.right>imageWidth){
            rect.right =imageWidth;
            rect.left = imageWidth -getWidth();
        }
        if(rect.left<0){

            rect.left=0;
            rect.right =getWidth();
        }
    }

    private void checkHeight() {
        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.bottom > imageHeight)
        {
            rect.bottom = imageHeight;
            rect.top = imageHeight - getHeight();
        }

        if (rect.top < 0)
        {
            rect.top = 0;
            rect.bottom = getHeight();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Bitmap bm = mDecoder.decodeRegion(mRect, options);
        canvas.drawBitmap(bm, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        //默认直接显示图片的中心区域，可以自己去调节
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;

    }
}
