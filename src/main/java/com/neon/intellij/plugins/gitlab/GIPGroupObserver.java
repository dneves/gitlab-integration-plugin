package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;

public interface GIPGroupObserver {

    default void onStartGroupsUpdate() {

    }

    void accept( GIPGroup group );

}
