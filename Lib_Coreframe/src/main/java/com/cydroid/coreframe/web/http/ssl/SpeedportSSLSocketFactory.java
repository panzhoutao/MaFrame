package com.cydroid.coreframe.web.http.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import android.util.Log;

public class SpeedportSSLSocketFactory extends SSLSocketFactory {

//private final static Logger logger = Logger.getLogger(SpeedportSSLSocketFactory.class);

/**
 * the order of ciphers in this list is important here e.g. TLS_DHE_* must not stay above TLS_RSA_*
 */
private static final String[] APPROVED_CIPHER_SUITES = new String[]{
       
        "SSL_RSA_WITH_3DES_EDE_CBC_SHA"
        
};

private SSLSocketFactory factory;

public SpeedportSSLSocketFactory(SSLContext sslcontext) {
    try {
//        SSLContext sslcontext = SSLContext.getInstance("TLS");
//        sslcontext.init(null, new TrustManager[]{
//                // accepts certs with valid but expired key chain (incl. root cert)
//                new CustomX509TrustManager()}, new java.security.SecureRandom());
        factory = sslcontext.getSocketFactory();
//       Log.i("getSupportedCipherSuites", getSupportedCipherSuites().toString());
    } catch (Exception ex) {
//        logger.error("Cannot create SpeedportSSLSocketFactory", ex);
    	ex.printStackTrace();
    }
}

// dirty
private void injectHostname(InetAddress address, String host) {
    try {
        java.lang.reflect.Field field = InetAddress.class.getDeclaredField("hostName");
        field.setAccessible(true);
        field.set(address, host);
    } catch (Exception ignored) {
//        logger.error("Cannot inject hostName");
    }
}

public static SocketFactory getDefault() {
	try {
  SSLContext sslcontext = SSLContext.getInstance("TLS");
  sslcontext.init(null, new TrustManager[]{
          // accepts certs with valid but expired key chain (incl. root cert)
          new CustomX509TrustManager()}, new java.security.SecureRandom());
  return new SpeedportSSLSocketFactory(sslcontext);
	} catch (Exception ex) {
//      logger.error("Cannot create SpeedportSSLSocketFactory", ex);
		ex.printStackTrace();
  }
	return null;
}
@Override
public Socket createSocket() throws IOException {
	Socket socket = factory.createSocket();
    ((SSLSocket) socket).setEnabledCipherSuites(getSupportedCipherSuites());
    return socket;
}
@Override
public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
	
	Socket sssocket = factory.createSocket(socket, host, port, autoClose);
    ((SSLSocket) sssocket).setEnabledCipherSuites(getSupportedCipherSuites());
    return sssocket;
}
@Override
public Socket createSocket(InetAddress addr, int port, InetAddress localAddr, int localPort) throws IOException {
	Socket sssocket = factory.createSocket(addr, port, localAddr, localPort);
    ((SSLSocket) sssocket).setEnabledCipherSuites(getSupportedCipherSuites());
    return sssocket;
}
@Override
public Socket createSocket(InetAddress inaddr, int i) throws IOException {
    Socket socket = factory.createSocket(inaddr, i);
    ((SSLSocket) socket).setEnabledCipherSuites(getSupportedCipherSuites());
    return socket;
}
@Override
public Socket createSocket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
	InetAddress addr = InetAddress.getByName(host);
    injectHostname(addr, host);

    Log.i("host+port:", "======"+host+port);
    Socket socket = factory.createSocket(host, port, localAddr, localPort);
    ((SSLSocket) socket).setEnabledCipherSuites(getSupportedCipherSuites());
    return socket;
}

@Override
public Socket createSocket(String host, int port) throws IOException {

    InetAddress addr = InetAddress.getByName(host);
    injectHostname(addr, host);

    Log.i("host+port:", "======"+host+port);
    Socket socket = factory.createSocket(addr, port);
    ((SSLSocket) socket).setEnabledCipherSuites(getSupportedCipherSuites());
    return socket;
}



@Override
public String[] getDefaultCipherSuites() {
    return APPROVED_CIPHER_SUITES;
}

@Override
public String[] getSupportedCipherSuites() {
    return APPROVED_CIPHER_SUITES;
}
}