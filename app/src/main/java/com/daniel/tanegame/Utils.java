package com.daniel.tanegame;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

public abstract class Utils {

    public static void runAnimationOnUiThread(final Activity activity, final View view,
                                              final Animation animation) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.startAnimation(animation);
            }
        });
    }

    public static void runAnimationOnUiThread(final Activity activity, final Runnable animation) {
        activity.runOnUiThread(animation);
    }
}
