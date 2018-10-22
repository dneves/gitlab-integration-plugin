package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;

public interface GIPProjectObserver {

    void accept( GIPProject project );

}
