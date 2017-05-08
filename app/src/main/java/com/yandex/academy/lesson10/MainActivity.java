package com.yandex.academy.lesson10;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private View mRootLayout;
    private ImageLoader mImageLoader;
    private WorkerThread mWorkerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageLoader = new ImageLoader();
        mRootLayout = findViewById(R.id.layout);

        mWorkerThread = new WorkerThread("WorkerThread");
        mWorkerThread.start();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWorkerThread.postTask(mWorkerTask);
            }
        });
    }

    private Runnable mWorkerTask = new Runnable() {
        @Override
        public void run() {
            final Handler handler1 = new Handler();
            final Handler handler2 = new Handler();

            final int[] count1 = {0};
            final int[] count2 = {0};
            for (int i = 0; i < 10; i++) {
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TEST", "handler1, i = " + count1[0]++);
                    }
                });

                handler2.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TEST", "handler2, i = " + count2[0]++);
                    }
                });

                /*handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TEST", "handler1, i = " + count1[0]++);
                    }
                }, 100);

                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TEST", "handler2, i = " + count2[0]++);
                    }
                }, 200);*/
            }

            loadImage();
        }
    };

    private void loadImage() {
        final String imageUrl = mImageLoader.getImageUrl();
        if (TextUtils.isEmpty(imageUrl) == false) {
            final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
            final BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mRootLayout.setBackground(bitmapDrawable);
                    } else {
                        mRootLayout.setBackgroundDrawable(bitmapDrawable);
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        mWorkerThread.quit();
        super.onPause();
    }

    private static class WorkerThread extends HandlerThread {

        @Nullable
        private Handler mHandler;

        WorkerThread(final String name) {
            super(name);
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            mHandler = new Handler(getLooper());
        }

        void postTask(@NonNull final Runnable runnable) {
            if (mHandler != null) {
                mHandler.post(runnable);
            }
        }
    }
}
