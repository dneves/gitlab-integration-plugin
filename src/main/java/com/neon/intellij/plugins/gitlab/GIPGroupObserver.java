package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;

public interface GIPGroupObserver {

    void accept( GIPGroup group );

}
