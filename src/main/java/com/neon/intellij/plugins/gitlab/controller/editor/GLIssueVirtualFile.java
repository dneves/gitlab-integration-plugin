package com.neon.intellij.plugins.gitlab.controller.editor;

import com.intellij.testFramework.LightVirtualFile;
import com.neon.intellij.plugins.gitlab.CloseIssueEditorAction;
import com.neon.intellij.plugins.gitlab.SaveIssueAction;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import org.jetbrains.annotations.NotNull;

public class GLIssueVirtualFile extends LightVirtualFile {

    private final SaveIssueAction saveIssueAction;

    private final CloseIssueEditorAction closeIssueEditorAction;

    private GIPIssue issue;

    public GLIssueVirtualFile(@NotNull GIPIssue issue, SaveIssueAction saveIssueAction, CloseIssueEditorAction closeIssueEditorAction ) {
        super( issue.title == null ? "<new issue>" : issue.title, GLEditorProvider.FILE_TYPE, "" );

        this.saveIssueAction = saveIssueAction;
        this.closeIssueEditorAction = closeIssueEditorAction;
        this.issue = issue;
    }

    public GIPIssue getIssue() {
        return issue;
    }

    public void setIssue(GIPIssue issue) {
        this.issue = issue;
    }

    public void saveAndClose() {
        saveIssueAction.apply( issue );
        close();
    }

    public void close() {
        closeIssueEditorAction.accept( this );
    }

}
