package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLNamespaceNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;

public class GLIssueListRenderer extends DefaultTreeCellRenderer {

    private final ImageIcon fileIcon = new ImageIcon(getClass().getResource("/com/neon/intellij/plugins/gitlab/icons/file-icon-16.png"));

    private final ImageIcon folderIcon = new ImageIcon(getClass().getResource("/com/neon/intellij/plugins/gitlab/icons/folder-icon-16.png"));


    public GLIssueListRenderer() {

    }

    @Override
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
        StringBuilder sb = new StringBuilder();
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) value;

        boolean isIssue = node instanceof GLIssueNode;

        int childCount = tree.getModel().getChildCount( node );

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

            sb.append(project.getName());
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

        TableLayout layout = new TableLayout(
                new double[]{TableLayout.MINIMUM, TableLayout.FILL},
                new double[]{TableLayout.MINIMUM}
        );
        layout.setHGap( 5 );

        JPanel panel = new JPanel( layout );
        panel.add( new JLabel( leaf && isIssue ? fileIcon : folderIcon ), new TableLayoutConstraints( 0, 0 ) );
        panel.add( new JLabel( sb.toString() ), new TableLayoutConstraints( 1, 0 ) );

        return panel;
    }

}
