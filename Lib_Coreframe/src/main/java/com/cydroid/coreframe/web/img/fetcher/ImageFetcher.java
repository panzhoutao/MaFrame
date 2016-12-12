/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cydroid.coreframe.web.img.fetcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.widget.Toast;

import com.cydroid.coreframe.tool.util.LogUtil;
import com.cydroid.coreframe.web.http.HttpsClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple subclass of {@link ImageResizer} that fetches and resizes images fetched from a URL.
 */
public class ImageFetcher extends ImageResizer {
    private static final String TAG = "ImageFetcher";
//    private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
//    public static final String HTTP_CACHE_DIR = "http";

    /**
     * Initialize providing a target image width and height for the processing images.
     *
     * @param context
     * @param imageWidth
     * @param imageHeight
     */
    public ImageFetcher(Context context, int imageWidth, int imageHeight) {
        super(context, imageWidth, imageHeight);
        init(context);
    }

    /**
     * Initialize providing a single target image size (used for both width and height);
     *
     * @param context
     * @param imageSize
     */
    public ImageFetcher(Context context, int imageSize) {
        super(context, imageSize);
        init(context);
    }

    private void init(Context context) {
        checkConnection(context);
    }

    /**
     * Simple network connection check.
     *
     * @param context
     */
    private void checkConnection(Context context) {
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            Toast.makeText(context, "No network connection found.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "checkConnection - no connection found");
        }
    }

    /**
     * The main process method, which will be called by the ImageWorker in the AsyncTask background
     * thread.
     *
     * @param url The data to load the bitmap, in this case, a regular http URL
     * @return The downloaded and resized bitmap
     */
    private int inSampleSize=1;

    public Bitmap getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setReadTimeout(10 * 1000);
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestMethod("GET");
        InputStream is = null;
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            is = conn.getInputStream();
        } else {
            is = null;
        }
        if (is == null){
            throw new RuntimeException("stream is null");
        } else {
            try {
                byte[] data=readStream(is);
                if(data!=null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    return bitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            is.close();
            return null;
        }
    }

    /*
         * 得到图片字节流 数组大小
         * */
    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }



    public void setInSampleSize(int inSampleSize) {
        this.inSampleSize = inSampleSize;
    }

    private Bitmap processBitmap(String url) {
//        Bitmap bitmap=null;
//        try {
//            bitmap= getImage(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bitmap;
        LogUtil.i("processBitmap===="+url);
        Bitmap bitmap = null;
        // AndroidHttpClient is not allowed to be used from the main thread
        final HttpClient client =AndroidHttpClient.newInstance("Android");
//        final HttpClient client = HttpsClient.getInstance()
//                .getHttpsClient();
        final HttpGet getRequest = new HttpGet(url);
        client.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                5000);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                FlushedInputStream mFlushedInputStream=new FlushedInputStream(inputStream);
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeStream(mFlushedInputStream,null,options);
////                    int imageHeight = options.outHeight;
////                    int imageWidth = options.outWidth;
//
//                    int calculate=calculateInSampleSize(options,720,1080);
////                    options = new BitmapFactory.Options();
////                    inputStream.reset();
                    options.inSampleSize = inSampleSize;
                    options.inJustDecodeBounds = false;
                    LogUtil.i("inSampleSize===="+inSampleSize);

                	bitmap = BitmapFactory.decodeStream(mFlushedInputStream,null,options);
                } catch(OutOfMemoryError e){
                	BitmapFactory.Options options = new BitmapFactory.Options();
                	options.inSampleSize = 4;
                    bitmap = BitmapFactory.decodeStream(mFlushedInputStream,null,options);
                }
                finally {
                    if (inputStream != null) {
                        inputStream.close();
                        inputStream = null;
                    }
                    entity.consumeContent();
                }
            }
            return bitmap;
        } catch (IOException e) {
            getRequest.abort();
            Log.w("LOG_TAG", "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) {
            getRequest.abort();
            Log.w("LOG_TAG", "Incorrect URL: " + url);
        } catch (Exception e) {
            getRequest.abort();
            Log.w("LOG_TAG", "Error while retrieving bitmap from " + url, e);
        } finally {
            if ((client instanceof AndroidHttpClient)) {
                ((AndroidHttpClient) client).close();
            }

        }
        return null;
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }
    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
        	Log.i(""+n, "");
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
   /* private Bitmap processBitmap(String data) {
        if (BuildConfig.DEBUG) {
//            Log.d(TAG, "processBitmap - " + data);
        }

        // Download a bitmap, write it to a file
        final File f = downloadBitmap(mContext, data);

        if (f != null) {
            // Return a sampled down version
            return decodeSampledBitmapFromFile(f.toString(), mImageWidth, mImageHeight);
        }

        return null;
    }*/

    @Override
    protected Bitmap processBitmap(Object data) {
        return processBitmap(String.valueOf(data));
    }

    /**
     * Download a bitmap from a URL, write it to a disk and return the File pointer. This
     * implementation uses a simple disk cache.
     *
     * @param context The context to use
     * @param urlString The URL to fetch
     * @return A File pointing to the fetched bitmap
     */
    /*
    public static File downloadBitmap(Context context, String urlString) {
        final File cacheDir = DiskLruCache.getDiskCacheDir(context, HTTP_CACHE_DIR);

        final DiskLruCache cache =
                DiskLruCache.openCache(context, cacheDir, HTTP_CACHE_SIZE);

        final File cacheFile = new File(cache.createFilePath(urlString));

        if (cache.containsKey(urlString)) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "downloadBitmap - found in http cache - " + urlString);
            }
            return cacheFile;
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "downloadBitmap - downloading - " + urlString);
        }

        Utils.disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            final InputStream in =
                    new BufferedInputStream(urlConnection.getInputStream(), Utils.IO_BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(cacheFile), Utils.IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }

            return cacheFile;

        } catch (final IOException e) {
            Log.e(TAG, "Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error in downloadBitmap - " + e);
                }
            }
        }

        return null;
    }*/
}
