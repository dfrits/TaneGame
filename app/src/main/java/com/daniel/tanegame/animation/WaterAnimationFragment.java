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

public class WaterAnimationFragment extends AnimationFragment {

    public WaterAnimationFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle args) {
        relativeLayout = getActivity().findViewById(R.id.animationPanel);
        View view = getLayoutInflater().inflate(R.layout.animation_fragment, relativeLayout, false);
        if (container != null) {
            animationView = view.findViewById(R.id.animationView);
            animationView.setImageResource(R.drawable.drop);
            int layoutWidth = relativeLayout.getMeasuredWidth();
            int layoutHeight = relativeLayout.getMeasuredHeight();
            int viewWidth = layoutWidth / 20;
            int viewHeight = (layoutHeight / 20) / 2;
            RelativeLayout.LayoutParams animationViewSize
                    = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
            animationView.setLayoutParams(animationViewSize);

            float elementX = (layoutWidth / 2) - (viewWidth / 2);
            animationView.setX(elementX);
            animationView.setY(layoutHeight);

            animationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (animationStatus == FINISHED || animationStatus == FIRST_TOUCH_DONE) {
                        playASeriesA1();
                    } else if (animationStatus == SECOND_TOUCH_DONE) {
                        stopPulse();
                        doEndAnimation();
                        animationStatus = THIRD_TOUCH_DONE;
                    }else if (animationStatus == THIRD_TOUCH_DONE && getActivity() != null && fragment != null) {
                        getActivity().getFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                }
            });

            animationView.setVisibility(View.VISIBLE);
            animationStatus = INITIALIZED;
        }
        return view;
    }

    @Override
    protected void playAnimation() {
        animationView.setClickable(false);
        animation = null;

        switch (animationStatus) {
            case INITIALIZED:
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.drop_falling);
                break;
            case FINISHED:
                animationView.setClickable(true);
                letItPulse();
                animationStatus = FIRST_TOUCH_DONE;
                break;
            case FIRST_TOUCH_DONE:
                animationView.setClickable(true);
                letItPulse();
                animationStatus = SECOND_TOUCH_DONE;
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
                                playSecondStartAnimation();
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

    private void playSecondStartAnimation() {
        animationView.setImageResource(R.drawable.puddle);
        int layoutWidth = relativeLayout.getMeasuredWidth();
        int layoutHeight = relativeLayout.getMeasuredHeight();
        int viewWidth = layoutWidth / 4;
        int viewHeight = (layoutHeight / 4) / 2;
        RelativeLayout.LayoutParams animationViewSize
                = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
        animationView.setLayoutParams(animationViewSize);

        float elementX = (layoutWidth / 2) - (viewWidth / 2);
        float elementY = layoutHeight - viewHeight;
        animationView.setX(elementX);
        animationView.setY(elementY);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.puddle_scaling);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationStatus = FINISHED;
                letItPulse();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Utils.runAnimationOnUiThread(getActivity(), animationView, animation);
    }

    private void playASeriesA1() {
        animationView.setClickable(false);
        stopPulse();
        animationView.setBackground(null);
        animationView.setImageResource(R.drawable.puddle);

        final ImageView drop = new ImageView(getContext());
        drop.setImageResource(R.drawable.drop);
        int dropWidth = relativeLayout.getMeasuredWidth() / 20;
        int dropHeight = (relativeLayout.getMeasuredHeight() / 20) / 2;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dropWidth, dropHeight);
        drop.setLayoutParams(layoutParams);
        relativeLayout.addView(drop);
        /*drop.setX((relativeLayout.getMeasuredWidth() / 2) - (dropWidth / 2));
        drop.setY(-dropHeight);*/

        playASeriesA2(drop);

        /*animation = AnimationUtils.loadAnimation(getContext(), R.anim.drop_rising);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (getContext() != null) {
                    playASeriesA2(drop);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Utils.runAnimationOnUiThread(getActivity(), drop, animation);*/
    }

    private void playASeriesA2(final ImageView drop) {
        float dropX = (relativeLayout.getMeasuredWidth() / 2) - (drop.getMeasuredWidth() / 2);
        float dropY = relativeLayout.getMeasuredHeight() - (animationView.getMeasuredHeight() / 2)
                - (drop.getMeasuredHeight() / 2);
        drop.setX(dropX);
        drop.setY(dropY);

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.drop_falling_2);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (getContext() != null) {
                    playASeriesA3(drop);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Utils.runAnimationOnUiThread(getActivity(), drop, animation);
    }

    private void playASeriesA3(ImageView drop) {
        relativeLayout.removeView(drop);

        animationView.setImageDrawable(null);

        final CustomAnimationDrawable animationDrawable = new CustomAnimationDrawable((AnimationDrawable) getResources()
                .getDrawable(R.drawable.puddle_inner_to_outer, null)) {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationFinish() {
                if (getContext() != null) {
                    animationView.setBackground(null);
                    animationView.setImageResource(R.drawable.puddle);
                    playAnimation();
                }
            }
        };

        animationView.setBackground(animationDrawable);
        animationDrawable.start();
    }

    @Override
    protected void doEndAnimation() {
        animationView.setBackgroundResource(R.drawable.mino_animation);
        animationView.setImageDrawable(null);

        int layoutWidth = relativeLayout.getMeasuredWidth();
        int layoutHeight = relativeLayout.getMeasuredHeight();
        int viewWidth = (int) (layoutWidth / 4 * 1.5);
        int viewHeight = layoutHeight / 2;
        RelativeLayout.LayoutParams animationViewSize
                = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
        animationView.setLayoutParams(animationViewSize);

        animationView.setX((layoutWidth / 2) - (viewWidth / 2));
        animationView.setY(layoutHeight - viewHeight);

        AnimationDrawable animationDrawable = new CustomAnimationDrawable((AnimationDrawable) getResources()
                .getDrawable(R.drawable.mino_animation, null)) {
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
            mainActivity.setWaterButton();
        }
    }

    @Override
    protected void showPuff() {
        Runnable puffAnimation = new Runnable() {
            @Override
            public void run() {
                puffImage = new ImageView(getContext());
                puffImage.setImageResource(R.drawable.puff);
                int viewWidth = relativeLayout.getMeasuredWidth();
                int viewHeight = relativeLayout.getMeasuredHeight()/2;
                RelativeLayout.LayoutParams layoutParams
                        = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
                puffImage.setLayoutParams(layoutParams);
                relativeLayout.addView(puffImage);
                puffImage.setX(0);
                puffImage.setY(relativeLayout.getMeasuredHeight()/2);

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
        playAnimalSound(R.raw.mino_sound);
    }
}
