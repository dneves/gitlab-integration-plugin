package com.neon.intellij.plugins.gitlab.view.issues;

import com.neon.intellij.plugins.gitlab.model.GLTreeNode;
import org.gitlab.api.models.GitlabIssue;

public class GLIssueNode extends GLTreeNode< GitlabIssue > {

    public GLIssueNode( GitlabIssue issue ) {
        super( issue, false );
    }

}
