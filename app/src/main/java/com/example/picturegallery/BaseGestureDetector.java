package com.example.picturegallery;

import android.content.Context;
import android.view.MotionEvent;

public abstract class  BaseGestureDetector {
    protected boolean mGestureInProgress;

    protected MotionEvent mPreMotionEvent;
    protected MotionEvent mCurrentMotionEvent;

    protected Context mContext;

    public BaseGestureDetector(Context context)
    {
        mContext = context;
    }


    public boolean onTouchEvent(MotionEvent event)
    {

        if (!mGestureInProgress)
        {
            handleStartProgressEvent(event);
        } else
        {
            handleInProgressEvent(event);
        }

        return true;

    }

    protected abstract void handleInProgressEvent(MotionEvent event);

    protected abstract void handleStartProgressEvent(MotionEvent event);

    protected abstract void updateStateByEvent(MotionEvent event);

    protected void resetState()
    {
        if (mPreMotionEvent != null)
        {
            mPreMotionEvent.recycle();
            mPreMotionEvent = null;
        }
        if (mCurrentMotionEvent != null)
        {
            mCurrentMotionEvent.recycle();
            mCurrentMotionEvent = null;
        }
        mGestureInProgress = false;
    }


}
 /*版权声明：本文为CSDN博主「鸿洋_」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
    原文链接：https://blog.csdn.net/lmj623565791/article/details/49300989/*/
