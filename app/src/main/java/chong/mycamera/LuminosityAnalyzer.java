package chong.mycamera;

import android.util.Log;

import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class LuminosityAnalyzer implements ImageAnalysis.Analyzer {
    private long lastAnalyzedTimestamp = 0L;

    /**
     * Helper extension function used to extract a byte array from an
     * image plane buffer
     */
    private byte[] toByteArray(ByteBuffer byteBuffer) {
        byteBuffer.rewind();   // Rewind the buffer to zero
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);  // Copy the buffer into a byte array
        return data; // Return the byte array
    }

    @Override
    public void analyze(ImageProxy image, int rotationDegrees) {
        long currentTimestamp = System.currentTimeMillis();
        // Calculate the average luma no more often than every second
        if (currentTimestamp - lastAnalyzedTimestamp >=
                TimeUnit.SECONDS.toMillis(1)) {
            // Since format in ImageAnalysis is YUV, image.planes[0]
            // contains the Y (luminance) plane
            java.nio.ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            // Extract image data from callback object
            byte[] data = toByteArray(buffer);
            // Convert the data into an array of pixel values
            int total = 0;
            for (byte datum : data) {
                total += (int) datum & 0xFF;
            }
            // Compute average luminance for the image

            // Log the new luma value
            Log.d("CameraXApp", "Average luminosity: "+total/data.length);
            // Update timestamp of last analyzed frame
            lastAnalyzedTimestamp = currentTimestamp;
        }
    }
}
