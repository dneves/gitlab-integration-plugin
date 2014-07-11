package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.intellij.openapi.progress.ProgressManager;
import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class GLIssueListMouseAdapter extends MouseAdapter {

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
        // null path means nothing selected - we dont care
        DefaultMutableTreeNode node = path == null ? null : (DefaultMutableTreeNode) path.getLastPathComponent();

        if ( SwingUtilities.isRightMouseButton( e ) ) {
            contextMenu( node, e.getX(), e.getY() );
        } else if ( SwingUtilities.isLeftMouseButton( e ) && e.getClickCount() >= 2 ) {
            doubleClick( node );
        }
    }

    private void contextMenu( final DefaultMutableTreeNode node, final int x, final int y ) {
        if ( node instanceof GLProjectNode) {
            GLProjectNode projectNode = (GLProjectNode) node;

            JPopupMenu popup = new GLProjectPopup( controller, this, projectNode );
            popup.show( tree, x, y );

        } else if ( node instanceof GLIssueNode) {
            GLIssueNode issueNode = (GLIssueNode) node;

            JPopupMenu popup = new GLIssuePopup( controller, issueNode );
            popup.show( tree, x, y );
        }
    }

    public void doubleClick( final DefaultMutableTreeNode node ) {
        if ( node instanceof GLProjectNode ) {
            if ( node.getChildCount() > 0 ) {
                return ;
            }

            final GLProjectNode projectNode = (GLProjectNode) node;
            ProgressManager.getInstance().run( new GetIssuesTask( controller, Arrays.asList( projectNode ), taskList ) );
        } else if ( node instanceof GLIssueNode ) {
            GLIssueNode issueNode = (GLIssueNode) node;
            controller.openEditor( issueNode.getUserObject() );
        }
    }

}
