package com.neon.intellij.plugins.gitlab.view.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurableProvider;
import org.jetbrains.annotations.Nullable;

public class GitLabConfigurableProvider extends ConfigurableProvider {

    public GitLabConfigurableProvider() {

    }

    @Nullable
    @Override
    public Configurable createConfigurable() {
//        return new GitLabConfigurable();
        return GitLabConfigurable.getInstance();
    }

}
