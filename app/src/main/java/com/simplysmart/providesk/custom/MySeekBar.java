package com.simplysmart.providesk.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class MySeekBar extends SeekBar {

	public MySeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	Drawable mThumb;

	@Override
	public void setThumb(Drawable thumb) {
		super.setThumb(thumb);
		mThumb = thumb;
	}

	public Drawable getSeekBarThumb() {
		return mThumb;
	}
}