package com.yandex.academy.lesson10;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private View mRootLayout;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageLoader = new ImageLoader();
        mRootLayout = findViewById(R.id.layout);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImage();
            }
        });
    }

    private void loadImage() {
        final Clazz clazz = new Clazz();

        new Thread(new Runnable() {
            public void run() {
                clazz.foo();
            }
        }).start();

        clazz.bar();

        /*final String imageUrl = mImageLoader.getImageUrl();
        if (TextUtils.isEmpty(imageUrl) == false) {
            final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
            final BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mRootLayout.setBackground(bitmapDrawable);
            } else {
                mRootLayout.setBackgroundDrawable(bitmapDrawable);
            }
        }*/
    }

    private static class Clazz {

        private final Object mLock1 = new Object();
        private final Object mLock2 = new Object();

        void foo() {
            synchronized (mLock1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (mLock2) {
                    Log.d("TEST", "foo");
                }
            }
        }

        void bar() {
            synchronized (mLock2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (mLock1) {
                    Log.d("TEST", "bar");
                }
            }
        }
    }
}
