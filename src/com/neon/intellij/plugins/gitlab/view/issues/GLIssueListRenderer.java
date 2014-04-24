package com.neon.intellij.plugins.gitlab.view.issues;

import com.neon.intellij.plugins.gitlab.model.GLRootIssue;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabProject;

public class GLIssueListRenderer implements TreeCellRenderer {

    public GLIssueListRenderer() {

    }

    @Override
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) value;
        Object userObject = node.getUserObject();

        StringBuilder sb = new StringBuilder();
        if ( userObject instanceof GitlabProject) {
            GitlabProject project = (GitlabProject) userObject;

            sb.append( "#" ).append( project.getId() ).append( ": " ).append( project.getName() );
            if ( project.getDescription() != null && ! project.getDescription().trim().isEmpty() ) {
                sb.append( " - " ).append( project.getDescription() );
            }
            if ( node.getChildCount() > 0 ) {
                sb.append( " ( " ).append( node.getChildCount() ).append( " )" );
            }

        } else if ( userObject instanceof GitlabIssue) {
            GitlabIssue issue = (GitlabIssue) userObject;

            sb.append( "#" ).append( issue.getId() ).append( ": " ).append( issue.getTitle() ).append( " ( " ).append( issue.getState() ).append( " )" );

        } else if ( node instanceof GLRootIssue ) {

            sb.append( node.toString() );
            if ( node.getChildCount() > 0 ) {
                sb.append( " ( " ).append( node.getChildCount() ).append( " )" );
            }

        } else {
            sb.append( node.toString() );
        }

        return new JLabel( sb.toString() );
    }

}
