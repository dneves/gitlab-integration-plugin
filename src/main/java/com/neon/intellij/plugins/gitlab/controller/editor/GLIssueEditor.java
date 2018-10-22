package com.neon.intellij.plugins.gitlab.controller.editor;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GLIssueEditor extends GLAbstractFileEditor {

//    private final GLIssueEditorView view;

    public GLIssueEditor(final GLIssueVirtualFile vf) {
//        view = new GLIssueEditorView( vf );
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return null;
//        return view;
    }

    @NotNull
    @Override
    public String getName() {
        return "GitLab Issue Editor";
    }

}
