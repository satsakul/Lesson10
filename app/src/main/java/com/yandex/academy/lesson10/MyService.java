package com.yandex.academy.lesson10;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.text.TextUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyService extends Service {

    private final ImageLoader mImageLoader;
    private final ExecutorService mExecutorService;

    private boolean mStarted;

    public MyService() {
        mImageLoader = new ImageLoader();
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(final Intent intent,
                              final int flags,
                              final int startId) {

        load();
        return super.onStartCommand(intent, flags, startId);
    }

    private void load() {
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
                            ImageSaver.getInstance().saveImage(getApplicationContext(), bitmap, imageName);

                            final Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                            intent.putExtra(MainActivity.PARAM_RESULT, imageName);
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
        mStarted = false;
        super.onDestroy();
    }
}
