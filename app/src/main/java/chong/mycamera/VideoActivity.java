package chong.mycamera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.ViewDragHelper;
import androidx.fragment.app.Fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Method;

import chong.mycamera.view.MyTextView;

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = "VideoActivity";
    MyTextView level1;
    TextView level2;

    ViewDragHelper viewDragHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        level1 = findViewById(R.id.level1);
        level2 = findViewById(R.id.level2);

        findViewById(R.id.toggle1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level1.setVisibility(level1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        findViewById(R.id.toggle2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level2.setVisibility(level2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        findViewById(R.id.toggle3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "isVisiable:  " + level1.isVisiable());
                Log.e(TAG, "isViewCovered:  " + level1.isViewCovered());
            }
        });

    }

}
