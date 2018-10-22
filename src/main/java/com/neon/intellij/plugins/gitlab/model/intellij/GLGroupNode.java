package com.neon.intellij.plugins.gitlab.model.intellij;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;

public class GLGroupNode extends GLTreeNode<GIPGroup> {

    public GLGroupNode( GIPGroup group ) {
        super( group, true );
    }

}
