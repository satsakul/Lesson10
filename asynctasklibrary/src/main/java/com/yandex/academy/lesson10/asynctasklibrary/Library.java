package com.yandex.academy.lesson10.asynctasklibrary;

import android.os.AsyncTask;
import android.util.Log;

public class Library {
    public static void startAsyncTask() {
        Log.d("TAG", "Library, start LibraryAsyncTask");
        new LibraryAsyncTask().execute();
    }

    private static class LibraryAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(final Void... params) {
            for (int i = 0; i < 10; i++) {
                Log.d("TAG", "LibraryAsyncTask, i = " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
