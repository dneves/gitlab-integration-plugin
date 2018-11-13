package com.neon.intellij.plugins.gitlab;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import java.util.Arrays;
import java.util.function.Supplier;

public class RetrofitSupplier implements Supplier<Retrofit> {

    private final Supplier<ConnectionPropertiesSupplier.ConnectionProperties> connectionPropertiesSupplier;

    public RetrofitSupplier( Supplier<ConnectionPropertiesSupplier.ConnectionProperties> connectionPropertiesSupplier ) {
        this.connectionPropertiesSupplier = connectionPropertiesSupplier;
    }

    @Override
    public Retrofit get() {
        ConnectionPropertiesSupplier.ConnectionProperties connectionProperties = connectionPropertiesSupplier.get();

        Interceptor tokenInterceptor = chain -> {
            Request request = chain.request();

            request = request.newBuilder()
                    .addHeader("Private-Token", connectionProperties.privateToken )
                    .build();

            return chain.proceed(request);
        };

        OkHttpClient okHttpClient = getOkHttpClient( connectionProperties.ignoreSSLCertificateErrors, tokenInterceptor );

        HttpUrl baseUrl = HttpUrl.parse(connectionProperties.host)
                .newBuilder()
                .addPathSegments( "api/v4/" )
                .build();

        return new Retrofit.Builder()
                .baseUrl( baseUrl )
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private OkHttpClient getOkHttpClient( boolean unsafe, Interceptor ... interceptors ) {
        if ( unsafe ) {
            return new UnsafeOkHttpClientSupplier( Arrays.asList( interceptors ) ).get();
        }
        return new SafeOkHttpClientSupplier( Arrays.asList( interceptors ) ).get();
    }
}
