package com.lc112.secco;

import com.lc112.secco.OperationMain.WidgetListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String TAG="LC112";
	private TextView  tv_enter;
	private TextView company_intro;
	private TextView product_intro;

	
	class WidgetListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			switch(v.getId()) {
			case R.id.company_intro:
				intent = new Intent(MainActivity.this, CompanyIntro.class);
				startActivity(intent);
				break;
			case R.id.product_intro:
				intent = new Intent(MainActivity.this, ProductIntro.class);
				startActivity(intent);
				break;
			
			case R.id.operation_enter:
		        intent = new Intent(MainActivity.this, OperationMain.class);
				startActivity(intent);
				break;
				
			default:
				break;
			
			}
			
		}
		
		
	}
	private WidgetListener widget_listener = new WidgetListener();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		company_intro = (TextView)findViewById(R.id.company_intro);
		company_intro.setOnClickListener(widget_listener);
		
		product_intro = (TextView)findViewById(R.id.product_intro);
		product_intro.setOnClickListener(widget_listener);
		
		tv_enter = (TextView)findViewById(R.id.operation_enter);
		tv_enter.setOnClickListener(widget_listener);
			
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
