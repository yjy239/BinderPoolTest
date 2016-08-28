package com.example.mybinderpool.aidl;

import android.os.RemoteException;

public class IComputeImpl extends ICompute.Stub{

	@Override
	public int add(int a, int b) throws RemoteException {
		// TODO Auto-generated method stub
		return a + b;
	}

}
