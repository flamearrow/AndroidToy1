package com.example.canonGame;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.example.ccweibo.R;

public class CanonGame extends Activity {
	private GestureDetector gestureDetector;
	private CannonView cannonView;

	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		public boolean onDoubleTap(android.view.MotionEvent e) {
			cannonView.fireCannonBall();
			return true;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_canon);
		// there's no predefined layout file for this app, will create the View
		// object from scratch
		cannonView = (CannonView) findViewById(R.id.cannonView);
		gestureDetector = new GestureDetector(this, gestureListener);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	protected void onPause() {
		super.onPause();
		cannonView.stopGame();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cannonView.releaseResources();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		// touch or drag on THIS activity
		if (action == MotionEvent.ACTION_DOWN
				|| action == MotionEvent.ACTION_MOVE) {
			cannonView.alignCannon(event);
		}
		return gestureDetector.onTouchEvent(event);
	}
}
