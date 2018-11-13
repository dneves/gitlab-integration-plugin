package com.neon.intellij.plugins.gitlab.model.intellij;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;

public class GLProjectNode extends GLTreeNode<GIPProject> {

    public GLProjectNode( GIPProject project ) {
        super( project, true );
    }

}
