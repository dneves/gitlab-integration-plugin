package com.neon.intellij.plugins.gitlab.view.issues;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import org.gitlab.api.models.GitlabIssue;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public class GLIssueListMouseAdapter extends MouseAdapter {

    private static final Logger LOG = Logger.getInstance("gitlab");

    private final GLIController controller;

    private final GLIssueListView taskList;

    private final JTree tree;

    public GLIssueListMouseAdapter(final GLIController controller, final GLIssueListView taskList, final JTree tree) {
        this.controller = controller;
        this.taskList = taskList;
        this.tree = tree;
    }

    @Override
    public void mouseClicked( MouseEvent e ) {
        final TreePath path = tree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

        if ( SwingUtilities.isRightMouseButton( e ) ) {
            contextMenu( node, e.getX(), e.getY() );
        } else if ( SwingUtilities.isLeftMouseButton( e ) && e.getClickCount() >= 2 ) {
            doubleClick( node );
        }
    }

    private void contextMenu( final DefaultMutableTreeNode node, final int x, final int y ) {
        if ( node instanceof GLProjectNode ) {
            GLProjectNode projectNode = (GLProjectNode) node;

            JPopupMenu popup = new GLProjectPopup( controller, projectNode );
            popup.show( tree, x, y );

        } else if ( node instanceof GLIssueNode ) {
            GLIssueNode issueNode = (GLIssueNode) node;

            JPopupMenu popup = new GLIssuePopup( controller, issueNode );
            popup.show( tree, x, y );

        }
    }

    private void doubleClick( final DefaultMutableTreeNode node ) {
        if ( node instanceof GLProjectNode ) {
            if ( node.getChildCount() > 0 ) {
                return ;
            }

            final GLProjectNode projectNode = (GLProjectNode) node;

            ProgressManager.getInstance().run( new Task.Backgroundable( controller.getProject(), "Get project issues" ) {
                @Override
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    progressIndicator.setFraction( 0.0 );
                    progressIndicator.setText( "Getting remote issues" );

                    try {
                        final List<GitlabIssue> issues = controller.getIssues( projectNode.getUserObject() );

                        progressIndicator.setFraction( 0.5 );
                        progressIndicator.setText( "Got remote issues" );

                        SwingUtilities.invokeLater( new Runnable() {
                            @Override
                            public void run() {
                                taskList.addIssues( issues, projectNode );
                            }
                        });

                        progressIndicator.setFraction( 1.0 );
                        progressIndicator.setText( "Get issues done" );
                    } catch ( IOException e1 ) {
                        LOG.error( e1 );
                    }
                }
            } );

        } else if ( node instanceof GLIssueNode ) {
            GLIssueNode issueNode = (GLIssueNode) node;
            controller.openEditor( issueNode.getUserObject() );
        }
    }

}
