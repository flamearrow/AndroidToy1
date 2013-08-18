package com.example.canonGame;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.example.ccweibo.R;

public class CannonView extends SurfaceView implements Callback {

	// private CannonThread cannonThread;
	private Activity activity;

	private boolean dialogIsDisplayed = false;

	public static final int TARGET_PIECES = 7;
	// miss one hit, deduct 2 seconds
	public static final int MISS_PENALTY = 2;
	// get one hit, add 3 seconds
	public static final int HIT_REWARD = 3;

	private boolean gameOver;
	private double timeLeft;
	private int shotsFired;
	private double totalElapsedTime;

	private Line blocker;
	private int blockerDistance;
	private int blockerBeginning;
	private int blockerEnd;
	private int initialBlockerVelocity;

	private float blockerVelocity;

	private Line target;
	private int targetDistance;
	private int targetBeginning;
	private double pieceLength;
	private int targetEnd;
	private int initialTargetVelocity;

	private float targetVelocity;
	private int lineWidth;
	private boolean[] hitStates;
	private int targetPiecesHit;

	private Point cannonball;
	private int cannonballVelocityX;
	private int cannonballVelocityY;
	private boolean cannonballOnScreen;
	private int cannonballRadius;
	private int cannonballSpeed;
	private int cannonBaseRadius;
	private int cannonLength;
	private Point barrelEnd;
	private int screenWidth;
	private int screenHeight;

	private static final int TARGET_SOUND_ID = 0;
	private static final int CANNON_SOUND_ID = 1;
	private static final int BLOCKER_SOUND_ID = 2;
	private SoundPool soundPool;
	private Map<Integer, Integer> soundMap;

	// Paint is used to draw stuff
	private Paint textPaint;
	private Paint cannonballPaint;
	private Paint cannonPaint;
	private Paint blockerPaint;
	private Paint targetPaint;
	private Paint backgroundPaint;

	public CannonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (Activity) context;

		// wtf?
		getHolder().addCallback(this);

		blocker = new Line();
		target = new Line();
		cannonball = new Point();

		hitStates = new boolean[TARGET_PIECES];
		// used to play three sound effects
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

		soundMap = new HashMap<Integer, Integer>();
		soundMap.put(TARGET_SOUND_ID,
				soundPool.load(context, R.raw.target_hit, 1));
		soundMap.put(CANNON_SOUND_ID,
				soundPool.load(context, R.raw.cannon_fire, 1));
		soundMap.put(BLOCKER_SOUND_ID,
				soundPool.load(context, R.raw.blocker_hit, 1));

		textPaint = new Paint();
		cannonPaint = new Paint();
		cannonballPaint = new Paint();
		blockerPaint = new Paint();
		targetPaint = new Paint();
		backgroundPaint = new Paint();
	}

	// call when size changed - including when it's first added to the view
	// hierarchy
	// override this method to initialize parameters of components
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// buffer the size of screen
		screenWidth = w;
		screenHeight = h;
		cannonBaseRadius = h / 18;
		cannonLength = w / 8;
		cannonballRadius = w / 36;
		cannonballSpeed = w * 3 / 2;

		lineWidth = w / 24;
		blockerDistance = w * 5 / 8;
		blockerBeginning = h / 8;
		blockerEnd = h * 3 / 8;
		initialBlockerVelocity = h / 2;
		blocker.start = new Point(blockerDistance, blockerBeginning);
		blocker.end = new Point(blockerDistance, blockerEnd);

		targetDistance = 2 * 7 / 8;
		targetBeginning = h / 8;
		targetEnd = h * 7 / 8;
		pieceLength = (targetEnd - targetBeginning) / TARGET_PIECES;

		initialTargetVelocity = -h / 4;
		target.start = new Point(targetDistance, targetBeginning);
		target.end = new Point(targetDistance, targetEnd);

		barrelEnd = new Point(cannonLength, h / 2);
		textPaint.setTextSize(w / 20);
		textPaint.setAntiAlias(true);
		cannonPaint.setStrokeWidth(lineWidth * 1.5f);
		blockerPaint.setStrokeWidth(lineWidth);
		targetPaint.setStrokeWidth(lineWidth);
		backgroundPaint.setColor(Color.WHITE);

		newGame();

	}

	// reset all screen elements and start a new game
	private void newGame() {
		for (int i = 0; i < TARGET_PIECES; i++) {
			hitStates[i] = false;
		}

		targetPiecesHit = 0;
		blockerVelocity = initialBlockerVelocity;
		targetVelocity = initialTargetVelocity;
		timeLeft = 10;
		cannonballOnScreen = false;

		shotsFired = 0;
		totalElapsedTime = 0.0;
		blocker.start.set(blockerDistance, blockerBeginning);
		blocker.end.set(blockerDistance, blockerEnd);
		target.start.set(targetDistance, targetBeginning);
		target.end.set(targetDistance, targetEnd);
		
		if(gameOver) {
			gameOver = false;
//			cannonThread = new CannonThread();
//			cannonThread.start();
		}
	}

	public void fireCannonBall() {

	}

	public void stopGame() {

	}

	public void releaseResources() {

	}

	public void alignCannon(MotionEvent event) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}
}
