package com.daniel.tanegame.animation;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daniel.tanegame.MainActivity;
import com.daniel.tanegame.R;
import com.daniel.tanegame.Utils;

import java.util.Timer;
import java.util.TimerTask;

import static com.daniel.tanegame.animation.AnimationStatus.INITIALIZED;
import static com.daniel.tanegame.animation.AnimationStatus.THIRD_TOUCH_DONE;

public abstract class AnimationFragment extends Fragment {
    protected AnimationStatus animationStatus;
    protected ImageView puffImage;
    protected RelativeLayout relativeLayout;
    protected ImageView animationView;
    protected Animation animation;
    private MediaPlayer player;
    private Timer timer;
    protected AnimationFragment fragment = this;

    @Nullable
    @Override
    public abstract View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                      Bundle savedInstanceState);

    @Override
    public void onStart() {
        super.onStart();
        if (animationStatus == INITIALIZED) {
            playAnimation();
        }
        if (animationStatus == THIRD_TOUCH_DONE && timer != null) {
            timer.cancel();
            setTimer();
        }
    }

    @Override
    public void onPause() {
        if (player != null) {
            player.release();
        }
        if (timer != null) {
            timer.cancel();
        } else if (getActivity() != null && fragment != null) {
            getActivity().getFragmentManager().beginTransaction().remove(fragment).commit();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.resetButtons();
        }
        if (relativeLayout != null && puffImage != null) {
            relativeLayout.removeView(puffImage);
        }
        super.onPause();
    }

    protected void setTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().getFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        };
        timer.schedule(timerTask, 5000);
    }

    protected abstract void playAnimation();

    protected abstract void doEndAnimation();

    protected abstract void showPuff();

    protected abstract void playAnimalSound();

    protected void playAnimalSound(int soundResource) {
        player = MediaPlayer.create(getContext(), soundResource);
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });
    }

    protected void letItPulse() {
        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.tap_hint);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animationStatus != THIRD_TOUCH_DONE) {
                    Utils.runAnimationOnUiThread(getActivity(), animationView, animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Utils.runAnimationOnUiThread(getActivity(), animationView, animation);
    }

    protected void stopPulse() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.stop_pulse);
        Utils.runAnimationOnUiThread(getActivity(), animationView, animation);
    }
}
