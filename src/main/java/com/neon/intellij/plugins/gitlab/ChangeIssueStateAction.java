package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;

import java.util.function.BiFunction;

public interface ChangeIssueStateAction extends BiFunction<GIPIssue, String, GIPIssue > {

}
