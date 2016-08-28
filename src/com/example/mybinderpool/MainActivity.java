package com.example.mybinderpool;

import com.example.mybinderpool.aidl.BinderPool;
import com.example.mybinderpool.aidl.ICompute;
import com.example.mybinderpool.aidl.IComputeImpl;
import com.example.mybinderpool.aidl.ISecurityCenter;
import com.example.mybinderpool.aidl.ISecurityImpl;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private ISecurityCenter security;
	private ICompute compute;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(){
        	@Override
        	public void run(){
        		dowork();
        	}
        }.start();
    }


    private void dowork(){
    	BinderPool binderPool = BinderPool.getInstance(MainActivity.this);
    	IBinder BinderSecurity = binderPool.queryBinder(BinderPool.BINDER_SECURITY);
    	security = (ISecurityCenter) ISecurityImpl.asInterface(BinderSecurity); 
    	Log.e(TAG, "VISIT SECURITY");
    	String msg = "helloworld";
    	Log.e(TAG, msg);
    	
    	try{
    		String password = security.encrypt("hello world");
    		Log.e("encrypt", ""+password);
    		Log.e("decrypt", ""+security.decrypt(password));
    	}catch (RemoteException e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	
    	IBinder BinderCompute = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
    	compute = (ICompute)IComputeImpl.asInterface(BinderCompute);
    	try{
    		int result = compute.add(3, 5);
    		Log.e("add", "3+5="+result);
    	}catch (RemoteException e) {
			// TODO: handle exception
		}
    }
    
}
