package com.neon.intellij.plugins.gitlab;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.function.Supplier;

public class SafeOkHttpClientSupplier implements Supplier<OkHttpClient> {

    private final List<Interceptor> interceptors;

    public SafeOkHttpClientSupplier(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public OkHttpClient get() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if ( interceptors != null ) {
            interceptors.forEach( interceptor -> builder.addInterceptor( interceptor ) );
        }

        return builder.build();
    }

}
