package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPUser;

import java.util.List;

public interface GIPUserObserver {

    void onStart();

    void accept(List< GIPUser > users);

}
