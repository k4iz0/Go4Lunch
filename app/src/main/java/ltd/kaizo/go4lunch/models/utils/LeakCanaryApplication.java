package ltd.kaizo.go4lunch.models.utils;


import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * The type Leak canary application.
 */
public class LeakCanaryApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

//        uncomment for debug purpose
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
    }

}