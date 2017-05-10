package com.yandex.academy.lesson10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static final String PARAM_RESULT = "MainActivity.result";
    static final String SERVICE_ACTION = "com.yandex.academy.lesson10.MyService2";
    static final String BROADCAST_ACTION = "com.yandex.academy.lesson10.MainActivity.BroadcastReceiver";
    private static final String TAG = "TAG";

    private View mRootLayout;
    private View mProgressBar;
    private MyBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity, onCreate");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRootLayout = findViewById(R.id.layout);
        mProgressBar = findViewById(R.id.progressBar);

        mBroadcastReceiver = new MyBroadcastReceiver();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                startService(new Intent(MainActivity.this, MyIntentService.class));
            }
        });
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.d(TAG, "MainActivity, MyBroadcastReceiver, onReceive");
            final String imageName = intent.getStringExtra(PARAM_RESULT);
            if (TextUtils.isEmpty(imageName) == false) {
                final Bitmap bitmap = ImageSaver.getInstance().loadImage(getApplicationContext(), imageName);
                final Drawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mRootLayout.setBackground(bitmapDrawable);
                } else {
                    mRootLayout.setBackgroundDrawable(bitmapDrawable);
                }
            }

            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity, onResume, registerReceiver");
        registerReceiver(mBroadcastReceiver, new IntentFilter(MainActivity.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "MainActivity, onPause, unregisterReceiver");
        unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }
}
