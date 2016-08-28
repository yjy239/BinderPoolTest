package com.example.mybinderpool.aidl;

import android.os.RemoteException;

public class ISecurityImpl extends ISecurityCenter.Stub{
	private static char SECRET_CODE= '^';
	
	@Override
	public String encrypt(String content) throws RemoteException {
		// TODO Auto-generated method stub
		char[] chars = content.toCharArray();
		for(int i = 0;i < chars.length;i++){
			chars[i] ^= SECRET_CODE;
		}
		return new String(chars);
	}

	@Override
	public String decrypt(String password) throws RemoteException {
		// TODO Auto-generated method stub
		return password;
	}

}
