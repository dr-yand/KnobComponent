package com.example.misctest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

/*
File:              RoundKnobButton2
Version:           1.0.0
Release Date:      November, 2013
License:           GPL v2
Description:	   A round knob button to control volume and toggle between two states

****************************************************************************
Copyright (C) 2013 Radu Motisan  <radu.motisan@gmail.com>

http://www.pocketmagic.net

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
****************************************************************************/

public class RoundKnobButton2 extends RelativeLayout implements OnGestureListener {

	private GestureDetector 	gestureDetector;
	private float 				mAngleDown , mAngleUp;
	
	private ImageView			ivRotor, ivCenter,ivProgress;
	
	private Bitmap 				bmpRotorOn , bmpRotorOff, bmpCenter;
	
	private boolean 			mState = false;
	private int					m_nWidth = 0, m_nHeight = 0;
	
	interface RoundKnobButton2Listener {
		public void onStateChange(boolean newstate) ;
		public void onRotate(int percentage);
	}
	
	private RoundKnobButton2Listener m_listener;
	
	public void SetListener(RoundKnobButton2Listener l) {
		m_listener = l;
	}
	
	public void SetState(boolean state) {
		mState = state;
		ivRotor.setImageBitmap(state?bmpRotorOn:bmpRotorOff);
	}
	
	public RoundKnobButton2(Context context) {		
		super(context);
		
		final int w=250;
		final int h=250;
		// we won't wait for our size to be calculated, we'll just store out fixed size
		m_nWidth = w; 
		m_nHeight = h;
		// create stator
		ImageView ivStator = new ImageView(context);
		ivStator.setImageResource(R.drawable.back);
		RelativeLayout.LayoutParams lp_ivStator = new RelativeLayout.LayoutParams(
				w,h);
		lp_ivStator.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		ivProgress = new ImageView(context);
		ivProgress.setImageResource(R.drawable.statoroff);
		
		addView(ivStator, lp_ivStator);
		addView(ivProgress, lp_ivStator);
		// load rotor images
		Bitmap srcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotor);
		Bitmap srcoff = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotor);
		Bitmap back = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
	    float scaleWidth = ((float) w) / srcon.getWidth();
	    float scaleHeight = ((float) h) / srcon.getHeight();
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
		    
		bmpRotorOn = Bitmap.createBitmap(
				srcon, 0, 0, 
				srcon.getWidth(),srcon.getHeight() , matrix , true);
		bmpRotorOff = Bitmap.createBitmap(
				srcoff, 0, 0, 
				srcoff.getWidth(),srcoff.getHeight() , matrix , true);
		bmpCenter = Bitmap.createBitmap(
				back, 0, 0, 
				back.getWidth(),back.getHeight() , matrix , true);
		// create rotor
		ivRotor = new ImageView(context);
		ivCenter = new ImageView(context);
		
//		Canvas c = new Canvas(bmpCenter);
//		c.drawBitmap(bmpRotorOn, 0, 0, null);
		
//		ivCenter.setImageBitmap(bmpCenter);
		ivCenter.setImageResource(R.drawable.knob_selector);
		ivCenter.setClickable(true);
		ivRotor.setImageBitmap(bmpRotorOn);
		RelativeLayout.LayoutParams lp_ivKnob = new RelativeLayout.LayoutParams(w,h);//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp_ivKnob.addRule(RelativeLayout.CENTER_IN_PARENT);
		RelativeLayout.LayoutParams lp_ivBack = new RelativeLayout.LayoutParams(100,100);//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp_ivBack.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(ivCenter, lp_ivBack);
		addView(ivRotor, lp_ivKnob);		
		// set initial state
		SetState(mState);
		// enable gesture detector
		gestureDetector = new GestureDetector(getContext(), this);
	}
	
	/**
	 * math..
	 * @param x
	 * @param y
	 * @return
	 */
	private float cartesianToPolar(float x, float y) {
		return (float) -Math.toDegrees(Math.atan2(x - 0.5f, y - 0.5f));
	}

	
	@Override public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) return true;
		else return super.onTouchEvent(event);
	}
	
	public boolean onDown(MotionEvent event) {
		float x = event.getX() / ((float) getWidth());
		float y = event.getY() / ((float) getHeight());
		mAngleDown = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
		return true;
	}
	
	public boolean onSingleTapUp(MotionEvent e) {
		float x = e.getX() / ((float) getWidth());
		float y = e.getY() / ((float) getHeight());
		mAngleUp = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
		
		// if we click up the same place where we clicked down, it's just a button press
		if (! Float.isNaN(mAngleDown) && ! Float.isNaN(mAngleUp) && Math.abs(mAngleUp-mAngleDown) < 10) {
			SetState(!mState);
			if (m_listener != null) m_listener.onStateChange(mState);
		}
		return true;
	}

	public void setRotorPosAngle(float deg) {

		if (deg >= 210 || deg <= 150) {
			if (deg > 180) deg = deg - 360;
			Matrix matrix=new Matrix();
			ivRotor.setScaleType(ScaleType.MATRIX);   
			matrix.postRotate((float) deg, m_nWidth/2, m_nHeight/2);//getWidth()/2, getHeight()/2);
			ivRotor.setImageMatrix(matrix);
			
			drawPosition(deg);
		}
	}
	
	private Paint mPaint;
    private RectF mOval;
//    private float mAngle = 210;
	
	void drawPosition(float deg){
		Bitmap bmpProgressOff = BitmapFactory.decodeResource(getResources(), R.drawable.statoroff).copy(Config.ARGB_8888, true);
		Bitmap bmpProgressOn = BitmapFactory.decodeResource(getResources(), R.drawable.statoron).copy(Config.ARGB_8888, true);
		Canvas c = new Canvas(bmpProgressOff);
//		c.drawBitmap(bmpRotorOn, 0, 0, null);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOval = new RectF();
        
        Matrix m = new Matrix();
        RectF src = new RectF(0, 0, bmpProgressOff.getWidth(), bmpProgressOff.getHeight());
        RectF dst = new RectF(0, 0,  bmpProgressOff.getWidth(), bmpProgressOff.getHeight());
        m.setRectToRect(src, dst, ScaleToFit.CENTER);
        Shader shader = new BitmapShader(bmpProgressOn, TileMode.CLAMP, TileMode.CLAMP);
        shader.setLocalMatrix(m);
        mPaint.setShader(shader);
        m.mapRect(mOval, src);
        
//        c.drawColor(0xff0000aa);
        c.drawArc(mOval, 120, deg+150, true, mPaint);
		
		ivProgress.setImageBitmap(bmpProgressOff);
	}
	
	public void setRotorPercentage(int percentage) {
		int posDegree = percentage * 3 - 150;
		if (posDegree < 0) posDegree = 360 + posDegree;
		setRotorPosAngle(posDegree);
	}
	
	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		float x = e2.getX() / ((float) getWidth());
		float y = e2.getY() / ((float) getHeight());
		float rotDegrees = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
		
		if (! Float.isNaN(rotDegrees)) {
			// instead of getting 0-> 180, -180 0 , we go for 0 -> 360
			float posDegrees = rotDegrees;
			if (rotDegrees < 0) posDegrees = 360 + rotDegrees;
			
			// deny full rotation, start start and stop point, and get a linear scale
			if (posDegrees > 210 || posDegrees < 150) {
				// rotate our imageview
				setRotorPosAngle(posDegrees);
				// get a linear scale
				float scaleDegrees = rotDegrees + 150; // given the current parameters, we go from 0 to 300
				// get position percent
				int percent = (int) (scaleDegrees / 3);
				if (m_listener != null) m_listener.onRotate(percent);
				return true; //consumed
			} else
				return false;
		} else
			return false; // not consumed
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) { return false; }

	public void onLongPress(MotionEvent e) {	}

	



}
