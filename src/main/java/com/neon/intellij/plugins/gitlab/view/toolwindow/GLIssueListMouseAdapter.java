package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.neon.intellij.plugins.gitlab.ChangeIssueStateAction;
import com.neon.intellij.plugins.gitlab.DeleteIssueAction;
import com.neon.intellij.plugins.gitlab.OpenIssueEditorAction;
import com.neon.intellij.plugins.gitlab.RefreshProjectIssuesAction;
import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GLIssueListMouseAdapter extends MouseAdapter {

    private final JTree tree;

    private final OpenIssueEditorAction openIssueEditorAction;
    private final RefreshProjectIssuesAction refreshProjectIssuesAction;
    private final DeleteIssueAction deleteIssueAction;
    private final ChangeIssueStateAction changeIssueStateAction;

    public GLIssueListMouseAdapter(final OpenIssueEditorAction openIssueEditorAction,
                                   final RefreshProjectIssuesAction refreshProjectIssuesAction,
                                   final DeleteIssueAction deleteIssueAction,
                                   final ChangeIssueStateAction changeIssueStateAction,
                                   final JTree tree)
    {
        this.openIssueEditorAction = openIssueEditorAction;
        this.refreshProjectIssuesAction = refreshProjectIssuesAction;
        this.deleteIssueAction = deleteIssueAction;
        this.changeIssueStateAction = changeIssueStateAction;
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

            JPopupMenu popup = new GLProjectPopup( projectNode, openIssueEditorAction, refreshProjectIssuesAction );
            popup.show( tree, x, y );

        } else if ( node instanceof GLIssueNode) {
            GLIssueNode issueNode = (GLIssueNode) node;

            JPopupMenu popup = new GLIssuePopup( issueNode, openIssueEditorAction, deleteIssueAction, changeIssueStateAction );
            popup.show( tree, x, y );
        }
    }

    private void doubleClick( final DefaultMutableTreeNode node ) {
        if ( node instanceof GLProjectNode ) {
            refreshProjectIssuesAction.accept((GLProjectNode) node);
        } else if ( node instanceof GLIssueNode ) {
            GLIssueNode issueNode = (GLIssueNode) node;
            openIssueEditorAction.accept( issueNode.getUserObject() );
        }
    }

}
