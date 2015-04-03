package com.kritsin.android.sample.knob;
   
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class MainActivity extends Activity implements OnCheckedChangeListener {
   
	private Button button1, button2, autoButton;
	   
	private Switch turnSwitch;
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 
		initView();
		 
	}
	  
	private void initView(){
		turnSwitch = (Switch)findViewById(R.id.switchUsers);
		turnSwitch.setOnCheckedChangeListener(this);
		 
		autoButton = (Button)findViewById(R.id.Button01);
		
		button1 = (Button)findViewById(R.id.button1);
		button2 = (Button)findViewById(R.id.button2);
		
		button1.setOnClickListener(new OnLampClickListener());
		button2.setOnClickListener(new OnLampClickListener());
		 
		autoButton.setOnClickListener(new OnAutoClickListener());
		
		button1.setSelected(true);
		button1.setTag(true); 
    }
	
	private void clearSelection(){
		button1.setSelected(false);
		button1.setTag(false);
		button2.setSelected(false);
		button2.setTag(false);
	}
	
	private class OnLampClickListener implements OnClickListener{
		public void onClick(View v) {
			clearSelection();
			if(v.getTag()!=null&&v.getTag().equals(true)){
				v.setSelected(false);
				v.setTag(false);
			}
			else{
				v.setSelected(true);
				v.setTag(true);  
			}
		}
	}
	
	private class OnAutoClickListener implements OnClickListener{
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
	}
	   
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
	}
	
	
}
