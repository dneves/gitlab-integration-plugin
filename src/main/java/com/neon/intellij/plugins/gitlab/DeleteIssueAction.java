package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;

import java.util.function.Function;

public interface DeleteIssueAction extends Function<GIPIssue, GIPIssue> {
}
