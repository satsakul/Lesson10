package com.yandex.academy.lesson10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;

/*
 * The AsyncTask class must be loaded on the UI thread.
 * This is done automatically as of JELLY_BEAN.
 */
class MyAsyncTask extends AsyncTask<Void, Void, Drawable> {

    private final ImageLoader mImageLoader;
    private final WeakReference<Context> mContextReference;

    private WeakReference<AsyncTaskListener> mAsyncTaskListenerReference;

    interface AsyncTaskListener {
        void onPreExecute();
        void onPostExecute(final Drawable bitmapDrawable);
    }

    MyAsyncTask(@NonNull final Context context) {
        mImageLoader = new ImageLoader();
        mContextReference = new WeakReference<>(context);
    }

    void setAsyncTaskListener(final AsyncTaskListener asyncTaskListener) {
        mAsyncTaskListenerReference = new WeakReference<>(asyncTaskListener);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        final AsyncTaskListener asyncTaskListener = mAsyncTaskListenerReference.get();
        if (asyncTaskListener != null) {
            asyncTaskListener.onPreExecute();
        }
    }

    @Override
    protected Drawable doInBackground(final Void... params) {
        Drawable drawable = null;
        if (isCancelled() == false) {
            final AsyncTaskListener asyncTaskListener = mAsyncTaskListenerReference.get();
            for (int i = 0; i < 10; i++) {
                Log.i("TAG", "i = " + i
                        + ", AsyncTaskListener = " + (asyncTaskListener != null ? asyncTaskListener.hashCode() : null)
                        + ", AsyncTask = " + this.hashCode());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            drawable = loadImage();
        }

        return drawable;
    }

    @Nullable
    private Drawable loadImage() {
        Drawable bitmapDrawable = null;
        final Context context = mContextReference.get();
        if (context != null) {
            final String imageUrl = mImageLoader.getImageUrl();
            if (TextUtils.isEmpty(imageUrl) == false) {
                final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
                bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
            }
        }

        return bitmapDrawable;
    }

    @Override
    protected void onPostExecute(final Drawable bitmapDrawable) {
        super.onPostExecute(bitmapDrawable);

        final AsyncTaskListener asyncTaskListener = mAsyncTaskListenerReference.get();
        if (asyncTaskListener != null) {
            asyncTaskListener.onPostExecute(bitmapDrawable);
        }
    }
}
