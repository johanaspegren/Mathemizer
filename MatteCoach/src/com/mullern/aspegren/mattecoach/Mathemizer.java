package com.mullern.aspegren.mattecoach;

import java.util.Random;

import com.mullern.aspegren.mattecoach.Questions.Question;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Mathemizer extends Activity {

	private static final String TAG = "MATTECOACH";

	private Questions mQuestions;
	private Question mCurrentQuestion;
	private int mCurrentAnswerX;
	private int mCurrentAnswerY;

	private int mNrOfCorrectAnswers;
	private int mNrOfWrongAnswers;

	private ImageView mLeftFactorImage;
	private ImageView mRightFactorImage;
	private ImageView mTimesImage;
	private ImageView mEqualsImage;
	private ImageView mAnswerLeftImage;
	private ImageView mAnswerRightImage;

	private ImageView mScaleImage;
	private ImageView mArrowImage;

	private ImageView mOkStaple;
	private LinearLayout mStapleContainer;
	private ImageView mHappyC;

	private ArrowDragEventListener mArrawDragListener;

	private ImageView mDbgOk;
	private ImageView mDbgFel;
	private TextView mDragX;

	private int[] mMapOfDrawables;

	ScaleAnimation mScaleAnimation;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		generateQuestion();
		drawCurrentQuestion();

	}

	private void init() {
		mQuestions = new Questions();
		mQuestions.generateQuestions();

		mRightFactorImage = (ImageView) findViewById(R.id.rightFactor);
		mLeftFactorImage = (ImageView) findViewById(R.id.leftFactor);
		mTimesImage = (ImageView) findViewById(R.id.times);
		mEqualsImage = (ImageView) findViewById(R.id.equals);
		mAnswerLeftImage = (ImageView) findViewById(R.id.answerLeft);
		mAnswerRightImage = (ImageView) findViewById(R.id.answerRight);

		mScaleImage = (ImageView) findViewById(R.id.scale);
		mArrowImage = (ImageView) findViewById(R.id.arrow);

		mHappyC = (ImageView) findViewById(R.id.happyC);
		mOkStaple = (ImageView) findViewById(R.id.okStaple);

		mDbgOk = (ImageView) findViewById(R.id.ok);
		// mDbgFel = (ImageView) findViewById(R.id.fel);
		mDragX = (TextView) findViewById(R.id.dragX);

		mDbgOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				handleAnswer();
			}
		});
		//
		// mDbgFel.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// handleAnswer(false);
		// }
		// });

		mArrawDragListener = new ArrowDragEventListener();
		mScaleImage.setOnDragListener(mArrawDragListener);

		mScaleImage.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				ClipData data = ClipData.newPlainText("volume", "data");
				// mArrowImage.setVisibility(View.VISIBLE);
				ArrowShadowBuilder myShadow = new ArrowShadowBuilder(
						mArrowImage, getApplicationContext());

				v.startDrag(data, myShadow, null, 0);
				return false;
			}
		});

		mMapOfDrawables = new int[10];
		mMapOfDrawables[0] = R.drawable.zero;
		mMapOfDrawables[1] = R.drawable.one;
		mMapOfDrawables[2] = R.drawable.two;
		mMapOfDrawables[3] = R.drawable.three;
		mMapOfDrawables[4] = R.drawable.four;
		mMapOfDrawables[5] = R.drawable.five;
		mMapOfDrawables[6] = R.drawable.six;
		mMapOfDrawables[7] = R.drawable.seven;
		mMapOfDrawables[8] = R.drawable.eight;
		mMapOfDrawables[9] = R.drawable.nine;

	}

	protected void handleAnswer() {
		boolean correct = false;

		int answer = Integer.parseInt(mCurrentAnswerX + "" + mCurrentAnswerY);
		if (answer == mCurrentQuestion.y * mCurrentQuestion.x) {
			correct = true;
		}

		clearAnswer();

		if (correct) {
			Toast.makeText(getApplicationContext(), "Bra gjort!",
					Toast.LENGTH_SHORT).show();
			mCurrentQuestion.ok = true;
			mNrOfCorrectAnswers++;
			drawOkStaple();
			generateQuestion();
		} else {
			Toast.makeText(getApplicationContext(), "Försök igen!",
					Toast.LENGTH_SHORT).show();
			mNrOfWrongAnswers++;
			clearOkStaple();
			// do not generate a new question
		}
		drawCurrentQuestion();
	}

	private void drawOkStaple() {
		int h = mOkStaple.getHeight();
		int w = mOkStaple.getWidth();
		float goalHeight = mHappyC.getY();
		float happyHeight = mOkStaple.getY();

		if (mNrOfCorrectAnswers % 2 == 0) {
			mOkStaple.setImageResource(R.drawable.happy_a);
		} else {
			mOkStaple.setImageResource(R.drawable.happy_b);
		}

		LayoutParams lo = new LayoutParams(w, h + 60);
		mOkStaple.setLayoutParams(lo);

		if ((mNrOfCorrectAnswers - mNrOfWrongAnswers) >= 10) {
			// hoppsan du vann
			Toast.makeText(getApplicationContext(), "Du klarade det! Wohooo",
					Toast.LENGTH_SHORT).show();

		}

	}

	private void clearOkStaple() {
		int h = mOkStaple.getHeight();
		int w = mOkStaple.getWidth();

		LayoutParams lo = new LayoutParams(w, (h / 2));
		mOkStaple.setLayoutParams(lo);

	}

	private void clearAnswer() {
		mAnswerRightImage.setVisibility(View.INVISIBLE);
		mAnswerLeftImage.setVisibility(View.INVISIBLE);
	}

	private void generateQuestion() {
		mCurrentQuestion = mQuestions.getNext();
	}

	private void drawCurrentQuestion() {
		mRightFactorImage.setImageResource(mMapOfDrawables[mCurrentQuestion.x]);
		mRightFactorImage.setVisibility(View.VISIBLE);

		mLeftFactorImage.setImageResource(mMapOfDrawables[mCurrentQuestion.y]);
		mLeftFactorImage.setVisibility(View.VISIBLE);

		mTimesImage.setVisibility(View.VISIBLE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public class ArrowDragEventListener implements OnDragListener {

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();
			int id = 0;
			String device = "";
			id = v.getId();

			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				Log.d(TAG, "STARTED." + " id = " + id + " dev = " + device);
				return true;

			case DragEvent.ACTION_DRAG_ENTERED:

				Log.d(TAG, "ENTERED." + " id = " + id + " dev = " + device);
				return true;

			case DragEvent.ACTION_DRAG_LOCATION:
				float x = event.getX();
				float y = event.getY();
				Log.d(TAG, "LOCATION." + " y = " + y + " x = " + x);

				drawAnswer(x);

				return true;

			case DragEvent.ACTION_DROP:
				Log.d(TAG, "DROPPED.");
				ClipData.Item item = event.getClipData().getItemAt(0);
				String fullTrack = item.getText().toString();

				Log.d(TAG, "DROPPED: fullTrack[" + fullTrack + "] id = " + id
						+ " dev = " + device);
				// 01-10 11:04:15.285: D/REVIB)E_UI(29138):
				// DROPPED.http://192.168.43.210:8888/Music/Led Zeppelin IV/04
				return true;

			case DragEvent.ACTION_DRAG_ENDED:
				Log.d(TAG, "ENDED." + " id = " + id + " dev = " + device);
				return true;

			case DragEvent.ACTION_DRAG_EXITED:
				Log.d(TAG, "ENDED." + " id = " + id + " dev = " + device);
				return true;

			default:
				Log.d("DragDrop Example",
						"Unknown action type received by OnDragListener.");
				break;
			}
			return false;
		}

	}

	public void drawAnswer(float x) {
		mAnswerRightImage.setVisibility(View.INVISIBLE);
		mAnswerLeftImage.setVisibility(View.INVISIBLE);
		mEqualsImage.setVisibility(View.INVISIBLE);

		int nr = 0;
		int right = 0;
		int left = 0;

		if (x < 80.0f) {
			right = 0;
			left = 0;
		} else {
			nr = (int) (x - 80) / 16;
			right = nr % 10;
			left = 0;
		}
		// mDragX.setText("x [" + x + "]" + "nr [" + nr + "]" + "left [" + left
		// + "]" + "right [" + right + "]");

		mEqualsImage.setVisibility(View.VISIBLE);
		if (nr < 10) {
			mAnswerLeftImage.setImageResource(mMapOfDrawables[right]);
			mAnswerLeftImage.setVisibility(View.VISIBLE);
		}

		if (nr > 9) {
			left = nr / 10;
			mAnswerRightImage.setImageResource(mMapOfDrawables[right]);
			mAnswerRightImage.setVisibility(View.VISIBLE);
			mAnswerLeftImage.setImageResource(mMapOfDrawables[left]);
			mAnswerLeftImage.setVisibility(View.VISIBLE);
		}
		mCurrentAnswerX = left;
		mCurrentAnswerY = right;

		// mDragX.setText("x [" + x + "]" + "nr [" + nr + "]" + "left [" + left
		// + "]" + "right [" + right + "]");

		// if(x < 155.0f) {
		// mAnswerRightImage.setImageResource(mMapOfDrawables[0]);
		// return;
		// }
		// if(x < 170.0f) {
		// mAnswerRightImage.setImageResource(mMapOfDrawables[1]);
		// return;
		// }
		// if(x < 186.0f) {
		// mAnswerRightImage.setImageResource(mMapOfDrawables[2]);
		// return;
		// }
		// if(x < 202.0f) {
		// mAnswerRightImage.setImageResource(mMapOfDrawables[3]);
		// return;
		// }

	}

}
