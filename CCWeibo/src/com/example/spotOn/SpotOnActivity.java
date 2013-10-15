package com.example.spotOn;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.ccweibo.R;

public class SpotOnActivity extends Activity {
	private SpotOnView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spoton);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.spotOnRelativeLayout);
		// here we first find the layout by it's id, note not same with tetris,
		// which directly sets TetrisView as the root of layout.
		// Here SpotOnView and RelativeLayout are two different objects, the
		// activity first set RelativeLayout as its layout, then the SpotOnView
		// is added as a child of the relativeLayout
		view = new SpotOnView(this, getPreferences(Context.MODE_PRIVATE),
				layout);
		layout.addView(view, 0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		view.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		view.resume(this);
	}
}
