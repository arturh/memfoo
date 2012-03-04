package org.bcn0.memfoo;

import java.util.Arrays;

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
		
		final int MAX_HEIGHT = getMeasuredHeight();
		final int BAR_WIDTH  = getMeasuredWidth()/data.length;
		final int MARGIN = 20;
		final int[] SORTED_DATA = (int[]) data.clone();
		Arrays.sort(SORTED_DATA);
		final int MAX_VALUE = SORTED_DATA[SORTED_DATA.length - 1];
		
		LinearGradient lg = new LinearGradient(
				0, MAX_HEIGHT/2, 0, MAX_HEIGHT,
				Color.rgb(80, 200, 80), Color.GREEN, Shader.TileMode.CLAMP);
		paint.setShader(lg);
		
		Paint paint2 = new Paint();
		paint2.setColor(Color.WHITE);
		paint2.setAlpha(180);
		paint2.setTextSize(30);
		
		for (int i=0; i < MAX_VALUE * 1.1; i += 10) {
			canvas.drawText(Integer.toString(i), 0,  i / MAX_VALUE * MAX_HEIGHT, paint2);
		}
		
		for (int i=0; i<data.length; ++i) {
			int y = data[i] * MAX_VALUE/MAX_HEIGHT;
			canvas.drawRect(
				i * BAR_WIDTH, MAX_HEIGHT - y,
				(i + 1) * BAR_WIDTH, MAX_HEIGHT,
				paint);
			canvas.drawText(Integer.toString(i), i * BAR_WIDTH, MAX_HEIGHT, paint2);
		}
		
//		paint.setTextSize(30);
//		paint.setColor(Color.CYAN);
//		canvas.drawText("foo", 0, 100, paint);
	}

	public void setData(int[] data) {
		this.data = data;
		this.invalidate();
	}
	

}
