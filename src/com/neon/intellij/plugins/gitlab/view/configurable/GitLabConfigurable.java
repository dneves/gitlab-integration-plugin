package com.neon.intellij.plugins.gitlab.view.configurable;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.neon.intellij.plugins.gitlab.model.intellij.ConfigurableState;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class GitLabConfigurable implements Configurable {

    private static final Logger LOG = Logger.getInstance("gitlab");

    private ConfigurableState settings;

    private SettingsView view;


    public GitLabConfigurable( ) {
        settings = ConfigurableState.getInstance();
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "GitLab Integration";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public JComponent createComponent() {
        view = new SettingsView();
        view.fill( settings );
        return view;
    }

    /**
     * method constantly called to check for changed content in the view.
     * if false, 'apply' button will be disabled.
     *
     */
    public boolean isModified() {
        String[] save = view.save();
        boolean res = save == null || ! settings.getHost().equals( save[0] ) ||
                ! settings.getToken().equals( save[1] );
        LOG.info( "isModified() : " + res );
        return res;
    }

    /**
     * called on 'apply' or 'ok' button click.
     */
    @Override
    public void apply() throws ConfigurationException {
        String[] save = view.save();
        settings.setHost( save[0] );
        settings.setToken( save[1] );
    }

    /**
     * called on 'cancel' button click.
     */
    @Override
    public void reset() {
        if ( view != null ) {
            view.fill(settings);
        }
    }

    @Override
    public void disposeUIResources() {
        view = null;
    }

}
