package com.kms.appcore.activity;

import android.app.Activity;
import android.os.Bundle;

import com.kms.appcore.log.Log;

/**
 * @author ziv
 */
public class CoreActivity extends Activity {

    private static final String TAG = "CoreActivity";
    protected boolean mResumed;
    protected boolean mCreated;
    protected boolean mStopped;
    protected boolean mIsDestroyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.debug(TAG, getClass().getName() + ":: onCreate");
        ActivityManager.getInstance().setActivityAttribute(this);
        ActivityManager.getInstance().addManagedActivity(this);
        mCreated = true;
        mIsDestroyed = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.debug(TAG, getClass().getName() + ":: onStart");
        mStopped = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.debug(TAG, getClass().getName() + ":: onResume");
        mResumed = true;
        ActivityManager.getInstance().setIsForeGround(true);
        ActivityManager.getInstance().setCurrActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.debug(TAG, getClass().getName() + ":: onPause");
        mResumed = false;
        ActivityManager.getInstance().setIsForeGround(false);
        // 及时置空，避免内存溢出
        ActivityManager.getInstance().setCurrActivity(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.debug(TAG, getClass().getName() + ":: onStop");
        mStopped = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.debug(TAG, getClass().getName() + ":: onDestroy");
        mIsDestroyed = true;
        ActivityManager.getInstance().removeManagedActivity(this);
    }

}
