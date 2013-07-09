package com.lc112.secco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class FamilyMemberLocation extends Activity {
	private String TAG = "LC112";
	private LinearLayout mFM1 = null;
	private Intent intent;
	
	
	private	MKSearch mSearch = null;	
	private BMapManager mBMapManager = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.family_member_location);

		mFM1 = (LinearLayout)findViewById(R.id.FM1);
		TextView tv= (TextView)findViewById(R.id.FM_coor1);
		double cLat = 29.945 ;
        double cLon = 116.404 ;
		tv.append(String.valueOf(cLat) + " " + String.valueOf(cLon));
		OnClickListener listener = new AllListener.Widget_Listner(this);
		mFM1.setOnClickListener(listener);
		
		TextView mFM1_address = (TextView)findViewById(R.id.FM_location1);
		
        MyApplication app = (MyApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
          
            app.mBMapManager.init(MyApplication.strKey,new AllListener.MyMapGeneralListener(this));
        }
        mBMapManager = app.mBMapManager;
        
        /* *********Search Module ******************
         * 
         */
		
        mSearch = new MKSearch();
		boolean ret = mSearch.init(mBMapManager, new AllListener.MyMapMKSearchListener(this));
		Log.v(TAG,"Search init: " + ret);
		GeoPoint ptCenter = new GeoPoint((int)(cLat*1e6), (int)(cLon*1e6));
	
		Log.v(TAG,"Search=========================");
		mSearch.reverseGeocode(ptCenter);
		
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
	
	

	
	
}
