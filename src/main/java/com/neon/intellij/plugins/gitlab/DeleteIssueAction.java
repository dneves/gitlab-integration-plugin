package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;

import java.util.function.Consumer;

public interface DeleteIssueAction extends Consumer<GIPIssue> {
}
