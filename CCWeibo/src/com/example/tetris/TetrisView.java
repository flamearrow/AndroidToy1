package com.example.tetris;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class TetrisView extends SurfaceView {

	public TetrisView(Context context) {
		super(context);
	}

	// pause the game, we want to save game state after returning to game
	public void pause() {

	}

	public void releaseResources() {

	}

	// rotate the current active block
	public void rotate() {

	}

	// move the current active block to left/right if according whether this tap
	// happens at left/right half of the screen
	public void moveLR(MotionEvent e) {

	}
}
