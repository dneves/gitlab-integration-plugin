package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPUser;

import java.util.List;

public interface GIPUserObserver {

    default void onStartUsersUpdate() {

    }

    void accept(List< GIPUser > users);

}
