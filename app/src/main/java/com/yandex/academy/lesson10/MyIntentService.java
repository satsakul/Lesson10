package com.yandex.academy.lesson10;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

public class MyIntentService extends IntentService {

    private static final String TAG = "TAG";

    private final ImageLoader mImageLoader;

    public MyIntentService() {
        super("MyIntentService");
        mImageLoader = new ImageLoader();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyIntentService, onCreate");
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        for (int i = 0; i < 10; i++) {
            Log.d(TAG, "MyIntentService, onHandleIntent, i = " + i);
            final String imageUrl = mImageLoader.getImageUrl();
            if (TextUtils.isEmpty(imageUrl) == false) {
                final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
                final String imageName = "myImage.png";
                ImageSaver.getInstance().saveImage(getApplicationContext(), bitmap, imageName);

                final Intent broadcastIntent = new Intent(MainActivity.BROADCAST_ACTION);
                broadcastIntent.putExtra(MainActivity.PARAM_RESULT, imageName);
                sendBroadcast(broadcastIntent);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "MyIntentService, onDestroy");
        super.onDestroy();
    }
}
