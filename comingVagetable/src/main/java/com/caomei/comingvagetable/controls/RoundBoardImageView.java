package com.caomei.comingvagetable.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundBoardImageView extends ImageView {

	public RoundBoardImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public RoundBoardImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RoundBoardImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true); // 设置画笔为无锯齿
		paint.setColor(Color.WHITE); // 设置画笔颜色 
		paint.setStyle(Style.STROKE);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2 - 1,
				paint);
	}
}
