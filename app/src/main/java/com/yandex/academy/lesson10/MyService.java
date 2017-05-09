package com.yandex.academy.lesson10;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = "TAG";

    private final ImageLoader mImageLoader;

    public MyService() {
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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(final Intent intent,
                              final int flags,
                              final int startId) {
        Log.d(TAG, "MyService, onStartCommand, loadImage");
        load(startId);

        return super.onStartCommand(intent, flags, startId);
    }

    private void load(final int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String imageUrl = mImageLoader.getImageUrl();
                if (TextUtils.isEmpty(imageUrl) == false) {
                    final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
                    final String imageName = "myImage.png";
                    ImageSaver.getInstance().saveImage(getApplicationContext(), bitmap, imageName);

                    final Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                    intent.putExtra(MainActivity.PARAM_RESULT, imageName);
                    sendBroadcast(intent);
                }
            }
        }).start();

        stopSelf(startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "MyService, onDestroy");
        super.onDestroy();
    }
}
