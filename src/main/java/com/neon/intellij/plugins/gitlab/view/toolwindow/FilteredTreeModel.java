package com.neon.intellij.plugins.gitlab.view.toolwindow;

import com.neon.intellij.plugins.gitlab.model.gitlab.GIPGroup;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPIssue;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPProject;
import com.neon.intellij.plugins.gitlab.model.gitlab.GIPUser;
import com.neon.intellij.plugins.gitlab.model.intellij.GLGroupNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLIssueNode;
import com.neon.intellij.plugins.gitlab.model.intellij.GLProjectNode;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * kindly 'stolen' and adapted from http://www.adrianwalker.org/2012/04/filtered-jtree.html
 */
public final class FilteredTreeModel implements TreeModel {

    private TreeModel treeModel;

    private String filter;

    private boolean showEmptyNodes = false;

    private boolean showClosedIssues = false;

    private GIPUser author;

    private GIPUser assignee;


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

    public void setShowEmptyNodes(boolean showEmptyNodes) {
        this.showEmptyNodes = showEmptyNodes;
    }

    public void setFilter(final String filter) {
        this.filter = filter;
    }

    public void setShowClosedIssues(boolean showClosedIssues) {
        this.showClosedIssues = showClosedIssues;
    }

    public void setAuthor(GIPUser author) {
        this.author = author;
    }

    public void setAssignee(GIPUser assignee) {
        this.assignee = assignee;
    }

    private boolean recursiveMatch( final Object node ) {
        boolean matches = true;

        if ( node instanceof GLGroupNode ) {
            GIPGroup group = ((GLGroupNode) node).getUserObject();

            if ( ! showEmptyNodes ) {
                matches = ((GLGroupNode) node).getChildCount() > 0;
            }

            matches &= group.name.toLowerCase().contains(filter.toLowerCase());
        } else if ( node instanceof GLProjectNode ) {
            GIPProject project = ((GLProjectNode) node).getUserObject();

            if ( ! showEmptyNodes ) {
                matches = ((GLProjectNode) node).getChildCount() > 0;
            }

            matches &= project.name.toLowerCase().contains(filter.toLowerCase());
        } else if ( node instanceof GLIssueNode ) {
            GIPIssue issue = ((GLIssueNode) node).getUserObject();

//            by state
            if ( ! showClosedIssues ) {
                matches = ! "closed".equalsIgnoreCase( issue.state );
            }

//            by text
            if ( filter != null && ! filter.trim().isEmpty() ) {
                matches &= issue.title.toLowerCase().contains(filter.toLowerCase());
            }

//            by author
            if ( author != null && author.id != null ) {
                matches &= author.id.equals( issue.author.id );
            }

//            by assignee
            if ( assignee != null && assignee.id != null ) {
                matches &= issue.assignees != null && ! issue.assignees.isEmpty();
                if ( matches ) {
                    matches = issue.assignees.stream()
                            .anyMatch(gipIssueAssignee -> assignee.id.equals( gipIssueAssignee.id ));
                }
            }
        }

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
