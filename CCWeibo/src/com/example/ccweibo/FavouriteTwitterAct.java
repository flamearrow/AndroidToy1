package com.example.ccweibo;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class FavouriteTwitterAct extends Activity {
	// like a map
	private SharedPreferences savedSearches;
	// populating the layout programatically 
	private TableLayout queryTableLayout;
	private EditText queryEditText;
	private EditText tagEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favtwitter);
		// should have a file names 'searches'
		savedSearches = getSharedPreferences("searches", MODE_PRIVATE);
		// all ui components are inherited from View
		queryTableLayout = (TableLayout) findViewById(R.id.queryTableLayout);
		queryEditText = (EditText) findViewById(R.id.queryEditText);
		tagEditText = (EditText) findViewById(R.id.tagEditText);

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(saveButtonListener);
		Button clearTagsButton = (Button) findViewById(R.id.clearTagsButton);
		clearTagsButton.setOnClickListener(clearTagsButtonListener);
		refreshButtons(null);
	}
	
	// recreate tag and search pairs based on the result from file
	private void refreshButtons(String newTag) {
		String[] tags = savedSearches.getAll().keySet().toArray(new String[0]);
		Arrays.sort(tags, String.CASE_INSENSITIVE_ORDER);

		if (newTag != null) {
			makeTagGUI(newTag, Arrays.binarySearch(tags, newTag));
		} else {
			for (int i = 0; i < tags.length; i++) {
				makeTagGUI(tags[i], i);
			}
		}
	}

	// add a new tag-query pair or modify existing ones
	// SharedPreferences will store the entries in this file:
	// /data/data/YOUR_PACKAGE_NAME/shared_prefs/YOUR_PREFS_NAME.xml
	// note: makeTag will only add data to file, to build it to GUI, need to
	// invoke makeTagGUI
	private void makeTag(String query, String tag) {
		String originalQuery = savedSearches.getString(tag, null);
		SharedPreferences.Editor preferencesEditor = savedSearches.edit();
		preferencesEditor.putString(tag, query);
		preferencesEditor.apply();

		if (originalQuery == null) {
			refreshButtons(tag);
		}
	}

	// add new tag and editbutton to GUI dynamically
	private void makeTagGUI(String tag, int index) {
		// the inflater is a systemService
		// also available to get Alarm_service or power_service
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// inflate a created view
		View newTagView = inflater.inflate(R.layout.new_tag_view, null);

		Button newTagButton = (Button) newTagView
				.findViewById(R.id.newTagButton);
		newTagButton.setText(tag);
		newTagButton.setOnClickListener(queryButtonListener);

		Button newEditButton = (Button) newTagView
				.findViewById(R.id.newEditButton);
		newEditButton.setOnClickListener(editButtonListener);
		queryTableLayout.addView(newTagView, index);
	}

	private void clearButtons() {
		queryTableLayout.removeAllViews();
	}

	// save a query text and update the queryTableLayout
	public OnClickListener saveButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String qTx = queryEditText.getText().toString();
			String tTx = tagEditText.getText().toString();

			if (qTx.length() > 0 && tTx.length() > 0) {
				makeTag(qTx, tTx);
				queryEditText.setText("");
				tagEditText.setText("");
			}
			// spawn a warning dialog
			else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FavouriteTwitterAct.this);

				builder.setTitle(R.string.missingTitle);
				builder.setPositiveButton(R.string.OK, null);
				builder.setMessage(R.string.missingMessage);

				builder.create().show();
			}
		}

	};

	// remove all tags from GUI and SharedPreferences
	// should always do SharedPreferences.edit() to return the Editor
	public OnClickListener clearTagsButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(
					FavouriteTwitterAct.this);
			builder.setTitle(R.string.confirmTitle);
			builder.setPositiveButton(R.string.erase,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							clearButtons();

							SharedPreferences.Editor preferencesEditor = savedSearches
									.edit();
							preferencesEditor.clear();
							preferencesEditor.apply();
						}
					});
			// you can cancel, of course
			builder.setCancelable(false);
			builder.setNegativeButton(R.string.cancel, null);

			builder.setMessage(R.string.confirmMessage);

			builder.create().show();
		}

	};

	// clicking this button will open a browser and take you to the address
	public OnClickListener queryButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// can use v to access the button
			String key = ((Button) v).getText().toString();
			String query = savedSearches.getString(key, null);
			String urlString = getString(R.string.searchURL) + query;

			// create an Intent to launch a web browser

			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlString)));
		}

	};

	// when clicking edit, will set the tagEditText and queryEditText to what
	// was in the tag, and click save will overwite the data
	public OnClickListener editButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// because the edit button and the tag belong to the same TableRow
			// parent, we get its parent and then get the tag
			// there're duplicate ids in the scroll view, so we need this way to
			// get the corresponding tag
			String tag = ((Button) (((TableRow) v.getParent())
					.findViewById(R.id.newTagButton))).getText().toString();
			// we set these two fields to be the current value, user can modify
			// and udpate by clicking save
			tagEditText.setText(tag);
			queryEditText.setText(savedSearches.getString(tag, null));
		}

	};
}
