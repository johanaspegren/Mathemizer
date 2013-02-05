package com.mullern.aspegren.mattecoach;

import java.util.ArrayList;
import java.util.Random;


public class Questions {

	private static final String TAG = "MATTECOACH";
	private ArrayList <Question> mQuestions;
		
	public Questions() {
		super();
	}

	public void generateQuestions() {
		int max = 5;
		
		mQuestions = new ArrayList<Question>();
		for(int x = 0; x <= max; x++) {
			for(int y = 0; y < 10; y++) {
				mQuestions.add(new Question(x,y));
			}
		}
		for(int x = 0; x < 10; x++) {
			for(int y = 0; y <= max; y++) {
				mQuestions.add(new Question(x,y));
			}
		}

	}

	public Question getNext() {

		while (mQuestions.size() > 0) {
			Random r = new Random();
			int i = r.nextInt(mQuestions.size());
			if(!mQuestions.get(i).ok) {
				Question re = mQuestions.remove(i);
				return (re);
			}
		}
		return null;
	}

	class Question {
		int x;
		int y;
		boolean ok;
		int tries;
		
		public Question(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
		
	}
}
