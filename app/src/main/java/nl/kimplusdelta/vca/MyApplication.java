package nl.kimplusdelta.vca;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import nl.kimplusdelta.vca.models.exam.Exam;
import nl.kimplusdelta.vca.utils.TypefaceUtil;

public class MyApplication extends Application {

    private Tracker mTracker;

    private String mCurrentScreenName;
    private Long mTimeTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        //fb init
        FacebookSdk.sdkInitialize(getApplicationContext());

        // init Exams
        Exam.init(this);

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/SourceSansPro.ttf");
    }

    public void trackScreen(String screenName) {
        this.mCurrentScreenName = screenName;

        getDefaultTracker().setScreenName(screenName);
        getDefaultTracker().send(new HitBuilders.ScreenViewBuilder().build());

        mTimeTracker = System.currentTimeMillis();
    }

    public void trackScreenTime() {
        getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Screen")
                .setAction("Screen Time")
                .setLabel(mCurrentScreenName)
                .setValue((int) ((System.currentTimeMillis() - mTimeTracker) / 1000.0))
                .build());
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
