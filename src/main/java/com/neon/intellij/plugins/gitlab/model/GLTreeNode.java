package com.neon.intellij.plugins.gitlab.model;

import javax.swing.tree.DefaultMutableTreeNode;

public class GLTreeNode< TYPE > extends DefaultMutableTreeNode {

    public GLTreeNode() {
    }

    public GLTreeNode( TYPE userObject ) {
        super(userObject);
    }

    public GLTreeNode( TYPE userObject, boolean allowsChildren ) {
        super(userObject, allowsChildren);
    }

    public TYPE getUserObject() {
        return (TYPE) super.getUserObject();
    }
}
