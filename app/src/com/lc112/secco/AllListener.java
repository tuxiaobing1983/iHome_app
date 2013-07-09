package com.lc112.secco;






import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class AllListener {
	private static String TAG = "LC112";
	private static MyApplication app = null;
	private static LinearLayout prev_next_button = null;
	//UI widget related listeners;
	  static class Widget_Listner implements View.OnClickListener {
		 private Activity mActivity = null;
		 Widget_Listner(Activity member) {
			 mActivity = member;
		 }
		 @Override
		public void onClick(View v) {
			Intent intent = null;
			// TODO Auto-generated method stub
			switch(v.getId()) {
			case R.id.FM1:
				intent = new Intent();
				TextView tv= (TextView)mActivity.findViewById(R.id.FM_coor1);
				String str = tv.getText().toString();
				Log.v(TAG, "Coor1 Text: " + str);
				int coor_index;
				coor_index = str.indexOf(":");
				String coor_str = str.substring(coor_index+1, str.length());
				Log.v(TAG, "Coor: " + coor_str);
				double mLat, mLon;
				mLat = Double.valueOf(coor_str.substring(0, coor_str.indexOf(" ")));
				mLon = Double.valueOf(coor_str.substring( coor_str.indexOf(" "), coor_str.length()));
				
				Log.v(TAG, "Lat: " + mLat);
				Log.v(TAG, "Lon: " + mLon);
				intent.putExtra("x", mLat);
				intent.putExtra("y", mLon);
				intent.setClass(mActivity, FamilyMemberMapLocationActivity.class);
				mActivity.startActivity(intent);
				break;
				
			case R.id.drive:
				app = (MyApplication)mActivity.getApplication();
				Log.v(TAG, "Start driver search 1==============");
				if (app.mSearch != null) {
					Log.v(TAG, "Start driver search 2####################");
				
					MKPlanNode stNode = new MKPlanNode();
					stNode.pt =  new GeoPoint((int)(39 * 1E6), (int)(116 * 1E6));
					MKPlanNode enNode = new MKPlanNode();
					enNode.pt =  new GeoPoint((int)(39 * 1E6), (int)(117 * 1E6));
					app.mSearch.drivingSearch(" ", stNode, " ", enNode);
				}
				break;
				
			
				
			
			default:
				break;
			
			
			}
			
		}
		 
	 }
	 
	 /* ==========Map Listener==========
	  * 
	  */
	//listener for network, permission error
	    static class MyMapGeneralListener implements MKGeneralListener {
	        private Context mContext;
	    	
	    	MyMapGeneralListener(Context context) {
	    		mContext = context;
	    	}
	        @Override
	        public void onGetNetworkState(int iError) {
	            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	                Toast.makeText(mContext, "Network Connect Error",
	                    Toast.LENGTH_LONG).show();
	            }
	            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	                Toast.makeText(mContext, "Input right search keys",
	                        Toast.LENGTH_LONG).show();
	            }
	            // ...
	        }

	        @Override
	        public void onGetPermissionState(int iError) {
	            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
	               
	                Toast.makeText(mContext, 
	                        "Input right key", Toast.LENGTH_LONG).show();
	               
	            }
	        }
	    }
	    static class MyMapMKSearchListener implements MKSearchListener {
            
	    	private Activity mActivity = null;
	    	public MyMapMKSearchListener(Activity member) {
	    		mActivity = member;
	    	}
			@Override
			public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
				// TODO Auto-generated method stub
				Log.v(TAG, "GetAddrResult=========");
				
				TextView addr = (TextView)mActivity.findViewById(R.id.FM_location1);
				addr.append(arg0.strAddr);
				
			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {
				MapView mMapView = ((MyApplication)mActivity.getApplication()).mMapView;
				prev_next_button = (LinearLayout)mActivity.findViewById(R.id.pre_next_button);
				prev_next_button.setVisibility(View.VISIBLE);
				Log.v(TAG, "onGetDrivingRouteResult=============");
				// TODO Auto-generated method stub
				RouteOverlay routeOverlay = new RouteOverlay(mActivity, mMapView);
				routeOverlay.setData(arg0.getPlan(0).getRoute(0));
				 //clear other overlay
			    mMapView.getOverlays().clear();
			    //add routeroverlay
			    mMapView.getOverlays().add(routeOverlay);
			    //refresh
			    mMapView.refresh();
			   
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			    //move to start point
			    mMapView.getController().animateTo(arg0.getStart().pt);
				
				
				
			}

			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub
				
			}
	    	
	    }

}
