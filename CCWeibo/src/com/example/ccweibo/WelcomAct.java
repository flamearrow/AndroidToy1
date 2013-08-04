package com.example.ccweibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class WelcomAct extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcom, menu);
		return true;
	}

	public void startTipCalc(View view) {
		startActivity(new Intent(this, TipCalculator.class));
	}

	public void startFavTwt(View view) {
		startActivity(new Intent(this, FavouriteTwitterAct.class));
	}

}
