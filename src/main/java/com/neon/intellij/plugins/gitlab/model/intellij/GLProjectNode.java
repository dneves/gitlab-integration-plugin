package com.neon.intellij.plugins.gitlab.model.intellij;

import com.neon.intellij.plugins.gitlab.model.GLTreeNode;
import org.gitlab.api.models.GitlabProject;


public class GLProjectNode extends GLTreeNode< GitlabProject > {

    public GLProjectNode( GitlabProject project ) {
        super( project, true );
    }
}
