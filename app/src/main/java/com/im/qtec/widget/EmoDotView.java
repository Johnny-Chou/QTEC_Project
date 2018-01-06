package com.im.qtec.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.im.qtec.R;
import com.im.qtec.utils.SupportMultipleScreensUtil;


/**
 * @author zhouyanglei
 */
public class EmoDotView extends View {

	private float density;
	private int width;
	private float radius;
	private float padding;
	private int position;
	private int count;
	private float mStartX;
	private Paint paint;
	private int selectedColor;
	private int unselectedColor;

	public EmoDotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView(context);
	}

	public EmoDotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView(context);
	}

	public EmoDotView(Context context) {
		super(context);
		initializeView(context);
	}

	private void initializeView(Context context) {
		width = getResources().getDisplayMetrics().widthPixels;
		//density = getResources().getDisplayMetrics().density;
		radius = SupportMultipleScreensUtil.getScaleValue(4);
		padding = SupportMultipleScreensUtil.getScaleValue(20);
		paint = new Paint();
		selectedColor = getResources().getColor(R.color.widgets_dot_pressed);
		unselectedColor = getResources().getColor(R.color.widgets_dot_normal);
	}

	public void notifyDataChanged(int position, int count) {
		this.position = position;
		this.count = count;
		mStartX = (width - ((count - 1) * padding + count * 2 * radius))/2;
		postInvalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < count; i++) {
			if (position == i) {
				paint.setColor(selectedColor);
			}else {
				paint.setColor(unselectedColor);
			}
			canvas.drawCircle(mStartX + i * padding + i * 2 * radius, radius, radius, paint);
		}
		
	}
	
}
