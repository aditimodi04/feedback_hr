package com.feedback.hr;

import android.app.Application;

import com.androidquery.util.AQUtility;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.tune.Tune;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.lang.Thread.UncaughtExceptionHandler;

import io.fabric.sdk.android.Fabric;


/**
 * Created by user on 01/12/16.
 */

@ReportsCrashes(formKey = "", mailTo = "aditi@uxarmy.com",
        customReportContent = {ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION,
                ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE},
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.app_name)
public class HrFeedbackApp extends Application {

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        try {

          /*  CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/IndieFlower.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );*/

            FacebookSdk.sdkInitialize(getApplicationContext());

            // When dry run is set, hits will not be dispatched, but will still be logged as
            // though they were dispatched.


//            GoogleAnalytics.getInstance(this).setDryRun(true);

            if (BuildConfig.DEBUG) {

            } else {
                AppEventsLogger.activateApp(this);
            }
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);

            // Initialize TMC
            Tune.init(this, "198204", "81aa105b78c520b5ef214f299bbb20d5");

            ACRA.init(this);

            AQUtility.debug(true);
            try {
                Fabric.with(this, new Crashlytics());
            } catch (Exception e) {
                e.printStackTrace();
            }
//            TypefaceUtils.setDefaultFont(getApplicationContext(), "SERIF", "fonts/IndieFlower.ttf");
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


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shapp_tracker.xmlell setprop log.tag.GAv4 DEBUG
            // analytics.enableAutoActivityReports(this);
            mTracker = analytics.newTracker(R.xml.app_tracker);
            // mTracker.enableExceptionReporting(true);
        }
        return mTracker;
    }
}
