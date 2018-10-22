package com.neon.intellij.plugins.gitlab;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.function.Supplier;

public class UnsafeOkHttpClientSupplier implements Supplier< OkHttpClient > {

    private final List< Interceptor > interceptors;

    public UnsafeOkHttpClientSupplier(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public OkHttpClient get() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            if ( interceptors != null ) {
                interceptors.forEach( interceptor -> builder.addInterceptor( interceptor ) );
            }

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
