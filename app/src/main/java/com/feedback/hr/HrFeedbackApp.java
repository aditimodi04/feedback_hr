package com.feedback.hr;

import android.app.Application;
import android.content.res.AssetManager;

import java.lang.Thread.UncaughtExceptionHandler;


/**
 * Created by user on 01/12/16.
 */
public class HrFeedbackApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            AssetManager am = getApplicationContext().getAssets();

            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    throwable.printStackTrace();
                    System.exit(1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
