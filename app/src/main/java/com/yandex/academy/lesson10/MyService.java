package com.yandex.academy.lesson10;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = "TAG";

    private final MyBinder mBinder;
    private final ImageLoader mImageLoader;

    private boolean mStarted;
    private String mImageName;

    public MyService() {
        mBinder = new MyBinder();
        mImageLoader = new ImageLoader();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyService, onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyService, onBind");
        return mBinder;
    }

    void loadImages() {
        Log.d(TAG, "MyService, onStartCommand, loadImages");
        if (mStarted == false) {
            mStarted = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mStarted) {
                        final String imageUrl = mImageLoader.getImageUrl();
                        if (TextUtils.isEmpty(imageUrl) == false) {
                            final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
                            final String imageName = "myImage.png";
                            ImageSaver.getInstance().saveImage(getApplicationContext(), bitmap, mImageName);

                            mImageName = imageName;

                            final Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                            intent.putExtra(MainActivity.PARAM_RESULT, mImageName);
                            sendBroadcast(intent);
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "MyService, onDestroy");
        mStarted = false;
        super.onDestroy();
    }

    class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }
}
