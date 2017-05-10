package com.yandex.academy.lesson10;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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
    static final String TAG = "TAG";

    private View mRootLayout;
    private View mProgressBar;

    private MyService mMyService;
    private ServiceConnection mServiceConnection;
    private MyBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRootLayout = findViewById(R.id.layout);
        mProgressBar = findViewById(R.id.progressBar);

        mServiceConnection = new MyServiceConnection();
        mBroadcastReceiver = new MyBroadcastReceiver();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);

                if (mMyService != null) {
                    mMyService.loadImages();
                }
            }
        });
    }

    private void setBackground(final String imageName) {
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

    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            Log.d(TAG, "MainActivity onServiceConnected");
            mMyService = ((MyService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            Log.d(TAG, "MainActivity onServiceDisconnected");
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.d(TAG, "MyBroadcastReceiver, onReceive");
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
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "MainActivity, onStart, bindService");

        final Intent intent = new Intent(MainActivity.this, MyService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "MainActivity, onPause, unregisterReceiver");
        unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "MainActivity, onStop, unbindService");
        unbindService(mServiceConnection);
        super.onStop();
    }
}
