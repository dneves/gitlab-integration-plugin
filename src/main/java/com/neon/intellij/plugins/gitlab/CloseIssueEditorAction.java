package com.neon.intellij.plugins.gitlab;

import com.neon.intellij.plugins.gitlab.controller.editor.GLIssueVirtualFile;

import java.util.function.Consumer;

public interface CloseIssueEditorAction extends Consumer<GLIssueVirtualFile > {
}
