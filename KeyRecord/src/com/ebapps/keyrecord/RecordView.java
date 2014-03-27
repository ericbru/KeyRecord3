package com.ebapps.keyrecord;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class RecordView extends SurfaceView implements SurfaceHolder.Callback{
	 
    String TAG="RecordView";
    private DrawRecordThread _thread;
 
    public RecordView(Context context) {
        super(context);
        getHolder().addCallback(this);
        _thread = new DrawRecordThread(getHolder(), context);
        setFocusable(true);
    }
 
    
    public RecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        _thread = new DrawRecordThread(getHolder(), context);
        setFocusable(true);
    }
 
    /**
     * Fetches the animation thread
     *
     * @return the animation thread
     */
    public DrawRecordThread getThread() {
        return _thread;
    }
 
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        _thread.setRunning(true);
        _thread.start();
    }
 
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        _thread.setRunning(false);
        while (retry) {
            try {
                _thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
     
    	if (event.getAction() == MotionEvent.ACTION_MOVE
        	    || event.getAction() == MotionEvent.ACTION_DOWN) {
     
               _thread.handleActionDown((int)event.getX(), (int)event.getY(), event);
        }
        
        if (event.getAction() == MotionEvent.ACTION_MOVE
        	    || event.getAction() == MotionEvent.ACTION_DOWN) {

        	}
        
        if (event.getAction() == MotionEvent.ACTION_UP) {
               // touch was released
               if (_thread.isTouched()) {
                _thread.setTouched(false);
                }
              }
              return true;
    }
    
    
    
    
}
//............................................................................................
class DrawRecordThread extends Thread {
    String TAG="TutorialThread";
    private SurfaceHolder _surfaceHolder;
    private boolean _run = false;
    Bitmap bitmapScreen;
    private Canvas c;
    private Paint paint;
    int myColor;
 
    /**
     * State-tracking constants.
     */
    public static final int STATE_STOPPED = 0;
    public static final int STATE_RUNNING = 1;
    public int mState=STATE_STOPPED;
 
    Resources mRes;
 
    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;
 
    float fingerx,fingery;
	private boolean touched;
 
    public DrawRecordThread(SurfaceHolder surfaceHolder, Context context) {
        _surfaceHolder = surfaceHolder;
        mContext = context;
        mRes = context.getResources();
        myColor = context.getResources().getColor(R.color.green);
        paint.setColor(myColor);
       // create droid and load bitmap
       //bitmapDroid=BitmapFactory.decodeResource(mRes, R.drawable.android);
 
    }
 
    public void setRunning(boolean run) {
        _run = run;
    }
    
    public void setRecordState(int mode) {
        synchronized (_surfaceHolder) {
            if (mode == STATE_RUNNING) {
                mState=STATE_RUNNING;
 
              } 
 
            else if(mode == STATE_STOPPED)
            {
                 mState=STATE_STOPPED;
            }
        }
    }
 
    @Override
    public void run() {
        while (_run) {
            c = null;
            try {
                c = _surfaceHolder.lockCanvas(null);
                synchronized (_surfaceHolder) {
                    onDraw(c);
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    _surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }
 
    public void onDraw(Canvas canvas) {
 
        //canvas.drawColor(Color.rgb(170, 218, 0));
        //canvas.drawBitmap(bitmapDroid, droidx - (bitmapDroid.getWidth() / 2), droidy - (bitmapDroid.getHeight() / 2), null);
 
    }
    
    public void handleActionDown(int eventX, int eventY, MotionEvent event)
    {
                if(mState==STATE_RUNNING){

              	  int p = event.getPointerCount();
              	     for (int i = 0; i < p; i++) { 
              	       c.drawPoint(event.getX(i), event.getY(i), paint);
              	     }
                }
                else{
                    setTouched(false);
                }
 
     }
 
     public void setTouched(boolean touched) {
          this.touched = touched;
         }
 
     public boolean isTouched() {
          return touched;
         }
 
}