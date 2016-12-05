package ie.pdprojects.personaldiary;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

// Pictures utility class used by DiaryFragment class
public class ImageManager
{

    // Rotate picture
    public static  Bitmap rotateBitmap(File mPhotoFile, Bitmap myBitmap)
    {

        try {
            ExifInterface exif = new ExifInterface(mPhotoFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6)
            {
                matrix.postRotate(90);
            }
            else if (orientation == 3)
            {
                matrix.postRotate(180);
            }
            else if (orientation == 8)
            {
                matrix.postRotate(270);
            }
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
        }
        catch (Exception e)
        {

        }

        return myBitmap;
    }

    // Decode bitmap
    public static Bitmap setPic(File mPhotoFile, ImageView mImageView)
    {
        if (mPhotoFile.exists() && mPhotoFile != null)
        {
            String mCurrentPhotoPath = mPhotoFile.getAbsolutePath();
            // Get the dimensions of the View
            float mTargetW = mImageView.getWidth();
            float mTargetH = mImageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            float photoW = bmOptions.outWidth;
            float photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            //int scaleFactor = (int) Math.min(photoW / mTargetW, photoH / mTargetH);
            int scaleFactor = 3;

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = scaleFactor;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            Bitmap rotatedBitmap = rotateBitmap(mPhotoFile, bitmap);

            return rotatedBitmap;
        }
        else
        {
            return null;
        }
    }

    // Create copy if used picture from different directory
    public static void copy(File src, File dst) throws IOException
    {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }//


}

