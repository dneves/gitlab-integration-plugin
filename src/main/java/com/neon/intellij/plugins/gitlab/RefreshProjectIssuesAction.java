package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;

import java.util.function.Consumer;

public interface RefreshProjectIssuesAction extends Consumer<GLProjectNode> {
}
