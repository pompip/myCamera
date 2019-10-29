package chong.mycamera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

import chong.mycamera.R;

public class MyDragFrameLayout extends FrameLayout {

    private ViewDragHelper viewDragHelper;

    private static final String TAG = "MyDragFrameLayout";

    public MyDragFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public MyDragFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyDragFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyDragFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return "move".equals(child.getTag());
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                Log.e(TAG, "onEdgeDragStarted() called with: edgeFlags = [" + edgeFlags + "], pointerId = [" + pointerId + "]");
                viewDragHelper.captureChildView(findViewById(R.id.toggle1), pointerId);

            }

            @Override
            public boolean onEdgeLock(int edgeFlags) {
                return super.onEdgeLock(edgeFlags);
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                Log.e(TAG, "onEdgeTouched() called with: edgeFlags = [" + edgeFlags + "], pointerId = [" + pointerId + "]");
            }

            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                capturedChild.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {

                float a = 10000.0f;
                double t = Math.sqrt(xvel * xvel + yvel * yvel) / a;
                double x = xvel * t / 2;
                double y = yvel * t / 2;
                viewDragHelper.settleCapturedViewAt(releasedChild.getLeft() + (int) x,
                        releasedChild.getTop() + (int) y);
                int left = releasedChild.getLeft();
                int top = releasedChild.getTop();
//                viewDragHelper.flingCapturedView(left-100,top-100,left+100,top+100);
                invalidate();
                releasedChild.animate().scaleX(1f).scaleY(1f).setDuration((long) (t * 1000)).start();


            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return top;
            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                View capturedView = viewDragHelper.getCapturedView();
                if (capturedView != null) {
                    if (state == ViewDragHelper.STATE_IDLE) {


                    } else if (state == ViewDragHelper.STATE_DRAGGING) {

                    }
                }
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                Log.e(TAG, "getViewVerticalDragRange: " + child + " slop:" + viewDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_HORIZONTAL));
                return 1;
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                Log.e(TAG, "getViewVerticalDragRange: " + child + " slop:" + viewDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL));
                return viewDragHelper.getTouchSlop();
            }
        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        View capturedView = viewDragHelper.getCapturedView();
        if (capturedView == null) {
            return super.onTouchEvent(event);
        }

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
