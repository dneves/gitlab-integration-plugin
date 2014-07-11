package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import java.util.Collection;
import org.gitlab.api.models.GitlabIssue;

public interface ProjectIssuesHolder {

    void addIssues( Collection<GitlabIssue> issues, GLProjectNode projectNode );

}
