package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import com.neon.intellij.plugins.gitlab.model.intellij.GLGroupNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class GLIssueListRenderer extends DefaultTreeCellRenderer {

//    private final ImageIcon fileIcon = new ImageIcon(getClass().getResource("/com/neon/intellij/plugins/gitlab/icons/file-icon-16.png"));

//    private final ImageIcon folderIcon = new ImageIcon(getClass().getResource("/com/neon/intellij/plugins/gitlab/icons/folder-icon-16.png"));


    public GLIssueListRenderer() {

    }

    @Override
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
        StringBuilder sb = new StringBuilder();
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) value;

//        boolean isIssue = node instanceof GLIssueNode;

        int childCount = tree.getModel().getChildCount( node );

        if ( node instanceof GLGroupNode) {
            GLGroupNode groupNode = (GLGroupNode) node;
            GIPGroup group = groupNode.getUserObject();

            sb.append( group.name );
//            if ( group.description != null && ! group.description.trim().isEmpty() ) {
//                sb.append( " - " ).append( group.description );
//            }

            if ( childCount > 0 ) {
                sb.append( " ( " ).append(childCount).append( " )" );
            }

        } else if ( node instanceof GLProjectNode) {
            GLProjectNode projectNode = (GLProjectNode) node;
            GIPProject project = projectNode.getUserObject();

            sb.append(project.name);
//            if ( project.description != null && ! project.description.trim().isEmpty() ) {
//                sb.append( " - " ).append( project.description );
//            }
            if ( childCount > 0 ) {
                sb.append( " ( " ).append(childCount).append( " )" );
            }
        } else if ( node instanceof GLIssueNode) {
            GLIssueNode issueNode = (GLIssueNode) node;
            GIPIssue issue = issueNode.getUserObject();

            sb.append( "#" ).append( issue.iid ).append( ": " ).append( issue.title ).append( " ( " ).append( issue.title ).append( " )" );

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
//        panel.add( new JLabel( leaf && isIssue ? fileIcon : folderIcon ), new TableLayoutConstraints( 0, 0 ) );
        panel.add( new JLabel( "" ), new TableLayoutConstraints( 0, 0 ) );
        panel.add( new JLabel( sb.toString() ), new TableLayoutConstraints( 1, 0 ) );

        return panel;
    }

}
