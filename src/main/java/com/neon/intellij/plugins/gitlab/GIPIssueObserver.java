package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;

public interface GIPIssueObserver {

    void accept(GIPIssue issue);

}
