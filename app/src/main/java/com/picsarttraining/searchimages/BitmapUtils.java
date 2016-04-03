package com.picsarttraining.searchimages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Arsen on 04.04.2016.
 */
public class BitmapUtils {
    public static Bitmap decodeUrl(Context context, URL url, final int requiredSize)
            throws IOException {

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o2);
    }

    public static void loadImagesFromUrls(final Context context, ArrayList<URL> imageUrls, final int requiredSize, final BitmapsLoadCallback bitmapsLoadCallback) {
        URL[] urls = new URL[imageUrls.size()];
        urls = imageUrls.toArray(urls);
        final URL[] finalUrls = urls;
        new AsyncTask<Void, Bitmap, ArrayList<Bitmap>>() {

            @Override
            protected ArrayList<Bitmap> doInBackground(Void... params) {
                ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
                for(URL url : finalUrls)
                {
                    try {
                        bitmaps.add(decodeUrl(context, url, requiredSize));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return bitmaps;
            }

            @Override
            protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
                bitmapsLoadCallback.onBitmapLoad(bitmaps);
            }
        }.execute();
    }

    public interface BitmapsLoadCallback{
        void onBitmapLoad(ArrayList<Bitmap> bitmaps);
    }
}
