package com.example.mybinderpool.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BinderPoolService extends Service{
	
	private static final String TAG = "BinderPoolService";
	
	private Binder mBinderPool = new BinderPool.BinderPoolImpl();
	
	@Override
	public void onCreate(){
		super.onCreate();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinderPool;
	}

}
