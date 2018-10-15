package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.neon.intellij.plugins.gitlab.controller.GLIController;
import com.neon.intellij.plugins.gitlab.model.gitlab.GLIssueState;
import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.gitlab.api.models.GitlabUser;

/**
 * kindly 'stolen' and adapted from http://www.adrianwalker.org/2012/04/filtered-jtree.html
 */
public final class FilteredTreeModel implements TreeModel {

    private TreeModel treeModel;

    private String filter;

    private boolean showClosedIssues = false;

    private String author;

    private String assignee;


    public FilteredTreeModel( final TreeNode root ) {
        this( new DefaultTreeModel( root ) );
    }

    public FilteredTreeModel(final TreeModel treeModel) {
        this.treeModel = treeModel;
        this.filter = "";
    }

    public TreeModel getTreeModel() {
        return treeModel;
    }

    public void setFilter(final String filter) {
        this.filter = filter;
    }

    public void setShowClosedIssues(boolean showClosedIssues) {
        this.showClosedIssues = showClosedIssues;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    private boolean recursiveMatch( final Object node ) {
        boolean matches = true;

        String title = null;
        if ( node instanceof GLIssueNode) {
            title = ( ( GLIssueNode ) node ).getUserObject().getTitle();
            GLIssueState issueState = GLIssueState.fromValue(((GLIssueNode) node).getUserObject().getState());

            GitlabUser authorUser = ((GLIssueNode) node).getUserObject().getAuthor();
            String author = GLIController.getLabel( authorUser );
            GitlabUser assigneeUser = ((GLIssueNode) node).getUserObject().getAssignee();
            String assignee = GLIController.getLabel( assigneeUser );

            matches = ( issueState == null || (showClosedIssues || ! GLIssueState.CLOSED.equals( issueState ) ) );
            if ( this.author != null && ! this.author.trim().isEmpty() ) {
                matches &= this.author.equals( author );
            }
            if ( this.assignee != null && ! this.assignee.trim().isEmpty() ) {
                matches &= this.assignee.equals( assignee );
            }

        } else if ( node instanceof GLProjectNode) {
            title = ( ( GLProjectNode ) node ).getUserObject().getName();
        }
        matches &= ( title != null && title.toLowerCase().contains( filter.toLowerCase() ) );


        int childCount = treeModel.getChildCount(node);
        for (int i = 0; i < childCount; i++) {
            Object child = treeModel.getChild(node, i);
            matches |= recursiveMatch( child );
        }
        return matches;
    }

    @Override
    public Object getRoot() {
        return treeModel.getRoot();
    }

    @Override
    public Object getChild(final Object parent, final int index) {
        int count = 0;
        int childCount = treeModel.getChildCount(parent);
        for (int i = 0; i < childCount; i++) {
            Object child = treeModel.getChild(parent, i);
            if (recursiveMatch(child)) {
                if (count == index) {
                    return child;
                }
                count++;
            }
        }
        return null;
    }

    @Override
    public int getChildCount(final Object parent) {
        int count = 0;
        int childCount = treeModel.getChildCount(parent);
        for (int i = 0; i < childCount; i++) {
            Object child = treeModel.getChild(parent, i);
            if (recursiveMatch(child)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isLeaf(final Object node) {
        return treeModel.isLeaf(node);
    }

    @Override
    public void valueForPathChanged(final TreePath path, final Object newValue) {
        treeModel.valueForPathChanged(path, newValue);
    }

    @Override
    public int getIndexOfChild(final Object parent, final Object childToFind) {
        int childCount = treeModel.getChildCount(parent);
        for (int i = 0; i < childCount; i++) {
            Object child = treeModel.getChild(parent, i);
            if (recursiveMatch(child)) {
                if (childToFind.equals(child)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(final TreeModelListener l) {
        treeModel.addTreeModelListener(l);
    }

    @Override
    public void removeTreeModelListener(final TreeModelListener l) {
        treeModel.removeTreeModelListener(l);
    }
}
