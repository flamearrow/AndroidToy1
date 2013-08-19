package com.example.canonGame;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.example.ccweibo.R;

/**
 * Cannon game uses a seperate thread CannonView.cannonThread to keep track of
 * time and update components and draw them
 * 
 * run() is a while(true) loop, and each time it's called
 * System.currentTimeMillis() is called to calculate the time difference between
 * last draw time, and time*velocity is added to each components previous
 * position
 * 
 * 
 * Note in order to let the customized SurfaceView to show up on the screen, the
 * corresponding xml file shouldn't have a background color
 * 
 * @author flamearrow
 * 
 */
public class CanonGame extends Activity {
	private GestureDetector gestureDetector;
	private CannonView cannonView;

	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		public boolean onDoubleTap(android.view.MotionEvent e) {
			cannonView.fireCannonBall(e);
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
		// catch the the double tab event from onTouchEvent() and pass it the
		// the gestureListener
		int action = event.getAction();
		// touch or drag on THIS activity
		if (action == MotionEvent.ACTION_DOWN
				|| action == MotionEvent.ACTION_MOVE) {
			cannonView.alignCannon(event);
		}
		return gestureDetector.onTouchEvent(event);
	}
}
