package com.scott.honerv8loadingview.view;

import android.animation.TimeInterpolator;
import android.view.animation.BaseInterpolator;
import android.view.animation.Interpolator;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/5/10.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class InterpolatorDemo implements TimeInterpolator {

    @Override
    public float getInterpolation(float input) {
        return 0;
    }
}
