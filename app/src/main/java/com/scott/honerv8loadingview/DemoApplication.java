package com.scott.honerv8loadingview;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/6/1.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        // ...
        // Do it on main process
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }
}