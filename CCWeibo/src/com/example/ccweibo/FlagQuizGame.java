package com.example.ccweibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class FlagQuizGame extends Activity {
	private static final String TAG = "FlagQuizGame Activity";
	private List<String> fileNameList;
	private List<String> quizCountriesList;
	private Map<String, Boolean> regionsMap;
	private String correctAnswer;
	private int totalGuess;
	private int correctAnswers;
	private int guessRows;
	private Random random;
	private Handler handler;
	private Animation shakeAnimation;

	private TextView answerTextView;
	private TextView questionNumberTextView;
	private ImageView flagImageView;
	private TableLayout buttonTableLayout;

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
//		resetQuiz();
	}
}
