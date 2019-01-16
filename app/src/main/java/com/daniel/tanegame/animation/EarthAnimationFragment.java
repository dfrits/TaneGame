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

import static com.daniel.tanegame.animation.AnimationStatus.*;

public class EarthAnimationFragment extends AnimationFragment {

    public EarthAnimationFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle args) {
        relativeLayout = getActivity().findViewById(R.id.animationPanel);
        View view = getLayoutInflater().inflate(R.layout.animation_fragment, relativeLayout, false);
        if (container != null) {
            animationView = view.findViewById(R.id.animationView);
            animationView.setImageResource(R.drawable.stone);
            int layoutWidth = relativeLayout.getMeasuredWidth();
            int layoutHeight = relativeLayout.getMeasuredHeight();
            int viewWidth = layoutWidth / 4;
            int viewHeight = layoutHeight / 4;
            RelativeLayout.LayoutParams animationViewSize
                    = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
            animationView.setLayoutParams(animationViewSize);


            float elementX = (layoutWidth / 2) - (viewWidth / 2);
            float elementY = layoutHeight - viewHeight;
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
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.stone_falling);
                break;
            case FINISHED:
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.stone_scaling);
                break;
            case FIRST_TOUCH_DONE:
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.stone_scaling);
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
                                animationStatus = FIRST_TOUCH_DONE;
                                break;
                            case FIRST_TOUCH_DONE:
                                letItPulse();
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
        int layoutWidth = relativeLayout.getMeasuredWidth();
        int layoutHeight = relativeLayout.getMeasuredHeight();
        int viewWidth = (int) (layoutWidth / 4 * 1.8);
        int viewHeight = (int) (layoutHeight / 4 * 1.5);
        RelativeLayout.LayoutParams animationViewSize
                = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
        animationView.setLayoutParams(animationViewSize);
        animationView.setX((layoutWidth / 2) - (viewWidth / 2));
        animationView.setY((layoutHeight / 2) - (viewHeight / 2));
        animationView.setBackgroundResource(R.drawable.pegasus_animation);

        AnimationDrawable animationDrawable = new CustomAnimationDrawable((AnimationDrawable) getResources()
                .getDrawable(R.drawable.pegasus_animation, null)) {
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
            mainActivity.setEarthButton();
        }
    }

    @Override
    protected void showPuff() {
        Runnable puffAnimation = new Runnable() {
            @Override
            public void run() {
                puffImage = new ImageView(getContext());
                puffImage.setImageResource(R.drawable.puff);
                int dimen = animationView.getMeasuredWidth() * 2;
                RelativeLayout.LayoutParams layoutParams
                        = new RelativeLayout.LayoutParams(dimen, dimen);
                puffImage.setLayoutParams(layoutParams);
                relativeLayout.addView(puffImage);
                puffImage.setX((animationView.getX() + (animationView.getMeasuredWidth() / 2)) - (dimen / 2));
                puffImage.setY((animationView.getY() + (animationView.getMeasuredHeight() / 2)) - (dimen / 2));

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
        playAnimalSound(R.raw.pegasus_sound);
    }
}
