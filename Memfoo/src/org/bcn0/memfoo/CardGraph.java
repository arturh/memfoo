package org.bcn0.memfoo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class CardGraph extends View {

	int[] data;
	
	public CardGraph(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CardGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CardGraph(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
	}
	
	@Override
	protected void onDraw(android.graphics.Canvas canvas) {
		Paint paint = new Paint();
		paint.setARGB(255, 200, 0, 0);
		canvas.drawLine(0, 0, 100, 100, paint);
		canvas.drawLine(0, 100, 100, 0, paint);
		
		if (getMeasuredWidth() < getMeasuredHeight()) {
			paint.setColor(Color.YELLOW);
		} else {
			paint.setColor(Color.MAGENTA);
		}
		
		final int NUMBER_BARS = 3;
		final int MAX_HEIGHT = getMeasuredHeight();
		final int BAR_WIDTH  = getMeasuredWidth()/NUMBER_BARS;
		final int MARGIN = 20;
		final int MAX_VALUE = 100;
		
		final double v1 = 10., v2 = 20., v3 = 40.;
		
		LinearGradient lg = new LinearGradient(0, MAX_HEIGHT/2, 0, MAX_HEIGHT, Color.RED, Color.GREEN, Shader.TileMode.CLAMP);
		paint.setShader(lg);
		
		for (int i = 0; i < NUMBER_BARS; ++i) {
			canvas.drawRect(i*BAR_WIDTH + MARGIN,
							(float) (MAX_HEIGHT - v1/MAX_VALUE*MAX_HEIGHT),
							BAR_WIDTH - MARGIN,
							MAX_HEIGHT,
							paint);
		}
		
		canvas.drawRect(MARGIN, (float) (MAX_HEIGHT - v1/MAX_VALUE*MAX_HEIGHT), BAR_WIDTH - MARGIN, MAX_HEIGHT, paint);
		canvas.drawRect(BAR_WIDTH + MARGIN, (float) (MAX_HEIGHT - v2/MAX_VALUE*MAX_HEIGHT), 2*BAR_WIDTH - MARGIN, MAX_HEIGHT, paint);
		canvas.drawRect(2*BAR_WIDTH + MARGIN, (float) (MAX_HEIGHT - v3/MAX_VALUE*MAX_HEIGHT), 3*BAR_WIDTH - MARGIN, MAX_HEIGHT, paint);
		
		paint.setTextSize(30);
		paint.setColor(Color.CYAN);
		canvas.drawText("foo", 0, 100, paint);
	}
	

}
