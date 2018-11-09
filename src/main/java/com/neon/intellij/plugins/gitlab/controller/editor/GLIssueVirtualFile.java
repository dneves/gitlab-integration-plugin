package com.neon.intellij.plugins.gitlab.controller.editor;

import com.intellij.testFramework.LightVirtualFile;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GLIssueVirtualFile extends LightVirtualFile {

    private final GLIController controller;

    private GIPIssue issue;

    public GLIssueVirtualFile(@NotNull GLIController controller, @NotNull GIPIssue issue) {
        super( issue.title == null ? "<new issue>" : issue.title, GLEditorProvider.FILE_TYPE, "" );

        this.controller = controller;
        this.issue = issue;
    }

    public GIPIssue getIssue() {
        return issue;
    }

    public void setIssue(GIPIssue issue) {
        this.issue = issue;
    }

    public void saveAndClose() throws IOException {
        controller.saveIssue( issue );
        close();
    }

    public void close() {
        controller.closeEditor( this );
    }

}
