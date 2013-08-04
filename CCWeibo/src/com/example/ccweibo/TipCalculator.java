package com.example.ccweibo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TipCalculator extends Activity {

	// These two strings act as keys of the Bundle object
	// so that when we need to recreate the activity, we can get values through
	// the Bundle using these as keys
	private static final String BILL_TOTAL = "BT";
	private static final String CUSTOM_PERCENT = "CP";

	private double currentBillTotal;
	private int currentCustomPercent;
	private EditText tip10ET;
	private EditText tip15ET;
	private EditText tip20ET;
	private EditText total10ET;
	private EditText total15ET;
	private EditText total20ET;
	private EditText billEditText;
	private EditText tipCustomET;
	private EditText totalCustomET;
	private TextView customTipTV;
	private SeekBar seekbar;

	/**
	 * This method should be as simple as possible so that app loads quickly
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tipcalculator);

		// if the app just loads
		if (savedInstanceState == null) {
			currentBillTotal = 0.0;
			currentCustomPercent = 18;
		}
		// otherwise this app is being restored from memory, need to get the
		// saved states
		else {
			currentBillTotal = savedInstanceState.getDouble(BILL_TOTAL);
			currentCustomPercent = savedInstanceState.getInt(CUSTOM_PERCENT);
		}

		// find widgets by their ids, use Activity's findViewById()
		tip10ET = (EditText) findViewById(R.id.tenTipEditTextView);
		tip15ET = (EditText) findViewById(R.id.fifteenTipEditTextView);
		tip20ET = (EditText) findViewById(R.id.twentyTipEditTextView);
		total10ET = (EditText) findViewById(R.id.tenTotalEditTextView);
		total15ET = (EditText) findViewById(R.id.fifteenTotalEditTextView);
		total20ET = (EditText) findViewById(R.id.twentyTotalEditTextView);
		billEditText = (EditText) findViewById(R.id.billEditTextView);
		tipCustomET = (EditText) findViewById(R.id.realTipEditTextView);
		totalCustomET = (EditText) findViewById(R.id.realTotalEditTextView);
		customTipTV = (TextView) findViewById(R.id.custTomtiptextView);
		seekbar = ((SeekBar) findViewById(R.id.custtomSeekbar));

		// add listeners for components that need to be changed, use anonymous
		// inner class...
		billEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					currentBillTotal = Double.parseDouble(s.toString());
				}
				// if it's not a string then we revert
				catch (NumberFormatException e) {
					currentBillTotal = 0;
				}
				updateStandard();
				updateCustom();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// nothing
			}

			@Override
			public void afterTextChanged(Editable s) {
				// nothing
			}
		});

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Nothing

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Nothing

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				currentCustomPercent = seekBar.getProgress();
				updateCustom();
			}
		});
		// need to call these two method after initialize all widgets
		// that's said, every time you rotate screen, everything on this screen
		// is recreated, and you need to set their values to previous states
		// explicitly
		updateCustom();
		updateStandard();
		billEditText.setText(String.format("%.02f", currentBillTotal));
	}

	// when user rotate the screen or slide out a keyboard or minimize the app,
	// we need to save the current state
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putDouble(BILL_TOTAL, currentBillTotal);
		outState.putInt(CUSTOM_PERCENT, currentCustomPercent);
	}

	private void updateStandard() {
		double tenPTip = currentBillTotal * 0.1;
		double tenPTotal = tenPTip + currentBillTotal;

		tip10ET.setText(String.format("%.02f", tenPTip));
		total10ET.setText(String.format("%.02f", tenPTotal));

		double fPTip = currentBillTotal * 0.15;
		double fPTotal = fPTip + currentBillTotal;

		tip15ET.setText(String.format("%.02f", fPTip));
		total15ET.setText(String.format("%.02f", fPTotal));

		double tPTip = currentBillTotal * 0.2;
		double tPTotal = tPTip + currentBillTotal;

		tip20ET.setText(String.format("%.02f", tPTip));
		total20ET.setText(String.format("%.02f", tPTotal));

	}

	private void updateCustom() {
		customTipTV.setText(currentCustomPercent + "%");
		double cTip = currentBillTotal * currentCustomPercent * 0.01;
		double cTotal = currentBillTotal + cTip;

		seekbar.setProgress(currentCustomPercent);
		tipCustomET.setText(String.format("%.02f", cTip));
		totalCustomET.setText(String.format("%.02f", cTotal));
	}

}
