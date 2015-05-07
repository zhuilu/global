package com.global.socket;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/******************************************
 * 负责创建和关闭SSL套接字对象
 *
 *
 ******************************************/
public class SSLSocket {
	// 配置SSL服务器地址
	private static final String HOST = "8.99.1.89";
	// 配置SSL服务器端口
	private static final int PORT = 446;

	/************************
	 * 创建SSL套接字对象
	 * 
	 * @Exception
	 ************************/
	public static Socket openSocket() throws Exception{
		Socket socket = null;
		SSLContext sslc;
		
		try {
			sslc = SSLContext.getInstance("TLS");
		
			TrustManager[] trustAllCerts = new TrustManager[] {
			 new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[] {};
				}
				
				public void checkClientTrusted(X509Certificate[] chain,String authType) throws CertificateException {
				}
				
				public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException {
				}
			 }
			};
			
			sslc.init(null,trustAllCerts,null);
			SocketFactory sf = sslc.getSocketFactory();
			socket = sf.createSocket(HOST,PORT);
		} catch(Exception e) {
			throw e;
		} finally {
			return socket;
		}
	}
	
	/*********************************
	 * 关闭SSL套接字对象
	 * 
	 * @param socket SSL套接字对象
	 * @throws Exception
	 *********************************/
	public static void closeSocket(Socket socket) throws Exception {
		if(socket!=null) {
			if(!socket.isClosed()) {
				try {
					socket.close();
				} catch(Exception e) {
					// 抛出异常
					throw e;
				}
			}
		}
	}
}