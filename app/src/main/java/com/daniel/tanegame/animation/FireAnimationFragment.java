package com.daniel.tanegame.animation;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daniel.tanegame.CustomAnimationDrawable;
import com.daniel.tanegame.MainActivity;
import com.daniel.tanegame.R;
import com.daniel.tanegame.Utils;

import java.util.Timer;
import java.util.TimerTask;

import static com.daniel.tanegame.animation.AnimationStatus.FINISHED;
import static com.daniel.tanegame.animation.AnimationStatus.FIRST_TOUCH_DONE;
import static com.daniel.tanegame.animation.AnimationStatus.INITIALIZED;
import static com.daniel.tanegame.animation.AnimationStatus.SECOND_TOUCH_DONE;
import static com.daniel.tanegame.animation.AnimationStatus.THIRD_TOUCH_DONE;

public class FireAnimationFragment extends AnimationFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        relativeLayout = getActivity().findViewById(R.id.animationPanel);
        View view = getLayoutInflater().inflate(R.layout.animation_fragment, relativeLayout, false);
        if (container != null) {
            animationView = view.findViewById(R.id.animationView);
            animationView.setImageResource(R.drawable.sun_evil);
            int layoutWidth = relativeLayout.getMeasuredWidth();
            int layoutHeight = relativeLayout.getMeasuredHeight();
            int viewWidth = (int) (layoutWidth / 4 * 1.5);
            int viewHeight = (int) (layoutHeight / 4 * 1.5);
            RelativeLayout.LayoutParams animationViewSize
                    = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
            animationView.setLayoutParams(animationViewSize);


            float elementX = (layoutWidth / 2) - (viewWidth / 2);
            float elementY = (layoutHeight / 2) - (viewHeight / 2);
            animationView.setX(elementX);
            animationView.setY(elementY);

            animationView.setVisibility(View.VISIBLE);

            animationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (animationStatus == FINISHED || animationStatus == FIRST_TOUCH_DONE) {
                        playAnimation();
                    } else if (animationStatus == SECOND_TOUCH_DONE) {
                        stopPulse();
                        doEndAnimation();
                        animationStatus = THIRD_TOUCH_DONE;
                    }else if (animationStatus == THIRD_TOUCH_DONE && getActivity() != null && fragment != null) {
                        getActivity().getFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                }
            });
            animationStatus = INITIALIZED;
        }
        return view;
    }

    @Override
    protected void playAnimation() {
        animationView.setClickable(false);
        switch (animationStatus) {
            case INITIALIZED:
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.sun_rising);
                break;
            case FINISHED:
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.sun_rotating);
                break;
            case FIRST_TOUCH_DONE:
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.sun_rotating);
                break;
        }
        if (animation != null) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (getContext() != null) {
                        animationView.setClickable(true);
                        switch (animationStatus) {
                            case INITIALIZED:
                                letItPulse();
                                animationStatus = FINISHED;
                                break;
                            case FINISHED:
                                letItPulse();
                                animationView.setImageResource(R.drawable.sun_neutral);
                                animationStatus = FIRST_TOUCH_DONE;
                                break;
                            case FIRST_TOUCH_DONE:
                                letItPulse();
                                animationView.setImageResource(R.drawable.sun_happy);
                                animationStatus = SECOND_TOUCH_DONE;
                                break;
                        }
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            Utils.runAnimationOnUiThread(getActivity(), animationView, animation);
        }
    }

    @Override
    protected void doEndAnimation() {
        animationView.setImageDrawable(null);
        animationView.setBackgroundResource(R.drawable.neptun_animation);

        int layoutWidth = relativeLayout.getMeasuredWidth();
        int layoutHeight = relativeLayout.getMeasuredHeight();
        int viewWidth = layoutWidth / 2;
        int viewHeight = (int) (layoutHeight / 4 * 1.5);
        RelativeLayout.LayoutParams animationViewSize
                = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
        animationView.setLayoutParams(animationViewSize);

        animationView.setX((layoutWidth / 2) - (viewWidth / 2));
        animationView.setY(layoutHeight - viewHeight);

        AnimationDrawable animationDrawable = new CustomAnimationDrawable((AnimationDrawable) getResources()
                .getDrawable(R.drawable.neptun_animation, null)) {
            @Override
            public void onAnimationFinish() {

            }

            @Override
            public void onAnimationStart() {
                showPuff();
            }
        };
        animationView.setBackground(animationDrawable);
        animationDrawable.start();

        setTimer();

        playAnimalSound();
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setFireButton();
        }
    }

    @Override
    protected void showPuff() {
        Runnable puffAnimation = new Runnable() {
            @Override
            public void run() {
                puffImage = new ImageView(getContext());
                puffImage.setImageResource(R.drawable.puff);
                int viewWidth = (int) (animationView.getMeasuredWidth() * 1.5);
                int viewHeight = (int) (animationView.getMeasuredHeight() * 1.5);
                RelativeLayout.LayoutParams layoutParams
                        = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
                puffImage.setLayoutParams(layoutParams);
                relativeLayout.addView(puffImage);
                puffImage.setX((relativeLayout.getMeasuredWidth() / 2) - (viewWidth / 2));
                puffImage.setY(relativeLayout.getMeasuredHeight() / 2);

                try {
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (getActivity()!=null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        relativeLayout.removeView(puffImage);
                                    }
                                });
                            }
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(timerTask, 500);
                } catch (Exception ignored) {}
            }
        };
        Utils.runAnimationOnUiThread(getActivity(), puffAnimation);
    }

    @Override
    protected void playAnimalSound() {
        playAnimalSound(R.raw.neptun_sound);
    }
}
