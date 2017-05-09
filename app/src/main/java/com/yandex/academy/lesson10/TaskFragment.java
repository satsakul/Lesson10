package com.yandex.academy.lesson10;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class TaskFragment extends Fragment {

    private AsyncTask<Void, Void, Drawable> mAsyncTask;
    private MyAsyncTask.AsyncTaskListener mAsyncTaskListener;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Retain this fragment across configuration changes.
         */
        setRetainInstance(true);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        if (mAsyncTask == null) {
            mAsyncTask = new MyAsyncTask(getContext());
            ((MyAsyncTask) mAsyncTask).setAsyncTaskListener((MyAsyncTask.AsyncTaskListener) getActivity());
            mAsyncTask.execute();
        }
    }

    @Override
    public void onDetach() {
        if (mAsyncTask != null) {
            ((MyAsyncTask) mAsyncTask).setAsyncTaskListener(null);
        }

        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (mAsyncTask != null) {
            mAsyncTask.cancel(false);
        }
        super.onDestroy();
    }
}
