package com.example.misctest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;

import com.example.misctest.RoundKnobButton.RoundKnobButtonListener;

/*
File:              RoundKnobButton
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
public class MainActivity extends Activity {

	Singleton m_Inst = Singleton.getInstance();
		
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        // Scaling mechanism, as explained on:
        // http://www.pocketmagic.net/2013/04/how-to-scale-an-android-ui-on-multiple-screens/
        m_Inst.InitGUIFrame(this);
                
        setContentView(R.layout.activity_main);
        
        /*final SemiCircleProgressBarView semiCircleProgressBarView = (SemiCircleProgressBarView) findViewById(R.id.progress);
        semiCircleProgressBarView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        semiCircleProgressBarView.setOnTouchListener(new OnTouchListener() {			
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("event.getRawX()", event.getRawX()+""+";"+v.getWidth());
				semiCircleProgressBarView.setClipping(event.getRawX()*100/v.getWidth());
				return true;
			}
		});

        semiCircleProgressBarView.setClipping(10);*/
        
        RelativeLayout panel = new RelativeLayout(this);        
        panel = (RelativeLayout)findViewById(R.id.rp);
       
//        TextView tv = new TextView(this); 
//        tv.setText("Rotary knob control\nRadu Motisan 2013\nwww.pocketmagic.net"); tv.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//  		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//  		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//  		panel.addView(tv, lp);
      	
//        TextView tv2 = new TextView(this);
        final TextView tv2 = (TextView)findViewById(R.id.textView1);
//        tv2.setText("");
//        lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//  		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//  		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//  		panel.addView(tv2, lp);

        final Switch sw = (Switch)findViewById(R.id.switchUsers);
          		
//        m_Inst.Scale(250);
//        m_Inst.Scale(250);
        final RoundKnobButton rv = new RoundKnobButton(this, R.drawable.stator3, R.drawable.rotoron3, R.drawable.rotoroff3, 250,250);
        lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		panel.addView(rv, lp);
        
        rv.setRotorPercentage(10);
        
        rv.SetListener(new RoundKnobButtonListener() {
			public void onStateChange(boolean newstate) {
//				Toast.makeText(MainActivity.this,  "New state:"+newstate,  Toast.LENGTH_SHORT).show();
				sw.setChecked(newstate);
			}
			
			public void onRotate(final int percentage) {
				tv2.post(new Runnable() {
					public void run() {
						tv2.setText("\n" + percentage + "%\n");
					}
				}); 
			}
		});
        
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				rv.SetState(isChecked);
			} 
		});        
      
        initView(); 
    }

    private void initView(){
    	((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {    		
    		public void onClick(View v) {
    			if(v.getTag()!=null&&v.getTag().equals(true)){
					v.setSelected(false);
					v.setTag(false);
    			}
    			else{
    				v.setSelected(true);
					v.setTag(true);
    			}
    		}
    	});
    	((Button)findViewById(R.id.Button01)).setOnClickListener(new OnClickListener() {    		
    		public void onClick(View v) {
    			if(v.getTag()!=null&&v.getTag().equals(true)){
					v.setSelected(false);
					v.setTag(false);
    			}
    			else{
    				v.setSelected(true);
					v.setTag(true);
    			}
    		}
    	});
    	
    	Switch sw = (Switch) findViewById(R.id.Switch01);

        sw.setTextColor(Color.WHITE);
		sw.setSwitchTextAppearance(this, Color.WHITE);
		
		sw.setTextOn("  ");
		sw.setTextOff("   ");
    }
    
}
