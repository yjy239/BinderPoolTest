package com.example.mybinderpool.aidl;

interface ISecurityCenter{
	String encrypt(String content);
	String decrypt(String password);
}