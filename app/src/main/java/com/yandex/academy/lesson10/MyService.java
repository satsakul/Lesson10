package com.yandex.academy.lesson10;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyService extends Service {

    private final ExecutorService mExecutorService;

    public MyService() {
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

        load(startId);
        return super.onStartCommand(intent, flags, startId);
    }

    private void load(final int startId) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Log.i("TAG", "i = " + i + ", Service = " + this.hashCode());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                final Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                sendBroadcast(intent);

                stopSelf(startId);
            }
        });
    }
}
