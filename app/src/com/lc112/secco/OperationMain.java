package com.lc112.secco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class OperationMain extends Activity {
	private String TAG = "OperationMain";
	private TextView room1;
	private TextView room2;
	private TextView kitchen;
	private TextView washroom;
	private TextView parlour;
	private TextView health_system;
	private TextView family_member_location;
	
	class WidgetListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			Intent intent;
			switch (id) {
			case R.id.room1:
				Log.v(TAG, "Start Room1=======");
				intent = new Intent(OperationMain.this, Room1.class);
				startActivity(intent);
				break;
				
			case R.id.room2:
				Log.v(TAG, "Start Room1=======");
			    intent = new Intent(OperationMain.this, Room2.class);
				startActivity(intent);
				break;
			
			case R.id.kitchen:
				intent = new Intent(OperationMain.this, Kitchen.class);
				startActivity(intent);
				break;
				
			case R.id.parlour:
				intent = new Intent(OperationMain.this, Parlour.class);
				startActivity(intent);
				break;
			
			case R.id.washroom:
				intent = new Intent(OperationMain.this, Washroom.class);
				startActivity(intent);
				break;
				
			case R.id.health_system:
				intent = new Intent(OperationMain.this, HealthSystem.class);
				startActivity(intent);
				break;
				
			case R.id.family_member_location:
				Log.v(TAG, "Start FamilyMemberLocation==============");
				intent = new Intent(OperationMain.this, FamilyMemberLocation.class);
				startActivity(intent);
				break;
			default:
				break;
				
			}
			
		}
		
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		WidgetListener widget_listener = new WidgetListener();
		setContentView(R.layout.operation_main);
		room1 = (TextView)findViewById(R.id.room1);
		room1.setOnClickListener(widget_listener);
		
		room2 = (TextView)findViewById(R.id.room2);
		room2.setOnClickListener(widget_listener);
		
		kitchen = (TextView)findViewById(R.id.kitchen);
		kitchen.setOnClickListener(widget_listener);
		
		parlour = (TextView)findViewById(R.id.parlour);
		parlour.setOnClickListener(widget_listener);
		
		washroom = (TextView)findViewById(R.id.washroom);
		washroom.setOnClickListener(widget_listener);
		
		health_system = (TextView)findViewById(R.id.health_system);
		health_system.setOnClickListener(widget_listener);
		
		family_member_location = (TextView)findViewById(R.id.family_member_location);
		family_member_location.setOnClickListener(widget_listener);
		
	}
		

}
