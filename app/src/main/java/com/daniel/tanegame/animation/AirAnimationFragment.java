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

public class AirAnimationFragment extends AnimationFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        relativeLayout = getActivity().findViewById(R.id.animationPanel);
        View view = getLayoutInflater().inflate(R.layout.animation_fragment, relativeLayout, false);
        if (container != null) {
            animationView = view.findViewById(R.id.animationView);
            animationView.setBackgroundResource(R.drawable.cloud_normal);
            int layoutWidth = relativeLayout.getMeasuredWidth();
            int layoutHeight = relativeLayout.getMeasuredHeight();
            int viewWidth = layoutWidth / 4;
            int viewHeight = layoutHeight / 4;
            RelativeLayout.LayoutParams animationViewSize
                    = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
            animationView.setLayoutParams(animationViewSize);

            float elementX = (layoutWidth / 2) - (viewWidth / 2);
            animationView.setX(elementX);
            animationView.setY(viewHeight);

            animationView.setVisibility(View.VISIBLE);

            animationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (animationStatus == FINISHED || animationStatus == FIRST_TOUCH_DONE) {
                        playAnimation();
                    } else if (animationStatus == SECOND_TOUCH_DONE) {
                        animationStatus = THIRD_TOUCH_DONE;
                        stopPulse();
                        doEndAnimation();
                    } else if (animationStatus == THIRD_TOUCH_DONE && getActivity() != null && fragment != null) {
                        getActivity().getFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                }
            });
            animationStatus = INITIALIZED;
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setAirButton();
        }
    }

    @Override
    protected void playAnimation() {
        animationView.setClickable(false);
        switch (animationStatus) {
            case INITIALIZED:
                animationStatus = FINISHED;
                animationView.setClickable(true);
                letItPulse();
                break;
            case FINISHED:
                animationView.setImageDrawable(null);
                animationView.setBackgroundResource(R.drawable.cloud_animation);
                ((AnimationDrawable) animationView.getBackground()).start();

                animation = AnimationUtils.loadAnimation(getContext(), R.anim.cloud_scaling);
                break;
            case FIRST_TOUCH_DONE:
                animationView.setImageDrawable(null);
                animationView.setBackgroundResource(R.drawable.cloud_animation);
                ((AnimationDrawable) animationView.getBackground()).start();

                animation = AnimationUtils.loadAnimation(getContext(), R.anim.cloud_scaling);
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
                            case FINISHED:
                                animationView.setBackgroundResource(R.drawable.cloud_normal);
                                animationStatus = FIRST_TOUCH_DONE;
                                letItPulse();
                                break;
                            case FIRST_TOUCH_DONE:
                                letItPulse();
                                animationView.setBackgroundResource(R.drawable.cloud_normal);
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
        animationView.setBackgroundResource(R.drawable.phoenix_animation);

        int layoutWidth = relativeLayout.getMeasuredWidth();
        int layoutHeight = relativeLayout.getMeasuredHeight();
        int viewSize = layoutHeight / 2;
        animationView.setX((layoutWidth / 2) - (viewSize / 2));
        animationView.setY((layoutHeight / 2) - (viewSize / 2));

        RelativeLayout.LayoutParams layoutParams
                = new RelativeLayout.LayoutParams(viewSize, viewSize);
        animationView.setLayoutParams(layoutParams);

        AnimationDrawable animationDrawable = new CustomAnimationDrawable((AnimationDrawable) getResources()
                .getDrawable(R.drawable.phoenix_animation, null)) {
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
    protected void showPuff() {
        Runnable puffAnimation = new Runnable() {
            @Override
            public void run() {

                puffImage = new ImageView(getContext());
                puffImage.setImageResource(R.drawable.puff);
                RelativeLayout.LayoutParams layoutParams
                        = new RelativeLayout.LayoutParams(animationView.getMeasuredWidth() * 2, animationView.getMeasuredWidth() * 2);
                puffImage.setLayoutParams(layoutParams);
                relativeLayout.addView(puffImage);
                puffImage.setX(0);
                puffImage.setY(0);

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
                } catch (Exception ignored) {
                }
            }
        };
        Utils.runAnimationOnUiThread(getActivity(), puffAnimation);
    }

    @Override
    protected void playAnimalSound() {
        playAnimalSound(R.raw.phoenix_sound);
    }
}
