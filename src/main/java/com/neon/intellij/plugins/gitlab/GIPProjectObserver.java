package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;

public interface GIPProjectObserver {

    default void onStartProjectUpdate( GIPProject project ) {

    }

    void accept( GIPProject project );

}
