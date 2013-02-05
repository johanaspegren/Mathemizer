package com.mullern.aspegren.mattecoach;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.DragShadowBuilder;

public class ArrowShadowBuilder extends DragShadowBuilder {
	Drawable d;
	private static final String TAG = "MATTECOACH";

	public ArrowShadowBuilder(View v, Context context) {
		super(v);
		d = context.getResources().getDrawable(R.drawable.arrow);
	}

	@Override
	public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
		// TODO Auto-generated method stub
		int width, height;
		width = getView().getWidth();
		height = getView().getHeight();
		d.setBounds(0, 0, width, height);
		shadowSize.set(width, height);
		shadowTouchPoint.set(width / 2, height);
	}

	public void onDrawShadow(Canvas canvas) {
		canvas.save();
		d.draw(canvas);
		canvas.restore();
	}

}
