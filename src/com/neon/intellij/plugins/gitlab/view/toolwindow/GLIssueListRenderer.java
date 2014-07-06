package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLNamespaceNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class GLIssueListRenderer implements TreeCellRenderer {

    public GLIssueListRenderer() {

    }

    @Override
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
        StringBuilder sb = new StringBuilder();
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) value;

        int childCount = tree.getModel().getChildCount(node);
        if ( node instanceof GLNamespaceNode) {
            GLNamespaceNode namespaceNode = (GLNamespaceNode) node;
            GitlabNamespace namespace = namespaceNode.getUserObject();

            if ( namespace == null ) {
                sb.append( "Default" );
            } else {
                sb.append( namespace.getName() );
                if ( namespace.getDescription() != null && ! namespace.getDescription().trim().isEmpty() ) {
                    sb.append( " - " ).append( namespace.getDescription() );
                }
            }
            if ( childCount > 0 ) {
                sb.append( " ( " ).append(childCount).append( " )" );
            }

        } else if ( node instanceof GLProjectNode) {
            GLProjectNode projectNode = (GLProjectNode) node;
            GitlabProject project = projectNode.getUserObject();

            sb.append( "#" ).append( project.getId() ).append( ": " ).append( project.getName() );
            if ( project.getDescription() != null && ! project.getDescription().trim().isEmpty() ) {
                sb.append( " - " ).append( project.getDescription() );
            }
            if ( childCount > 0 ) {
                sb.append( " ( " ).append(childCount).append( " )" );
            }

        } else if ( node instanceof GLIssueNode) {
            GLIssueNode issueNode = (GLIssueNode) node;
            GitlabIssue issue = issueNode.getUserObject();

            sb.append( "#" ).append( issue.getId() ).append( ": " ).append( issue.getTitle() ).append( " ( " ).append( issue.getState() ).append( " )" );

        } else {
            sb.append( node.toString() );
            if ( childCount > 0 ) {
                sb.append( " ( " ).append(childCount).append( " )" );
            }
        }

        return new JLabel( sb.toString() );
    }

}
