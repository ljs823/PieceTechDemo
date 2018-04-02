package com.ljs.piecetechdemo;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by ljs on 2018/4/2.
 * Desc:
 */

public class ProApplication extends Application {
    private static ProApplication pro; //全局上下文

    @Override
    public void onCreate() {
        super.onCreate();
        pro = this;
        CrashReport.initCrashReport(getApplicationContext(), "79c8d313c6", false);
    }

    /**
     * @return 全局上下文
     */
    public static ProApplication getAppContext() {
        return pro;
    }

}
