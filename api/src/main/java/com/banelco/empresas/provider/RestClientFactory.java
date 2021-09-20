package com.banelco.empresas.provider;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import com.banelco.empresas.util.log.LoggingOutInterceptor;

public class RestClientFactory {

	private static PoolingHttpClientConnectionManager cm = null;

	static {
		Registry<ConnectionSocketFactory> socketFactoryRegistry = null;
		try {
			SSLConnectionSocketFactory trustSelfSignedSocketFactory = new SSLConnectionSocketFactory(
					new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
					new TrustAllHostNameVerifier());
			socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", new PlainConnectionSocketFactory())
					.register("https", trustSelfSignedSocketFactory).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}

		cm = (socketFactoryRegistry != null) ? new PoolingHttpClientConnectionManager(socketFactoryRegistry)
				: new PoolingHttpClientConnectionManager();

		cm.setMaxTotal(100);
		cm.setDefaultMaxPerRoute(100);
		
		IdleConnectionMonitorThread staleMonitor = new IdleConnectionMonitorThread(cm);
		staleMonitor.start();
		try {
			staleMonitor.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static <T> T create(String url, Integer connectionTimeout, Integer receiveTimeout, Class<T> clientClass) {
		return create(url, null, null, connectionTimeout, receiveTimeout, clientClass);
	}

	public static <T> T create(String url, String host, Integer port, Integer connectionTimeout, Integer receiveTimeout,
			Class<T> clientClass) {

		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectionTimeout)
				.setConnectionRequestTimeout(connectionTimeout).setSocketTimeout(receiveTimeout).build();

		LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();

		HttpClientBuilder builder = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(config)
				.addInterceptorFirst((HttpRequestInterceptor) loggingOutInterceptor)
				.addInterceptorLast((HttpResponseInterceptor) loggingOutInterceptor);

		if (StringUtils.isNotBlank(host)) {
			builder.setProxy(new HttpHost(host, port));
		}

		ClientExecutor executor = new ApacheHttpClient4Executor(builder.build());
		return ProxyFactory.create(clientClass, url, executor);
	}
	
	public static void close() {
		cm.close();
	}

	private static class TrustAllHostNameVerifier implements X509HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}

		@Override
		public void verify(String host, SSLSocket ssl) throws IOException {
		}

		@Override
		public void verify(String host, X509Certificate cert) throws SSLException {
		}

		@Override
		public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
		}
	}
}