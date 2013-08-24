package com.example.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * Tetris will be represented by a 18 * 10 color
 * 
 * @author flamearrow
 * 
 */
public class TetrisView extends SurfaceView implements Callback {

	private static final int MATRIX_HEIGHT = 18;
	private static final int MATRIX_WIDTH = 18;
	private static final int SQUARE_EDGE_WIDTH = 2;
	private static final int SQUARE_EDGE_COLOR = Color.YELLOW;

	private TetrisThread _thread;

	// Android Color are all int!
	private int[][] _colorMatrix;

	private int _screenWidth;
	private int _screenHeight;

	// Paint object si used to draw stuff
	private Paint _backgroundPaint;
	private Paint _matrixPaint;

	public TetrisView(Context context) {
		super(context);
		_colorMatrix = new int[MATRIX_HEIGHT][MATRIX_WIDTH];
		_backgroundPaint = new Paint();
		_matrixPaint = new Paint();
	}

	// pause the game, we want to save game state after returning to game
	public void pause() {

	}

	public void releaseResources() {

	}

	// rotate the current active block
	public void rotate() {

	}

	private void updateComponents() {
		updateMatrix();
		updateTimer();
		updateScoreBoard();
	}

	private void updateMatrix() {
		for (int i = 0; i < MATRIX_HEIGHT; i++)
			for (int j = 0; j < MATRIX_WIDTH; j++)
				_colorMatrix[i][j] = Color.BLUE;

	}

	private void updateTimer() {

	}

	private void updateScoreBoard() {

	}

	private void drawComponents(Canvas canvas) {
		// starting from bottom left, draw a MATRIX_HEIGHT x MATRIX_WIDTH matrix
		// each block has a black frame and filled with color at
		// _colorMatrix[i][j] 

		// a squre's edge length, should accommodate with width
		int blockEdge = _screenWidth / MATRIX_WIDTH;

		// bottom left
		Point currentPoint = new Point(0, _screenHeight);
		for (int i = MATRIX_HEIGHT - 1; i >= 0; i--) {
			for (int j = 0; j < MATRIX_WIDTH; j++) {
				// draw edge
				_matrixPaint.setColor(SQUARE_EDGE_COLOR);
				canvas.drawRect(currentPoint.x, currentPoint.y - blockEdge,
						currentPoint.x + blockEdge, currentPoint.y,
						_matrixPaint);
				// draw squre
				_matrixPaint.setColor(_colorMatrix[i][j]);
				canvas.drawRect(currentPoint.x + SQUARE_EDGE_WIDTH,
						currentPoint.y - blockEdge + SQUARE_EDGE_WIDTH,
						currentPoint.x + blockEdge - SQUARE_EDGE_WIDTH,
						currentPoint.y - SQUARE_EDGE_WIDTH, _matrixPaint);
				currentPoint.offset(blockEdge, 0);
			}
			// move to the start of next line
			currentPoint.offset(-MATRIX_WIDTH * blockEdge, -blockEdge);
		}
	}

	// called when first added to the View, used to record screen width and
	// height
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		_screenHeight = h;
		_screenWidth = w;
	}

	// move the current active block to left/right if according whether this tap
	// happens at left/right half of the screen
	public void moveLR(MotionEvent e) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		_thread = new TetrisThread(holder);
		_thread.setRunning(true);
		_thread.start();
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

	private class TetrisThread extends Thread {
		// _holder is used to retrieve Canvas object of the current SurfaceView
		private SurfaceHolder _holder;
		private boolean _running;

		public TetrisThread(SurfaceHolder holder) {
			_holder = holder;
		}

		public void setRunning(boolean running) {
			_running = running;
		}

		@Override
		public void run() {
			Canvas canvas = null;
			long previousFrameTime = System.currentTimeMillis();
			while (_running) {
				try {
					long currentFrameTime = System.currentTimeMillis();
					long elapsed = currentFrameTime - previousFrameTime;

					// we want to update the canvas every 100 milis
					if (elapsed < 100) {
						continue;
					}
					previousFrameTime = currentFrameTime;

					canvas = _holder.lockCanvas();
					synchronized (_holder) {
						updateComponents();
						drawComponents(canvas);
					}
				} finally {
					if (canvas != null) {
						_holder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}
	}
}
