package com.neon.intellij.plugins.gitlab.model.intellij;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;

public class GLIssueNode extends GLTreeNode<GIPIssue> {

    public GLIssueNode( GIPIssue issue ) {
        super( issue, false );
    }

}
