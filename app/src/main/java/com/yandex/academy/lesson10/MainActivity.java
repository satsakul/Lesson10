package com.yandex.academy.lesson10;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MyAsyncTask.AsyncTaskListener {

    private static final String TASK_FRAGMENT_TAG = "MainActivity.TaskFragment";

    private View mRootLayout;
    private View mProgressBar;
    private TaskFragment mTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRootLayout = findViewById(R.id.layout);
        mProgressBar = findViewById(R.id.progressBar);

        if (savedInstanceState == null) {
            mTaskFragment = new TaskFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(mTaskFragment, TASK_FRAGMENT_TAG).commit();
        } else {
            mTaskFragment = (TaskFragment) getSupportFragmentManager().findFragmentByTag(TASK_FRAGMENT_TAG);
        }
    }


    @Override
    public void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(final Drawable bitmapDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRootLayout.setBackground(bitmapDrawable);
        } else {
            mRootLayout.setBackgroundDrawable(bitmapDrawable);
        }

        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
