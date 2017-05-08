package com.yandex.academy.lesson10;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.yandex.academy.lesson10.asynctasklibrary.Library;

public class MainActivity extends AppCompatActivity {

    private View mRootLayout;
    private View mProgressBar;
    private ImageLoader mImageLoader;
    private AsyncTask<Void, Void, Drawable> mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageLoader = new ImageLoader();
        mRootLayout = findViewById(R.id.layout);
        mProgressBar = findViewById(R.id.progressBar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                 * The task instance must be created on the UI thread.
                 *
                 * Method execute(Params...) must be invoked on the UI thread.
                 *
                 * The task can be executed only once (an exception will be thrown
                 * if a second execution is attempted.)
                 */
                if (mAsyncTask != null) {
                    mAsyncTask.cancel(false);
                }

                Library.startAsyncTask();

                Log.d("TAG", "MainActivity, start MyAsyncTask");
                mAsyncTask = new MyAsyncTask().execute();
            }
        });
    }

    @Nullable
    private Drawable loadImage() {
        Drawable bitmapDrawable = null;
        final String imageUrl = mImageLoader.getImageUrl();
        if (TextUtils.isEmpty(imageUrl) == false) {
            final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
            bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        }

        return bitmapDrawable;
    }

    /*
     * The AsyncTask class must be loaded on the UI thread.
     * This is done automatically as of JELLY_BEAN.
     */
    private class MyAsyncTask extends AsyncTask<Void, Void, Drawable> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Drawable doInBackground(final Void... params) {
            Drawable drawable = null;
            if (isCancelled() == false) {
                for (int i = 0; i < 10; i++) {
                    Log.i("TAG", "MyAsyncTask, i = " + i
                            + ", Activity = " + MainActivity.this.hashCode()
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

        @Override
        protected void onPostExecute(final Drawable bitmapDrawable) {
            super.onPostExecute(bitmapDrawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mRootLayout.setBackground(bitmapDrawable);
            } else {
                mRootLayout.setBackgroundDrawable(bitmapDrawable);
            }

            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
