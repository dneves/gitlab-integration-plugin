package com.neon.intellij.plugins.gitlab.controller.editor;

import com.intellij.testFramework.LightVirtualFile;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import java.io.IOException;
import org.gitlab.api.models.GitlabIssue;
import org.jetbrains.annotations.NotNull;

public class GLIssueVirtualFile extends LightVirtualFile {

    private final GLIController controller;

    private GitlabIssue issue;

    public GLIssueVirtualFile(@NotNull GLIController controller, @NotNull GitlabIssue issue) {
        super( issue.getTitle() == null ? "<new issue>" : issue.getTitle(), GLEditorProvider.FILE_TYPE, "" );

        this.controller = controller;
        this.issue = issue;
    }

    public GitlabIssue getIssue() {
        return issue;
    }

    public void setIssue(GitlabIssue issue) {
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
