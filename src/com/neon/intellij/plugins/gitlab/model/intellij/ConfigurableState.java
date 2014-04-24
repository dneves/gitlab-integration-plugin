package com.neon.intellij.plugins.gitlab.model.intellij;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.components.StorageScheme;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.Nullable;

@State(
        name = "ConfigurableState",
        storages = {
                @Storage( id = "default", file = StoragePathMacros.PROJECT_FILE ),
                @Storage( id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/ant.xml", scheme = StorageScheme.DIRECTORY_BASED )
        }
)
public class ConfigurableState implements PersistentStateComponent< ConfigurableState > {

    @Transient
    private static final Logger LOG = Logger.getInstance("gitlab");

    public String host;

    public String token;


    public ConfigurableState() {

    }

    public static ConfigurableState getInstance() {
        return ServiceManager.getService( ConfigurableState.class );
    }

    @Nullable
    @Override
    public ConfigurableState getState() {
        LOG.info( "getState() : host=" + host + ", token=" + token );
        return this;
    }

    @Override
    public void loadState( ConfigurableState configurableState ) {
        LOG.info( "loadState() : host=" + configurableState.host + ", token=" + configurableState.token );
        XmlSerializerUtil.copyBean( configurableState, this );
    }

    public String getHost() {
        return host == null ? "" : host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
