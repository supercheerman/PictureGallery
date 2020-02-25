package com.example.picturegallery;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

class MoveGestureDetector extends BaseGestureDetector {

    private PointF mCurrentPointer;
    private PointF mPrePointer;

    //最终结果
    private PointF mExternalPointer = new PointF();

    private OnMoveGestureListener mListener;

    public MoveGestureDetector(Context context,OnMoveGestureListener listener){

        super(context);
        mListener =listener;

    }

    /*
    * 有事件发生时LargeImageView调用OnTouchEvent将其传入到MoveGestureDetector的OnTouchEvent方法中
    * 该方法调用handleInProgressEvent和handleStartProgressEvent其中一个。
    * */
    @Override
    protected void handleInProgressEvent(MotionEvent event) {

        int actionCode =event.getAction()&MotionEvent.ACTION_MASK;//获取低8位代表动作类型，高位代表不同的点，用以多点触摸使用
        switch(actionCode){
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                //点击离开时调用onMoveEnd方法
                mListener.onMoveEnd(this);
                resetState();//重置mGestureInProgress判断量和MotionEvent(见父类)
                break;
            case MotionEvent.ACTION_MOVE:
                updateStateByEvent(event);
                mListener.onMove(this);

        }

    }

    @Override
    protected void handleStartProgressEvent(MotionEvent event)
    {
        int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
        switch (actionCode)
        {
            case MotionEvent.ACTION_DOWN:
                resetState();//防止没有接收到CANCEL or UP ,保险起见，或者第一次使用时
                mPreMotionEvent = MotionEvent.obtain(event);
                updateStateByEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                this.mGestureInProgress = mListener.onMoveBegin(this);//onMoveBegin返回为真代表开始移动，下次调用handleInProcess方法
                break;
        }

    }


    /*
    * 从事件中获取点击点并且储存在mCurrentPointer中，并且将上一次点赋给mPrePointer
    * 计算2点之间移动的位移储存到mExternalPointer中。
    *
    * */
    @Override
    protected void updateStateByEvent(MotionEvent event)
    {
        final MotionEvent prev = mPreMotionEvent;

        mPrePointer = caculateFocalPointer(prev);
        mCurrentPointer = caculateFocalPointer(event);

        //Log.e("TAG", mPrePointer.toString() + " ,  " + mCurrentPointer);

        boolean mSkipThisMoveEvent = prev.getPointerCount() != event.getPointerCount();

        //Log.e("TAG", "mSkipThisMoveEvent = " + mSkipThisMoveEvent);
        mExternalPointer.x = mSkipThisMoveEvent ? 0 : mCurrentPointer.x - mPrePointer.x;
        mExternalPointer.y = mSkipThisMoveEvent ? 0 : mCurrentPointer.y - mPrePointer.y;

    }

    /**
     * 根据event计算多指中心点
     *多次点击点取中心值
     */
    private PointF caculateFocalPointer(MotionEvent event)
    {
        final int count = event.getPointerCount();
        float x = 0, y = 0;
        for (int i = 0; i < count; i++)
        {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= count;
        y /= count;

        return new PointF(x, y);
    }


    public float getMoveX()
    {
        return mExternalPointer.x;

    }

    public float getMoveY()
    {
        return mExternalPointer.y;
    }


    public interface OnMoveGestureListener{

        public boolean onMoveBegin(MoveGestureDetector detector);

        public boolean onMove(MoveGestureDetector detector);

        public void onMoveEnd(MoveGestureDetector detector);
    }

    public static class SimpleMoveGestureDetector implements OnMoveGestureListener{

        @Override
        public boolean onMoveBegin(MoveGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onMove(MoveGestureDetector detector) {
            return false;
        }

        @Override
        public void onMoveEnd(MoveGestureDetector detector) {

        }
    }
}
