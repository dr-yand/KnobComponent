package com.kritsin.android.component.knob;

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
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.kritsin.android.sample.knob.R;
 

public class RoundKnobButton extends RelativeLayout implements OnGestureListener {

	private GestureDetector gestureDetector;
	private float mAngleDown , mAngleUp;
	
	private ImageView ivRotor, ivCenter,ivProgress;
	
	private Bitmap bmpRotorOn , bmpRotorOff, bmpCenter;
	
	private boolean mState = false;
	private int m_nWidth = 0, m_nHeight = 0;
	
	private RoundKnobButtonListener m_listener;
	
	private OnCenterClickListener mCenterClickListener;
	
	public interface RoundKnobButtonListener {
		public void onStateChange(boolean newstate) ;
		public void onRotate(int percentage);
	}
	
	public interface OnCenterClickListener  {
		public void onCenterClick() ; 
	}
	
	public void setOnCenterClickListener(OnCenterClickListener l){
		this.mCenterClickListener=l;
	}
	
	public void setListener(RoundKnobButtonListener l) {
		m_listener = l;
	}
	
	public void setState(boolean state) {
		mState = state;
		ivRotor.setImageBitmap(state?bmpRotorOn:bmpRotorOff);
	}
	
	public RoundKnobButton(Context context) {		
		super(context);
		init();		
	}
	
	public RoundKnobButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	    init();
	}

	/*public RoundKnobButton(Context context, AttributeSet attrs, int defStyle) {
		this(context); 
//	    init(context);
	}*/
	
	private void init(){
		double density = getResources().getDisplayMetrics().density;
		final int w=(int)(250*density);
		final int h=(int)(250*density);
		final int innerW=(int)(100*density);
		final int innerH=(int)(100*density);
 
		m_nWidth = w; 
		m_nHeight = h;
 
		ImageView ivStator = new ImageView(getContext());
		ivStator.setImageResource(R.drawable.back);
		RelativeLayout.LayoutParams lp_ivStator = new RelativeLayout.LayoutParams(
				w,h);
		lp_ivStator.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		ivProgress = new ImageView(getContext());
		ivProgress.setImageResource(R.drawable.statoroff);
		
		addView(ivStator, lp_ivStator);
		addView(ivProgress, lp_ivStator);
 
		Bitmap srcon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.rotor);
		Bitmap srcoff = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.rotor);
		Bitmap back = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.back);
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
 
		ivRotor = new ImageView(getContext());
		ivCenter = new ImageView(getContext());
		
//		Canvas c = new Canvas(bmpCenter);
//		c.drawBitmap(bmpRotorOn, 0, 0, null);
		
//		ivCenter.setImageBitmap(bmpCenter);
		ivCenter.setImageResource(R.drawable.knob_selector);
		ivCenter.setClickable(true);
		ivCenter.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(mCenterClickListener!=null)
					mCenterClickListener.onCenterClick();
			}
		});
		
		ivRotor.setImageBitmap(bmpRotorOn);
		RelativeLayout.LayoutParams lp_ivKnob = new RelativeLayout.LayoutParams(w,h);//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp_ivKnob.addRule(RelativeLayout.CENTER_IN_PARENT);
		RelativeLayout.LayoutParams lp_ivBack = new RelativeLayout.LayoutParams(innerW,innerH);//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp_ivBack.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		drawPosition(0);
		 
		addView(ivRotor, lp_ivKnob);		
 
		setState(mState);
 
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

	private float cartesianToPolar2(float x, float y) {
		float result = 0;
		x=125-x;
		y=125-y;
		float r = (float) Math.sqrt(x*x + y*y);
		float fg = y/x;
		float fi = (float) Math.atan2(y, x);
		float deg = (float) Math.toDegrees(fi);
		if (deg < 0) deg = 360 + deg;
		return deg;
	}
	
	@Override public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) return true;
		else return super.onTouchEvent(event);
	}
	
	public boolean onDown(MotionEvent event) {
		float x = event.getX() / ((float) getWidth());
		float y = event.getY() / ((float) getHeight());
		mAngleDown = cartesianToPolar(1 - x, 1 - y); 
		return true;
	}
	
	public boolean onSingleTapUp(MotionEvent e) {
		float x = e.getX() / ((float) getWidth());
		float y = e.getY() / ((float) getHeight());
		mAngleUp = cartesianToPolar(1 - x, 1 - y); 
		
		// if we click up the same place where we clicked down, it's just a button press
		if (! Float.isNaN(mAngleDown) && ! Float.isNaN(mAngleUp) && Math.abs(mAngleUp-mAngleDown) < 10) {
			setState(!mState);
			if (m_listener != null) m_listener.onStateChange(mState);
		}
		onScroll(null, e, 0, 0);
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
			float posDegrees = rotDegrees;
			if (rotDegrees < 0) posDegrees = 360 + rotDegrees;
  
			if (posDegrees >= 210 || posDegrees <= 150) {
 
				setRotorPosAngle(posDegrees);
 
				float scaleDegrees = rotDegrees + 150; // given the current parameters, we go from 0 to 300
 
				int percent = (int) (scaleDegrees / 3); 
				
				if (m_listener != null) m_listener.onRotate(percent);
				return true;  
			}
			else if(posDegrees<210&&posDegrees>180){
				posDegrees=210;
				setRotorPosAngle(posDegrees);  
				int percent = 0;
				if (m_listener != null) m_listener.onRotate(percent);
				return true;  
			}
			else if(posDegrees>150&&posDegrees<180){
				posDegrees=150;
				setRotorPosAngle(posDegrees); 
				int percent = 100; 
				if (m_listener != null) m_listener.onRotate(percent);
				return true;  
			}
			else
				return false;
		} else
			return false;  
	}

	public void onShowPress(MotionEvent e) {
		 
		
	}
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) { return false; }

	public void onLongPress(MotionEvent e) {	}

	



}
