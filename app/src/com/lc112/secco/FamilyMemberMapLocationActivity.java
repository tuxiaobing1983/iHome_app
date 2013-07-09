package com.lc112.secco;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;


public class FamilyMemberMapLocationActivity extends Activity {
	
	String TAG = "LC112";
	//UI相关
	Button mBtnDrive = null;	// driver search
	Button mBtnTransit = null;	// bus search
	Button mBtnWalk = null;	// walk search
	Button mBtnCusRoute = null; //custom search
	
	//node related
	Button mBtnPre = null;//prev node
	Button mBtnNext = null;//next node
	int nodeIndex = -2;//node index
	MKRoute route = null;//save driver/walk data variable for node view
	TransitOverlay transit = null;//save bus data variable
	int searchType = -1;// distinguish driver/walk or bus search
	private PopupOverlay   pop  = null;
	private MyMapOverlay mOverlay = null;
	private TextView  popupText = null;
	private View viewCache = null;
	
	
	MyRouteMapView mMapView = null;	
	
	MKSearch mSearch = null;	
	
	double fmLat = 39.945 ;
    double fmLon = 116.404 ;
    double myLat = 38;
    double myLon = 116;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        MyApplication app = (MyApplication)this.getApplication();
		setContentView(R.layout.map);
		CharSequence titleLable="Route plan";
        setTitle(titleLable);
		
        mMapView = (MyRouteMapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        mMapView.getController().setZoom(12);
        mMapView.getController().enableClick(true);
        
        Intent  intent = getIntent();
        if ( intent.hasExtra("x") && intent.hasExtra("y") ){
        	
        	Log.v(TAG, "Intent has extral data===========");
        	Bundle b = intent.getExtras();
        	fmLat = b.getDouble("x");
        	fmLon = b.getDouble("y");
        	//p = new GeoPoint((int)(b.getDouble("x") * 1E6), (int)(b.getDouble("y") * 1E6));
        
        }
        GeoPoint p = new GeoPoint((int)(fmLat * 1E6), (int)(fmLon * 1E6));
        initOverlay();
        mMapView.getController().setCenter(p);

        //find view 
        mBtnDrive = (Button)findViewById(R.id.drive);
        mBtnTransit = (Button)findViewById(R.id.transit);
        mBtnWalk = (Button)findViewById(R.id.walk);
        mBtnPre = (Button)findViewById(R.id.pre);
        mBtnNext = (Button)findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
        //set listener
        OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
				
				SearchButtonProcess(v);
			}
        };
        OnClickListener nodeClickListener = new OnClickListener(){
			public void onClick(View v) {
				
				nodeClick(v);
			}
        };
       
        
        mBtnDrive.setOnClickListener(clickListener); 
        mBtnTransit.setOnClickListener(clickListener); 
        mBtnWalk.setOnClickListener(clickListener);
        mBtnPre.setOnClickListener(nodeClickListener);
        mBtnNext.setOnClickListener(nodeClickListener);
       
        
        createPaopao();
        
       
        mSearch = new MKSearch();
        
        boolean ret = mSearch.init(app.mBMapManager, new MKSearchListener(){

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
				Log.v(TAG,"onGetDrivingRouteResult=============");
				
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//iterate all addresses
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				//error num refer MKEVENT
				if (error != 0 || res == null) {
					Toast.makeText(FamilyMemberMapLocationActivity.this, "Sorry, No found address", Toast.LENGTH_SHORT).show();
					return;
				}
			
				searchType = 0;
				RouteOverlay routeOverlay = new RouteOverlay(FamilyMemberMapLocationActivity.this, mMapView);
			    // demo a plan
			    routeOverlay.setData(res.getPlan(0).getRoute(0));
			   
			    mMapView.getOverlays().clear();
			  
			    mMapView.getOverlays().add(routeOverlay);
			    
			    mMapView.refresh();
			    
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			   
			    mMapView.getController().animateTo(res.getStart().pt);
			    mMapView.setBuiltInZoomControls(true);
			   
			    route = res.getPlan(0).getRoute(0);
			    
			    nodeIndex = -1;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
				Log.v(TAG, "onGetTransitRouteResult==============");
				
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//iterate all address
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					Toast.makeText(FamilyMemberMapLocationActivity.this, "Sorry, No found address", Toast.LENGTH_SHORT).show();
					return;
				}
				
				searchType = 1;
				TransitOverlay  routeOverlay = new TransitOverlay (FamilyMemberMapLocationActivity.this, mMapView);
			    // demo a plan
			    routeOverlay.setData(res.getPlan(0));
			 
			    mMapView.getOverlays().clear();
			 
			    mMapView.getOverlays().add(routeOverlay);
			  
			    mMapView.refresh();
			   
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			
			    mMapView.getController().animateTo(res.getStart().pt);
			    mMapView.setBuiltInZoomControls(true);
			
			    transit = routeOverlay;
			
			    nodeIndex = 0;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				
				Log.v(TAG, "onGetWalkingRouteResult=============");
				
				if (error == MKEvent.ERROR_ROUTE_ADDR){
				
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					Toast.makeText(FamilyMemberMapLocationActivity.this, "Sorry, No found address", Toast.LENGTH_SHORT).show();
					return;
				}

				searchType = 2;
				RouteOverlay routeOverlay = new RouteOverlay(FamilyMemberMapLocationActivity.this, mMapView);
			    
				routeOverlay.setData(res.getPlan(0).getRoute(0));
			
			    mMapView.getOverlays().clear();
			
			    mMapView.getOverlays().add(routeOverlay);
			  
			    mMapView.refresh();
			  
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			
			    mMapView.getController().animateTo(res.getStart().pt);
			    mMapView.setBuiltInZoomControls(true);
			   
			    route = res.getPlan(0).getRoute(0);
			    
			    nodeIndex = -1;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			    
			}
			public void onGetAddrResult(MKAddrInfo res, int error) {
			}
			public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
			}
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			@Override
			public void onGetPoiDetailSearchResult(int type, int iError) {
				// TODO Auto-generated method stub
			}
        });
        Log.v(TAG, "FM  Search ret: " + ret);
	}
	
	void SearchButtonProcess(View v) {
		Log.v(TAG, "SearchButtonProcess===========");
		if ( pop != null){
			pop.hidePop();
		}
		
		route = null;
		transit = null; 
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		
		MKPlanNode stNode = new MKPlanNode();
		//stNode.name = editSt.getText().toString();
		stNode.pt =  new GeoPoint((int)(39 * 1E6), (int)(116 * 1E6));
		MKPlanNode enNode = new MKPlanNode();
		//enNode.name = editEn.getText().toString();
		enNode.pt =  new GeoPoint((int)(39 * 1E6), (int)(117 * 1E6));

		
		if (mBtnDrive.equals(v)) {
			mSearch.drivingSearch("", stNode, "", enNode);
		} else if (mBtnTransit.equals(v)) {
			mSearch.transitSearch("", stNode, enNode);
		} else if (mBtnWalk.equals(v)) {
			mSearch.walkingSearch("", stNode, "", enNode);
		} 
	}
	
	public void nodeClick(View v){
		viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        popupText =(TextView) viewCache.findViewById(R.id.textcache);
		if (searchType == 0 || searchType == 2){
			// driver/walk
			if (nodeIndex < -1 || route == null || nodeIndex >= route.getNumSteps())
				return;
			
			
			if (mBtnPre.equals(v) && nodeIndex > 0){
				
				nodeIndex--;
				
				mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
				
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						route.getStep(nodeIndex).getPoint(),
						5);
			}
			
			if (mBtnNext.equals(v) && nodeIndex < (route.getNumSteps()-1)){
				
				nodeIndex++;
				
				mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
				
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						route.getStep(nodeIndex).getPoint(),
						5);
			}
		}
		if (searchType == 1){
			//bus 
			if (nodeIndex < -1 || transit == null || nodeIndex >= transit.getAllItem().size())
				return;
			
	
			if (mBtnPre.equals(v) && nodeIndex > 1){
			
				nodeIndex--;
				
				mMapView.getController().animateTo(transit.getItem(nodeIndex).getPoint());
			
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(transit.getItem(nodeIndex).getTitle());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						transit.getItem(nodeIndex).getPoint(),
						5);
			}
			
			if (mBtnNext.equals(v) && nodeIndex < (transit.getAllItem().size()-2)){
			
				nodeIndex++;
			
				mMapView.getController().animateTo(transit.getItem(nodeIndex).getPoint());
			
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(transit.getItem(nodeIndex).getTitle());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						transit.getItem(nodeIndex).getPoint(),
						5);
			}
		}
		
	}
	
	public void createPaopao(){
		
        
        PopupClickListener popListener = new PopupClickListener(){
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
        };
        pop = new PopupOverlay(mMapView,popListener);
        MyRouteMapView.pop = pop;
	}
	

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        mMapView.destroy();
        super.onDestroy();
    }
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
    
    public void initOverlay(){
    	mOverlay = new MyMapOverlay(getResources().getDrawable(R.drawable.icon_marka),mMapView);
    	
    	GeoPoint fm_coor = new GeoPoint ((int)(fmLat*1E6),(int)(fmLon*1E6));
        OverlayItem fm_item = new OverlayItem(fm_coor," " ,"");
      
        fm_item.setMarker(getResources().getDrawable(R.drawable.icon_markb));
        
        GeoPoint my_coor = new GeoPoint((int)(myLat*1E6), (int)(myLon*1E6));
        OverlayItem my_item = new OverlayItem(my_coor, " ", " ");
        my_item.setMarker(getResources().getDrawable(R.drawable.icon_marka));
        
        
        mOverlay.addItem(fm_item);
        mOverlay.addItem(my_item);
        
        mMapView.getOverlays().add(mOverlay);
        mMapView.refresh();
       

}
}

 
 class MyRouteMapView extends MapView{
    static PopupOverlay   pop  = null;
	
	public MyRouteMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MyRouteMapView(Context context, AttributeSet attrs){
		super(context,attrs);
	}
	public MyRouteMapView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	@Override
    public boolean onTouchEvent(MotionEvent event){
		if (!super.onTouchEvent(event)){
			//消隐泡泡
			if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
				pop.hidePop();
		}
		return true;
	}
}

 class MyMapOverlay extends ItemizedOverlay{

	public MyMapOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker, mapView);
	}
	

	@Override
	public boolean onTap(int index){
	
		return true;
	}
	
	@Override
	public boolean onTap(GeoPoint pt , MapView mMapView){
		/*
		if (pop != null){
            pop.hidePop();
            mMapView.removeView(button);
		}
		*/
		return false;
	}
}

	


