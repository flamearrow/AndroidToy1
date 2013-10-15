package com.example.spotOn;

import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.example.ccweibo.R;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.SoundPool;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SpotOnView extends View {
	private static final String HIGH_SCORE = "HIGH_SCORE";
	private SharedPreferences preferences; // used to store high score

	private int spotsTouched;
	private int score;
	private int level;
	private int viewWidth;
	private int viewHeight;
	private long animationTime;
	private boolean gameOver;
	private boolean gamePaused;
	private boolean dialogDisplayed;
	private int highScore;

	// collections of spots (ImageViews) and Animators
	private final Queue<ImageView> spots = new ConcurrentLinkedQueue<ImageView>();
	private final Queue<Animator> animators = new ConcurrentLinkedQueue<Animator>();

	private TextView highScoreTextView;
	private TextView currentScoreTextView;
	private TextView levelTextView;
	private LinearLayout livesLinearLayout;
	private RelativeLayout spotOnRelativeLayout;
	private Resources resources;
	private LayoutInflater layoutInflater;

	private static final int INITIAL_ANIMATION_DURATION = 6000;
	private static final Random random = new Random();
	private static final int SPOT_DIAMETER = 100;
	private static final float SCALE_X = 0.25f;
	private static final float SCALE_Y = 0.25f;
	private static final int INITIAL_SPOTS = 5;
	private static final int SPOT_DELAY = 500;
	private static final int LIVES = 3;
	private static final int MAX_LIVES = 7;
	private static final int NEW_LEVEL = 10;
	private Handler spotHandler; // add new spots to the game

	private static final int HIT_SOUND_ID = 1;
	private static final int MISS_SOUND_ID = 2;
	private static final int DISAPPEAR_SOUND_ID = 3;
	private static final int SOUND_PRIORITY = 1;
	private static final int SOUND_QUALITY = 100;
	private static final int MAX_STREAMS = 4;
	private SoundPool soundPool;
	private int volume;
	private Map<Integer, Integer> soundMap;

	public SpotOnView(Context context, SharedPreferences sharedPreferences,
			RelativeLayout parentlayout) {
		super(context);
		preferences = sharedPreferences;
		highScore = preferences.getInt(HIGH_SCORE, 0);
		resources = context.getResources();

		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		spotOnRelativeLayout = parentlayout;
		livesLinearLayout = (LinearLayout) spotOnRelativeLayout
				.findViewById(R.id.lifeLinearLayout);
		highScoreTextView = (TextView) spotOnRelativeLayout
				.findViewById(R.id.highScoreTextView);
		currentScoreTextView = (TextView) spotOnRelativeLayout
				.findViewById(R.id.scoreTextView);
		levelTextView = (TextView) spotOnRelativeLayout
				.findViewById(R.id.levelTextView);
		spotHandler = new Handler();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		viewWidth = w;
		viewHeight = h;
	}

	public void pause() {
		gamePaused = true;
		soundPool.release();
		soundPool = null;
		cancelAnimations();
	}

	public void resume(Context context) {
		gamePaused = false;
		initializeSoundEffects(context);
		if (!dialogDisplayed)
			resetGame();
	}

	private void initializeSoundEffects(Context context) {

	}

	private void resetGame() {

	}

	private void cancelAnimations() {
		for (Animator animator : animators)
			animator.cancel();
		for (ImageView view : spots)
			spotOnRelativeLayout.removeView(view);
		// spotHandler.removeCallbacks(addSpotRunnable);
		animators.clear();
		spots.clear();
	}
}
