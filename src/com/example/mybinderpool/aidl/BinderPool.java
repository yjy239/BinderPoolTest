package com.example.mybinderpool.aidl;

import java.security.PublicKey;
import java.util.concurrent.CountDownLatch;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class BinderPool {
	private static final String TAG = "BinderPool";
	public static final int BINDER_NONE = -1;
	public static final int BINDER_COMPUTE = 0;
	public static final int BINDER_SECURITY = 1;
	
	private Context context;
	private IBinderPool mBinderPool;
	private static volatile BinderPool sInstance;
	private CountDownLatch mConnectBinderPoolCountDownLatch;
	
	private BinderPool(Context context){
		this.context = context.getApplicationContext();
		connectBinderPoolService();
	}
	//这里是单例模式，保证只有一个BinderPool
	public static BinderPool getInstance(Context context){
		if(sInstance == null){
			synchronized (BinderPool.class) {
				if(sInstance == null){
					sInstance = new BinderPool(context);
				}
			}
		}
		return sInstance;
	}
	
	//这里synchronized的关键字是因为，这个行为可能是并发，进程，防止共享资源出现冲突。
	private synchronized void connectBinderPoolService(){
		mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
		Intent service = new Intent(context,BinderPoolService.class);
		context.bindService(service, mBinderConnection, Context.BIND_AUTO_CREATE);
		try {
			mConnectBinderPoolCountDownLatch.await();
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//调用内部类里面查询Binder的方法
	public IBinder queryBinder(int binderCode){
		IBinder binder = null;
		try {
			if(mBinderPool == null){
				binder = mBinderPool.queryBinder(binderCode);
			}
		} catch (RemoteException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return binder;
	}
	
	//实现ServiceConnection来绑定Service
	private ServiceConnection mBinderConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName classname, IBinder service) {
			// TODO Auto-generated method stub
			mBinderPool = IBinderPool.Stub.asInterface(service);
			try{
				//回调BinderDied方法，为的是给BinderPool设置死亡代理
				mBinderPool.asBinder().linkToDeath(mBinderPoolDeath, 0);
			}catch(RemoteException e){
				e.printStackTrace();
			}
			mConnectBinderPoolCountDownLatch.countDown();
			
		}
	};
	
	private IBinder.DeathRecipient mBinderPoolDeath = new IBinder.DeathRecipient() {
		
		@Override
		public void binderDied() {
			// TODO Auto-generated method stub
			//BinderPool断开后重新连接并且重新绑定
			mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeath, 0);
			mBinderPool = null;
			connectBinderPoolService();
		}
	};
	
	public static class BinderPoolImpl extends IBinderPool.Stub{
		
		public BinderPoolImpl(){
			super();
		}
		

		//实现了IBinderPool.Stub中查询功能
		@Override
		public IBinder queryBinder(int bindercode) throws RemoteException {
			// TODO Auto-generated method stub
			IBinder binder = null;
			switch (bindercode) {
			case BINDER_SECURITY:
				binder = new ISecurityImpl();
				break;

			case BINDER_COMPUTE:
				binder = new IComputeImpl();
				break;
			default:
				break;
			}
			
			return binder;
		}
		
	}
	
	
}
