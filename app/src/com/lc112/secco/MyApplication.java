package com.lc112.secco;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.MKSearch;


public class MyApplication extends Application {
	
    private static MyApplication mInstance = null;
   // public boolean m_bKeyRight = true;
    BMapManager mBMapManager = null;
    
    MKSearch mSearch = null;	
    MapView mMapView = null;
    
    public static final String strKey = "5B32D2DB94F0A18EADA8994D42AF48CDC1B1EDED";
	
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		initEngineManager(this);
	}
	
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new AllListener.MyMapGeneralListener(this))) {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                    "BMapManager  Initial error!", Toast.LENGTH_LONG).show();
        }
	}
	
	public void initMapSearcher(Activity context) {
	
        mSearch = new MKSearch();
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
            if (!mBMapManager.init(strKey,new AllListener.MyMapGeneralListener(this))) {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                        "BMapManager  Initial error!", Toast.LENGTH_LONG).show();
            }
        }
        mSearch.init(mBMapManager, new AllListener.MyMapMKSearchListener(context));
	}
	public static MyApplication getInstance() {
		return mInstance;
	}
}
