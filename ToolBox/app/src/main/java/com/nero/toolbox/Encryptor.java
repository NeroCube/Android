package com.nero.toolbox;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class Encryptor {
	private static String i ="a1u2j5h9c5i7o6p2";//16
	private static String j ="q9s4d5r6f1g2y3c6x9d8g9b6j2k3i7m8";//32
	
	public static String Encrypt(String data) {
		if(data!=null) {
			byte[] TextByte = null;
			try {
				TextByte = EncryptAES(i.getBytes("UTF-8"), j.getBytes("UTF-8"), data.getBytes("UTF-8"));
				data = Base64.encodeToString(TextByte, Base64.DEFAULT); 
			} catch (UnsupportedEncodingException e) {e.printStackTrace();}
			return data;
    	}
		else{return null;}
	}
	public static String Decrypt(String data) {
		if(data!=null) {
			byte[] TextByte = null;
			try {
				TextByte = DecryptAES(i.getBytes("UTF-8"), j.getBytes("UTF-8"),Base64.decode(data.getBytes("UTF-8"), Base64.DEFAULT));
				data = new String(TextByte,"UTF-8");
			} catch (UnsupportedEncodingException e) {e.printStackTrace();}
			return data;
    	}
		else{return null;}
	}
	
	private static byte[] EncryptAES(byte[] iv, byte[] key,byte[] text) {
	  try {
	    AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
	    SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
	    Cipher mCipher = null;
	    mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    mCipher.init(Cipher.ENCRYPT_MODE,mSecretKeySpec,mAlgorithmParameterSpec);
	        
	    return mCipher.doFinal(text);
	  }
	  catch(Exception ex) {return null;}
	}

	private static byte[] DecryptAES(byte[] iv,byte[] key,byte[] text) {
	  try {
	    AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
	    SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
	    Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    mCipher.init(Cipher.DECRYPT_MODE, 
	                 mSecretKeySpec, 
	                 mAlgorithmParameterSpec);
	        
	    return mCipher.doFinal(text);
	  }
	  catch(Exception ex) {return null;}
	}
}
