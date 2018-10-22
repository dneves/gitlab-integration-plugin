package com.neon.intellij.plugins.gitlab;

import retrofit2.Retrofit;

import java.util.function.Supplier;

public class GitLabServiceSupplier implements Supplier< GitLabService > {

    private final Supplier<ConnectionPropertiesSupplier.ConnectionProperties> connectionPropertiesSupplier;

    public GitLabServiceSupplier(Supplier<ConnectionPropertiesSupplier.ConnectionProperties> connectionPropertiesSupplier) {
        this.connectionPropertiesSupplier = connectionPropertiesSupplier;
    }

    @Override
    public GitLabService get() {
        Retrofit retrofit = new RetrofitSupplier( connectionPropertiesSupplier ).get();

        return retrofit.create(GitLabService.class);
    }

}
