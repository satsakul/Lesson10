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
import android.view.View;

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

        mAsyncTask = new MyAsyncTask();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAsyncTask.execute();
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

    private class MyAsyncTask extends AsyncTask<Void, Void, Drawable> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Drawable doInBackground(final Void... params) {
            final Drawable drawable = loadImage();
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
