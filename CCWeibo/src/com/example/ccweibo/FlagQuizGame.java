package com.example.ccweibo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FlagQuizGame extends Activity {
	private static final String TAG = "FlagQuizGame Activity";
	private List<String> fileNameList;
	private List<String> quizCountriesList;
	private Map<String, Boolean> regionsMap;
	private String correctAnswer;
	private int totalGuesses;
	private int correctAnswers;
	private int guessRows;
	private Random random;
	private Handler handler;
	private Animation shakeAnimation;

	private TextView answerTextView;
	private TextView questionNumberTextView;
	private ImageView flagImageView;
	private TableLayout buttonTableLayout;

	private final int CHOICES_MENU_ID = Menu.FIRST;
	private final int REGIONS_MENU_ID = Menu.FIRST + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flag_guess);
		fileNameList = new ArrayList<String>();
		quizCountriesList = new ArrayList<String>();
		regionsMap = new HashMap<String, Boolean>();

		guessRows = 1;
		random = new Random();
		// handler is used to perform delayed operations
		handler = new Handler();

		// each .xml file will automatically create a var under R.java
		shakeAnimation = AnimationUtils.loadAnimation(this,
				R.anim.incorrect_shake);
		shakeAnimation.setRepeatCount(3);

		// use getResources() to get the string values from strings.xml
		String[] regionNames = getResources()
				.getStringArray(R.array.regionList);
		for (String region : regionNames) {
			regionsMap.put(region, true);
		}
		questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
		flagImageView = (ImageView) findViewById(R.id.flagImageView);
		buttonTableLayout = (TableLayout) findViewById(R.id.buttonTableLayout);
		answerTextView = (TextView) findViewById(R.id.answerTextView);

		questionNumberTextView.setText(getResources().getString(
				R.string.question)
				+ " 1 " + getResources().getString(R.string.of) + " 10");

		// do refresh the quiz
		resetQuiz();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, CHOICES_MENU_ID, Menu.NONE, R.string.choices);
		menu.add(Menu.NONE, REGIONS_MENU_ID, Menu.NONE, R.string.regions);
		// return whether this menue needs to be displayed
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case CHOICES_MENU_ID:
			// create a dialog to display available options
			final String[] possibleChoices = getResources().getStringArray(
					R.array.guessesList);
			AlertDialog.Builder choicesBuilder = new AlertDialog.Builder(this);
			choicesBuilder.setTitle(R.string.choices);

			choicesBuilder.setItems(R.array.guessesList,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							guessRows = Integer.parseInt(possibleChoices[which]
									.toString()) / 3;
							resetQuiz();
						}
					});
			AlertDialog choicesDialog = choicesBuilder.create();
			choicesDialog.show();
			return true;
		case REGIONS_MENU_ID:
			final String[] regionNames = regionsMap.keySet().toArray(
					new String[regionsMap.size()]);

			boolean[] regionsEnabled = new boolean[regionsMap.size()];
			for (int i = 0; i < regionsEnabled.length; i++) {
				regionsEnabled[i] = regionsMap.get(regionNames[i]);
			}

			AlertDialog.Builder regionsBuilder = new AlertDialog.Builder(this);
			regionsBuilder.setTitle(R.string.regions);

			String[] displayNames = new String[regionNames.length];
			for (int i = 0; i < regionNames.length; i++) {
				displayNames[i] = regionNames[i].replace('_', ' ');
			}
			regionsBuilder.setMultiChoiceItems(displayNames, regionsEnabled,
					new DialogInterface.OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							regionsMap.put(regionNames[which].toString(),
									isChecked);
						}
					});
			regionsBuilder.setPositiveButton(R.string.reset_quiz,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							resetQuiz();
						}
					});
			AlertDialog regionsDialog = regionsBuilder.create();
			regionsDialog.show();
		}

		return super.onOptionsItemSelected(item);
	}

	private void resetQuiz() {
		// Assets are not compiled with a given name, all files can be accessed
		// during compile time
		AssetManager assets = getAssets();
		fileNameList.clear();
		try {
			Set<String> regions = regionsMap.keySet();
			for (String region : regions) {
				// if this region is enabled
				if (regionsMap.get(region)) {
					// list() will return all the files under the given dir
					String[] paths = assets.list(region);
					// add all files under path to the fileNameList
					for (String path : paths) {
						fileNameList.add(path.replace(".png", ""));
					}
				}
			}
		} catch (IOException e) {
			// log error in an android way
			Log.e(TAG, "Error loading image file names", e);
		}

		correctAnswers = 0;
		totalGuesses = 0;

		quizCountriesList.clear();

		int flagCounter = 1;
		int numberOfFlags = fileNameList.size();

		// add 10 random flags
		while (flagCounter <= 10) {
			int randomIndex = random.nextInt(numberOfFlags);

			String fileName = fileNameList.get(randomIndex);
			if (!quizCountriesList.contains(fileName)) {
				quizCountriesList.add(fileName);
				++flagCounter;
			}
		}
		loadNextFlag();
	}

	private void loadNextFlag() {
		String nextImageName = quizCountriesList.remove(0);
		correctAnswer = nextImageName;

		answerTextView.setText("");

		questionNumberTextView.setText(getResources().getString(
				R.string.question)
				+ " "
				+ (correctAnswers + 1)
				+ " "
				+ getResources().getString(R.string.of) + " 10");

		String region = nextImageName.substring(0, nextImageName.indexOf('-'));

		// AssetManager is used to access raw files
		AssetManager assets = getAssets();
		InputStream stream;

		try {
			// use inputstream and AssetManager to set a picture to the app
			stream = assets.open(region + "/" + nextImageName + ".png");
			Drawable flag = Drawable.createFromStream(stream, nextImageName);
			flagImageView.setImageDrawable(flag);
		} catch (IOException e) {
			Log.e(TAG, "Error loading " + nextImageName, e);
		}

		for (int row = 0; row < buttonTableLayout.getChildCount(); row++)
			((TableRow) buttonTableLayout.getChildAt(row)).removeAllViews();

		Collections.shuffle(fileNameList);

		int correct = fileNameList.indexOf(correctAnswer);
		// put the correct answer at the end of fileNameList, so that we first
		// add the previous (upto) 9, then replace a random one with the
		// last(correct) answer
		fileNameList.add(fileNameList.remove(correct));

		// add the flag dynamically
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int row = 0; row < guessRows; row++) {
			TableRow currentTableRow = getTableRow(row);

			for (int column = 0; column < 3; column++) {
				Button newGuessButton = (Button) inflater.inflate(
						R.layout.guess_button, null);
				String fileName = fileNameList.get(row * 3 + column);
				newGuessButton.setText(getCountryName(fileName));
				newGuessButton.setOnClickListener(guessButtonListener);
				currentTableRow.addView(newGuessButton);
			}
		}

		// pick a random button to set the correct answer
		int row = random.nextInt(guessRows);
		int column = random.nextInt(3);
		TableRow randomTableRow = getTableRow(row);
		String countryName = getCountryName(correctAnswer);
		((Button) randomTableRow.getChildAt(column)).setText(countryName);
	}

	private OnClickListener guessButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			submitGuess((Button) v);
		}
	};

	private void submitGuess(Button b) {
		String guess = b.getText().toString();
		String answer = getCountryName(correctAnswer);
		++totalGuesses;

		if (guess.equals(answer)) {
			++correctAnswers;

			answerTextView.setText(answer + "!");
			answerTextView.setTextColor(getResources().getColor(
					R.color.correct_answer));
			// if it's correct then we disable all the button and do the
			// statistic
			disableButtons();
			// if we have 10 correct answers, pop out a dialog to display the
			// results and give user the option to start over
			if (correctAnswers == 10) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.reset_quiz);

				builder.setMessage(String.format("%d %s, %.02f%% %s",
						totalGuesses, getResources()
								.getString(R.string.guesses),
						(1000 / (double) totalGuesses), getResources()
								.getString(R.string.correct)));

				builder.setCancelable(false);

				// for a dialog we can add a positive button to reset the quiz
				builder.setPositiveButton(R.string.reset_quiz,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								resetQuiz();
							}
						});

				AlertDialog resetDialog = builder.create();
				resetDialog.show();
			}
			// not done yet, load next flag to guess, need to wait for 1 second
			else {
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						loadNextFlag();
					}
				}, 1000);
			}

		} else {
			// View.startAnimation() can be applied to all View objects
			// animation is created by an .xml file
			// then disable the incorrect button
			flagImageView.startAnimation(shakeAnimation);

			answerTextView.setText(R.string.incorrect_answer);
			answerTextView.setTextColor(getResources().getColor(
					R.color.incorrect_answer));
			b.setEnabled(false);
		}
	}

	private void disableButtons() {
		for (int row = 0; row < buttonTableLayout.getChildCount(); row++) {
			TableRow tableRow = (TableRow) buttonTableLayout.getChildAt(row);
			for (int i = 0; i < tableRow.getChildCount(); i++) {
				tableRow.getChildAt(i).setEnabled(false);
			}
		}
	}

	private TableRow getTableRow(int row) {
		return (TableRow) buttonTableLayout.getChildAt(row);
	}

	private String getCountryName(String fileName) {
		return fileName.substring(fileName.indexOf('-') + 1).replace('_', ' ');
	}
}
