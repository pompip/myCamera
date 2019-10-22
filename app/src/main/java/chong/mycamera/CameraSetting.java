package chong.mycamera;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.UseCase;

import java.io.File;

public class CameraSetting {
    int width, height;
    TextureView textureView;
    Context context;
    private static final String TAG = "CameraSetting";
    private ImageCapture imageCapture;
    ImageAnalysis analyzerUseCase;
    Preview preview;

    public CameraSetting(TextureView textureView, Context context) {
        this.textureView = textureView;
        this.context = context;
        width = textureView.getWidth();
        height = textureView.getHeight();
        buildAnalyzer();
        buildPreview();
        buildImageCapture();
    }

    private void buildPreview() {
        PreviewConfig previewConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(new Rational(width, height))
                .setTargetResolution(new Size(width, height))
                .build();

        // Build the viewfinder use case
        preview = new Preview(previewConfig);

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                // To update the SurfaceTexture, we have to remove it and re-add it
                ViewGroup parent = (ViewGroup) textureView.getParent();
                parent.removeView(textureView);
                parent.addView(textureView, 0);

                textureView.setSurfaceTexture(output.getSurfaceTexture());
                updateTransform();
            }
        });

    }

    private void buildImageCapture() {

        // Create configuration object for the image capture use case
        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                .setTargetAspectRatio(new Rational(width, height))
                // We don't set a resolution for image capture; instead, we
                // select a capture mode which will infer the appropriate
                // resolution based on aspect ration and requested mode
                .setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
                .build();

        // Build the image capture use case and attach button click listener
        imageCapture = new ImageCapture(imageCaptureConfig);

    }

    public UseCase[] buildUseCase() {
        return new UseCase[]{preview, imageCapture, analyzerUseCase};
    }

    public void takePicture() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
        Log.d(TAG, "onClick: " + file.getAbsolutePath());
        imageCapture.takePicture(file,
                new ImageCapture.OnImageSavedListener() {

                    @Override
                    public void onImageSaved(File file) {
                        String msg = new String("Photo capture succeeded: " + file.getAbsolutePath());
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        Log.d("CameraXApp", msg);
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
                        Log.e(TAG, "onError: :" + imageCaptureError.toString());
                        String msg = "Photo capture failed: " + message;
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        Log.e("CameraXApp", msg);
                        if (cause != null) {
                            cause.printStackTrace();
                        }
                    }
                });
    }

    public void updateTransform() {
        Matrix matrix = new Matrix();

        // Compute the center of the view finder
        float centerX = width / 2f;
        float centerY = height / 2f;

        // Correct preview output to account for display rotation
        int rotationDegrees;
        switch (textureView.getDisplay().getRotation()) {
            case Surface.ROTATION_0:
                rotationDegrees = 0;
                break;
            case Surface.ROTATION_90:
                rotationDegrees = 90;
                break;
            case Surface.ROTATION_180:
                rotationDegrees = 180;
                break;
            case Surface.ROTATION_270:
                rotationDegrees = 270;
                break;
            default:
                return;
        }
        matrix.postRotate(-rotationDegrees, centerX, centerY);
        // Finally, apply transformations to our TextureView
        textureView.setTransform(matrix);
    }

    private void buildAnalyzer() {
        // Setup image analysis pipeline that computes average pixel luminance
        HandlerThread analyzerThread = new HandlerThread(
                "LuminosityAnalysis");
        analyzerThread.start();

        ImageAnalysisConfig analyzerConfig = new ImageAnalysisConfig.Builder()
                // Use a worker thread for image analysis to prevent glitches

                .setCallbackHandler(new Handler(analyzerThread.getLooper()))
                // In our analysis, we care more about the latest image than
                // analyzing *every* image
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .build();

        // Build the image analysis use case and instantiate our analyzer
        analyzerUseCase = new ImageAnalysis(analyzerConfig);
        analyzerUseCase.setAnalyzer(new LuminosityAnalyzer());
    }


}
