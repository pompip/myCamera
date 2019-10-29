package chong.mycamera.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;


public class MyTextView extends AppCompatTextView {
    private static final String TAG = "MyTextView";

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw: ");
    }


    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.d(TAG, "onVisibilityChanged() called with: changedView = [" + changedView + "], visibility = [" + visibility + "]");
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.d(TAG, "onWindowFocusChanged() called with: hasWindowFocus = [" + hasWindowFocus + "]");

    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        Log.d(TAG, "onFocusChanged() called with: focused = [" + focused + "], direction = [" + direction + "], previouslyFocusedRect = [" + previouslyFocusedRect + "]");
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        Log.d(TAG, "onWindowVisibilityChanged() called with: visibility = [" + visibility + "]");
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        Log.d(TAG, "onVisibilityAggregated() called with: isVisible = [" + isVisible + "]");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow() called");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged() called with: w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged() called with: newConfig = [" + newConfig + "]");
    }


    public boolean isViewCovered() {
        View currentView = this;
        View view = this;

        Rect currentViewRect = new Rect();
        boolean partVisible = currentView.getGlobalVisibleRect(currentViewRect);
        boolean totalHeightVisible = (currentViewRect.bottom - currentViewRect.top) >= view.getMeasuredHeight();
        boolean totalWidthVisible = (currentViewRect.right - currentViewRect.left) >= view.getMeasuredWidth();
        boolean totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible;
        if (!totalViewVisible)//if any part of the view is clipped by any of its parents,return true
            return true;

        while (currentView.getParent() instanceof ViewGroup) {
            ViewGroup currentParent = (ViewGroup) currentView.getParent();
            if (currentParent.getVisibility() != View.VISIBLE)//if the parent of view is not visible,return true
                return true;

            int start = currentParent.indexOfChild(currentView);
            for (int i = start + 1; i < currentParent.getChildCount(); i++) {
                View otherView = currentParent.getChildAt(i);
                Rect otherViewRect = new Rect();
                otherView.getGlobalVisibleRect(otherViewRect);
                if (Rect.intersects(currentViewRect, otherViewRect))//if view intersects its older brother(covered),return true
                    return true;
            }
            currentView = currentParent;
        }
        return false;
    }


    public boolean isVisiable() {
        if (!isShown()) {
            return false;
        }
        Rect rect = new Rect();
        boolean globalVisibleRect = getGlobalVisibleRect(rect);
        if (!globalVisibleRect) {
            return false;
        }

        View currentView = this;
        while (currentView.getParent() instanceof ViewGroup) { //遍历父控件
            ViewGroup currentParent = (ViewGroup) currentView.getParent();
            int start = currentParent.indexOfChild(currentView);
            for (int i = start + 1; i < currentParent.getChildCount(); i++) { //从自己开始遍历同级控件
                View otherView = currentParent.getChildAt(i);
                if (otherView.getVisibility() != VISIBLE) {
                    continue;
                }
                ArrayDeque<View> arrayDeque = new ArrayDeque<View>();
                arrayDeque.addLast(otherView);
                while (!arrayDeque.isEmpty()) { //遍历子控件
                    View first = arrayDeque.getFirst();
                    Rect currentRect = new Rect();
                    first.getGlobalVisibleRect(currentRect);
                    if (Rect.intersects(rect, currentRect)) {
                        if (first instanceof ViewGroup) {
                            int count = ((ViewGroup) first).getChildCount();
                            for (int j = 0; j < count; j++) {
                                arrayDeque.addLast(((ViewGroup) first).getChildAt(j));
                            }
                        } else {
                            Log.e(TAG, "checkChild: "+otherView );
                            return false;
                        }
                    }
                    arrayDeque.pollFirst();
                }

            }
            currentView = currentParent;
        }
        return true;

    }

    void checkChild(View view, Rect rect) {


    }

}
