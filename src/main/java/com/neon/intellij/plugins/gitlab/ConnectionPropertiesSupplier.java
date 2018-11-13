package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.intellij.ConfigurableState;

import java.util.function.Supplier;

public class ConnectionPropertiesSupplier implements Supplier<ConnectionPropertiesSupplier.ConnectionProperties> {

    @Override
    public ConnectionProperties get() {
        ConfigurableState state = ConfigurableState.getInstance();
        return new ConnectionProperties( state.host, state.token, state.ignoreCertificateErrors );
    }

    public static class ConnectionProperties {
        public final String host;
        public final String privateToken;
        public final boolean ignoreSSLCertificateErrors;

        public ConnectionProperties(String host, String privateToken, boolean ignoreSSLCertificateErrors) {
            this.host = host;
            this.privateToken = privateToken;
            this.ignoreSSLCertificateErrors = ignoreSSLCertificateErrors;
        }

    }

}
